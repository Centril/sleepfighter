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

import java.sql.SQLException;
import java.util.Map;

import se.toxbee.sleepfighter.model.Alarm;
import se.toxbee.sleepfighter.model.SnoozeConfig;
import se.toxbee.sleepfighter.model.audio.AudioConfig;
import se.toxbee.sleepfighter.model.audio.AudioSource;
import se.toxbee.sleepfighter.model.challenge.ChallengeConfig;
import se.toxbee.sleepfighter.model.challenge.ChallengeConfigSet;
import se.toxbee.sleepfighter.model.challenge.ChallengeParam;
import se.toxbee.sleepfighter.model.gps.GPSFilterArea;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.common.collect.Maps;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

/**
 * Provides an OrmLiteSqliteOpenHelper for application.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Sep 21, 2013
 */
public class OrmHelper extends OrmLiteSqliteOpenHelper {
	// Name of the database file in application.
	private static final String DATABASE_NAME = "sleep_fighter.db";

	// Current database version, change when database structure changes.
	// IMPORTANT: If you update this, also add the old version to the switch/case
	private static final int DATABASE_VERSION = 23;

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

	private final Map<Class<?>, PersistenceExceptionDao<Class<?>, Integer>> daoMap = Maps.newHashMap();
	private final Map<Class<?>, DaoInitRunner<?>> daoInitRunners = Maps.newHashMap();

	/**
	 * DaoInitRunner is a method run when a Dao is first initialized.
	 *
	 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
	 * @version 1.0
	 * @since Nov 12, 2013
	 */
	public interface DaoInitRunner<T> {
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
	public OrmHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO.
		//super(context, DATABASE_NAME, null, DATABASE_VERSION, R.raw.ormlite_config);
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
			Log.e( OrmHelper.class.getName(), "Can't create database", e );
			throw new PersistenceException( e );
		}
	}

	/**
	 * <p>Called when application is updates from google play or something<br/>
	 * and this entails a changed model and therefore changed DB structure.</p>
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource,
			int oldVersion, int newVersion) {
		try {
			switch(oldVersion) {
			// Only test on 19 and higher, since that is the Release version
			case 19:
			case 20:
			case 21:
				this.dao( Alarm.class ).executeRaw("ALTER TABLE 'alarm'" +
						"ADD COLUMN 'isFlash' BOOLEAN DEFAULT false;");
			case 22:
				TableUtils.dropTable( connectionSource, ChallengeParam.class, true );
				TableUtils.createTable( connectionSource, ChallengeParam.class );
				// Break after the newest version
				break;
			default:
				// Just drop everything if the version is a pre-release version
				if (oldVersion < 19) {
					dropEverything(connectionSource);
					onCreate(db, connectionSource);
				}
				else {
					throw new RuntimeException("Strange Database Version in OrmHelper." +
							"Check database version and switch/case.");
				}
			}
		} catch (SQLException e) {
			Log.e(OrmHelper.class.getName(), "SQL exception during database upgrade", e);
			throw new PersistenceException(e);
		}
	}

	/**
	 * Tries to drop all tables in database.
	 *
	 * @param connectionSource
	 */
	private void dropEverything( ConnectionSource connectionSource ) {
		try {
			for ( Class<?> clazz : CLASSES ) {
				TableUtils.dropTable( connectionSource, clazz, true );
			}
		} catch ( SQLException e ) {
			Log.e( OrmHelper.class.getName(), "Can't drop databases", e );
			throw new PersistenceException( e );
		}
	}

	/**
	 * Rebuilds database. Any data is lost.
	 */
	public void rebuild() {
		SQLiteDatabase db = this.getWritableDatabase();
		this.dropEverything( this.connectionSource );
		this.onCreate( db, this.connectionSource);
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
}