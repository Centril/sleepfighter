/*******************************************************************************
 * Copyright (c) 2013 See AUTHORS file.
 * 
 * This file is part of SleepFighter.
 * 
 * SleepFighter is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * SleepFighter is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with SleepFighter. If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package se.toxbee.sleepfighter.persist;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

import se.toxbee.sleepfighter.model.Alarm;
import se.toxbee.sleepfighter.model.SnoozeConfig;
import se.toxbee.sleepfighter.model.audio.AudioConfig;
import se.toxbee.sleepfighter.model.audio.AudioSource;
import se.toxbee.sleepfighter.model.challenge.ChallengeConfig;
import se.toxbee.sleepfighter.model.challenge.ChallengeConfigSet;
import se.toxbee.sleepfighter.model.challenge.ChallengeParam;
import se.toxbee.sleepfighter.model.gps.GPSFilterArea;
import se.toxbee.sleepfighter.persist.migration.MigrationException;
import se.toxbee.sleepfighter.persist.migration.MigrationException.Reason;
import se.toxbee.sleepfighter.persist.migration.VersionMigrater;
import se.toxbee.sleepfighter.utils.message.Message;
import se.toxbee.sleepfighter.utils.message.MessageBus;
import se.toxbee.sleepfighter.utils.message.MessageBusHolder;
import se.toxbee.sleepfighter.utils.reflect.ReflectionUtil;
import se.toxbee.sleepfighter.utils.string.StringUtils;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;
import com.google.common.primitives.Ints;
import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.misc.TransactionManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

/**
 * Provides an OrmLiteSqliteOpenHelper for persistence layer.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Sep 21, 2013
 */
public class OrmHelper extends OrmLiteSqliteOpenHelper implements MessageBusHolder {
	private static final String TAG = OrmHelper.class.getSimpleName();

	// Name of the database file in application.
	private static final String DATABASE_NAME = "sleep_fighter.db";

	// Current database version, change when database structure changes.
	// IMPORTANT: If you update this, also add the old version to the switch/case
	private static final int DATABASE_VERSION = 23;

	// Package containing migrations for versions, relative to this one.
	private static final String MIGRATION_PACKAGE = ".upgrades";

	// List of all classes that is managed by helper.
	private static final Class<?>[] CLASSES = new Class<?>[] {
		Alarm.class,

		AudioSource.class,
		AudioConfig.class,

		SnoozeConfig.class,

		ChallengeConfigSet.class,
		ChallengeConfig.class,
		ChallengeParam.class,

		GPSFilterArea.class
	};

	/**
	 * OrmAlterFailureEvent occurs when migration fails.
	 *
	 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
	 * @version 1.0
	 * @since Nov 13, 2013
	 */
	public static enum OrmAlterFailureEvent implements Message {
		UPGRADE, CREATE
	}

	private final Map<Class<?>, PersistenceExceptionDao<Class<?>, Integer>> daoMap = Maps.newHashMap();
	private final Map<Class<?>, DaoInitRunner<?>> daoInitRunners = Maps.newHashMap();

	private MessageBus<Message> bus;

	/**
	 * DaoInitRunner is a method run when a Dao is first initialized.
	 *
	 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
	 * @version 1.0
	 * @since Nov 12, 2013
	 */
	public static interface DaoInitRunner<T> {
		/**
		 * Called when Dao is initialized.
		 *
		 * @param helper the OrmHelper.
		 * @param daoClazz the Class of the Dao.
		 * @param dao the Dao itself.
		 */
		public <D extends PersistenceExceptionDao<T, Integer>> void daoInit( OrmHelper helper, Class<T> daoClazz, D dao );
	}

	/**
	 * CacheEnabler is a DaoInitRunner that simply enables object cache.
	 *
	 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
	 * @version 1.0
	 * @since Nov 12, 2013
	 */
	public class CacheEnabler<T> implements DaoInitRunner<T> {
		@Override
		public <D extends PersistenceExceptionDao<T, Integer>> void daoInit( OrmHelper helper, Class<T> daoClazz, D dao ) {
			dao.setObjectCache( true );
		}
	}

	// Initialize DaoInitRunner:s here.
	{
		Map<Class<?>, DaoInitRunner<?>> r = this.daoInitRunners;
		r.put( Alarm.class, new CacheEnabler<Alarm>() );
	}

	public <D extends PersistenceExceptionDao<T, Integer>, T> D dao( Class<T> clazz ) {
		@SuppressWarnings( "unchecked" )
		D dao = (D) this.daoMap.get( clazz );

		if ( dao == null ) {
			dao = this.getExceptionDao( clazz );

			this.runDaoInit( clazz, dao );

			@SuppressWarnings( "unchecked" )
			PersistenceExceptionDao<Class<?>, Integer> castDao = (PersistenceExceptionDao<Class<?>, Integer>) dao;

			this.daoMap.put( clazz, castDao );
		}

		return dao;
	}

	/**
	 * Runs the DaoInitRunner for clazz if any.
	 *
	 * @param clazz the to run for.
	 * @param dao the dao for the clazz.
	 */
	private <D extends PersistenceExceptionDao<T, Integer>, T> void runDaoInit( Class<T> clazz, D dao ) {
		@SuppressWarnings( "unchecked" )
		DaoInitRunner<T> onInit = (DaoInitRunner<T>) this.daoInitRunners.get( clazz );
		if ( onInit != null ) {
			onInit.daoInit( this, clazz, dao );
		}
	}

	/**
	 * Get a PersistenceExceptionDao for given class. This uses the {@link DaoManager} to cache the DAO for future gets.
	 *
	 * @param clazz the class object to get Dao for.
	 */
	private <D extends PersistenceExceptionDao<T, ?>, T> D getExceptionDao(Class<T> clazz) {
		try {
			Dao<T, ?> dao = getDao(clazz);
			@SuppressWarnings({ "unchecked", "rawtypes" })
			D castDao = (D) new PersistenceExceptionDao(dao);
			return castDao;
		} catch (SQLException e) {
			throw new PersistenceException("Could not create RuntimeExcepitionDao for class " + clazz, e);
		}
	}

	/**
	 * Constructs the helper from a given context.
	 *
	 * @param context the context to use.
	 */
	public OrmHelper( Context context ) {
		super( context, DATABASE_NAME, null, DATABASE_VERSION );
	}

	/**
	 * Called when database is created (on install of android app).<br/>
	 * Creates all tables, etc.
	 */
	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
		try {
			for ( Class<?> clazz : CLASSES ) {
				TableUtils.createTable( connectionSource, clazz );
			}
		} catch ( SQLException e ) {
			this.bus.publish( OrmAlterFailureEvent.CREATE );
			Log.e( OrmHelper.class.getName(), "Fatal error: Can't create database", e );
			throw new PersistenceException( e );
		}
	}

	/**
	 * <p>Called when application is updates from google play or something<br/>
	 * and this entails a changed model and therefore changed DB structure.</p>
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource cs, int oldVersion, int newVersion) {
		if ( oldVersion == newVersion ) {
			return;
		}

		if ( !this.performMigration( cs, db, oldVersion, newVersion ) ) {
			this.getMessageBus().publishAsync( OrmAlterFailureEvent.UPGRADE );
			this.rebuild();
		}
	}

	/**
	 * Performs migration from oldVersion to newVersion returning true on success and false on failure.
	 *
	 * @param cs
	 * @param oldVersion
	 * @param newVersion
	 * @return
	 */
	private boolean performMigration( final ConnectionSource cs, final SQLiteDatabase db, final int oldVersion, final int newVersion ) {
		final OrmHelper self = this;
		try {
			return TransactionManager.callInTransaction( cs, new Callable<Boolean>() {
				@Override
				public Boolean call() throws Exception {
					try {
						final List<VersionMigrater> list = self.assembleMigraters( oldVersion, newVersion );
						PersistenceExceptionDao<?, Integer> rawDao = self.rawDao();

						for ( VersionMigrater m : list ) {
							m.applyMigration( cs, db, rawDao );
						}

						return true;
					} catch ( MigrationException e ) {
						Log.e( TAG, "Error during migration.", e );
						return false;
					} catch ( Exception e ) {
						Log.e( TAG, "Error during migration.", e );
						return false;
					}
				}
			} );
		} catch ( SQLException e ) {
			Log.e( TAG, "Error during migration.", e );
			return false;
		}
	}

	private List<VersionMigrater> assembleMigraters( int oldVersion, int newVersion ) throws MigrationException {
		List<VersionMigrater> list = this.findMigraters( oldVersion );

		// Let the migraters report what versions to skip.
		Set<Integer> skipVersions = Sets.newHashSet();
		for ( VersionMigrater m : list ) {
			Collection<Integer> skip = m.skipVersions( oldVersion, newVersion );
			if ( skip != null ) {
				skipVersions.addAll( skip );
			}
		}

		// Remove any migraters to skip.
		Iterator<VersionMigrater> it = list.iterator();
		while ( it.hasNext() ) {
			VersionMigrater m = it.next();
			if ( skipVersions.contains( m.versionCode() ) ) {
				it.remove();
			}
		}

		if ( list.isEmpty() ) {
			throw new MigrationException( "No migraters found, the version is too old", Reason.TOO_OLD, oldVersion );
		}

		// Finally sort the migraters based on version.
		Collections.sort( list, new Ordering<VersionMigrater>() {
			@Override
			public int compare( VersionMigrater a, VersionMigrater b ) {
				return Ints.compare( a.versionCode(), b.versionCode() );
			}
		} );

		return list;
	}

	/**
	 * Finds any migraters available.
	 *
	 * @param originVersion the version we are coming from.
	 * @return a list of migraters.
	 * @throws MigrationException If there was some error in finding migraters,
	 *							  this is super almost {@link AssertionError} level serious.
	 */
	private List<VersionMigrater> findMigraters( int originVersion ) throws MigrationException {
		List<VersionMigrater> list = Lists.newArrayList();

		try {
			// Find all classes that are 
			ClassPath cp = ReflectionUtil.getClassPath();
			String pack = this.getClass().getPackage().getName() + MIGRATION_PACKAGE;
			for ( ClassInfo info : cp.getTopLevelClassesRecursive( pack ) ) {
				// Skip the class if the version is not appropriate.
				int version = StringUtils.getDigitsIn( info.getSimpleName() );
				if ( originVersion >= version ) {
					continue;
				}

				// Load the class, skip if not a migrater.
				Class<? extends VersionMigrater> clazz = ReflectionUtil.asSubclass( info.load(), VersionMigrater.class );
				if ( clazz == null ) {
					continue;
				}

				// Time to construct the migrater.
				VersionMigrater migrater;

				try {
					Constructor<? extends VersionMigrater> ctor = clazz.getConstructor();
					migrater = (VersionMigrater) ctor.newInstance();
				} catch ( NoSuchMethodException e ) {
					throw new MigrationException( "No no-arg constructor found for migrater", e, version );
				} catch ( InstantiationException e ) {
					throw new MigrationException( "Could not instantiate migrater", e, version );
				} catch ( IllegalAccessException e ) {
					throw new MigrationException( "Could not access migrater constructor", e, version );
				} catch ( InvocationTargetException e ) {
					throw new MigrationException( "Migrater constructor threw exception.", e, version );
				}

				// Double check to ensure migrater version is appropriate.
				if ( originVersion > migrater.versionCode() ) {
					continue;
				}

				// Finally, we've got a migrater.
				list.add( migrater );
			}
		} catch( RuntimeException e ) {
			throw new MigrationException( "Failure in finding migraters", e, originVersion );
		}

		return list;
	}

	/**
	 * Returns a Dao which you can run raw statements on.
	 *
	 * @return the Dao.
	 */
	public PersistenceExceptionDao<?, Integer> rawDao() {
		return this.dao( Alarm.class );
	}

	/**
	 * Drops all DB tables for clazzes.
	 *
	 * @param clazzes the classes to drop tables for.
	 * @return this.
	 */
	public OrmHelper drop( Class<?>[] clazzes ) {
		try {
			for ( Class<?> clazz : clazzes ) {
				TableUtils.dropTable( this.getConnectionSource(), clazz, true );
			}
		} catch ( SQLException e ) {
			Log.e( OrmHelper.class.getName(), "Can't drop databases", e );
			throw new PersistenceException( e );
		}
		return this;
	}

	/**
	 * Clears all DB tables for clazzes.
	 *
	 * @param clazzes the classes to clear tables for.
	 * @return this.
	 */
	public OrmHelper clear( Class<?>[] clazzes ) {
		try {
			for ( Class<?> clazz : clazzes ) {
				TableUtils.clearTable( this.getConnectionSource(), clazz );
			}
		} catch ( SQLException e ) {
			Log.e( OrmHelper.class.getName(), "Can't drop databases", e );
			throw new PersistenceException( e );
		}
		return this;
	}

	/**
	 * Drops a DB table for given clazz.
	 *
	 * @param clazz the class to drop table for.
	 * @return this.
	 */
	public OrmHelper drop( Class<?> clazz ) {
		try {
			TableUtils.dropTable( this.getConnectionSource(), clazz, true );
		} catch ( SQLException e ) {
			throw new PersistenceException( e );
		}

		return this;
	}

	/**
	 * Clears a DB table for given clazz.
	 *
	 * @param clazz the class to clear table for.
	 * @return this.
	 */
	public OrmHelper clear( Class<?> clazz ) {
		try {
			TableUtils.clearTable( this.getConnectionSource(), clazz );
		} catch ( SQLException e ) {
			throw new PersistenceException( e );
		}

		return this;
	}

	/**
	 * Rebuilds database. Any data is lost.
	 */
	public void rebuild() {
		SQLiteDatabase db = this.getWritableDatabase();
		this.drop( CLASSES );
		this.onCreate( db, this.getConnectionSource() );
	}

	/**
	 * Closes the database connections and clear any cached DAOs.
	 */
	@Override
	public void close() {
		super.close();

		// Get rid of reference.
		this.daoMap.clear();

		// Clear all caches (Dao + Object).
		DaoManager.clearCache();
	}


	@Override
	public void setMessageBus( MessageBus<Message> bus ) {
		this.bus = bus;
	}

	@Override
	public MessageBus<Message> getMessageBus() {
		return this.bus;
	}
}