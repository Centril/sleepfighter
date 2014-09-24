/*
 * Copyright 2014 toxbee.se
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package se.toxbee.ormlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

import com.google.common.collect.Maps;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.io.File;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Map;

import se.toxbee.ormlite.migration.MigrationExecutor;

/**
 * Provides a base implementation OrmLiteSqliteOpenHelper for persistence layer.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Sep 21, 2013
 */
public abstract class BaseOrmHelper extends OrmLiteSqliteOpenHelper {
	abstract protected Class<?>[] classes();
	abstract protected MigrationExecutor migrationExecutor();

	protected String tag() {
		return this.getClass().getSimpleName();
	}

	protected final Map<Class<?>, PersistenceExceptionDao<Class<?>, ?>> daoMap = Maps.newHashMap();
	protected final Map<Class<?>, DaoInitRunner<?>> daoInitRunners = Maps.newHashMap();

	protected void initWith( Class<?> clazz, DaoInitRunner<?> runner ) {
		this.daoInitRunners.put( clazz, runner );
	}

	/**
	 * Returns a DAO for the given class.
	 *
	 * @param clazz the class.
	 * @param <T> the element type.
	 * @param <I> the key type.
	 * @return the dao.
	 */
	public <T, I> PersistenceExceptionDao<T, I> dao( Class<T> clazz ) {
		@SuppressWarnings( "unchecked" )
		PersistenceExceptionDao<T, I> dao = (PersistenceExceptionDao<T, I>) this.daoMap.get( clazz );

		if ( dao == null ) {
			dao = this.getExceptionDao( clazz );

			this.runDaoInit( clazz, dao );

			@SuppressWarnings( "unchecked" )
			PersistenceExceptionDao<Class<?>, ?> castDao = (PersistenceExceptionDao<Class<?>, ?>) dao;

			this.daoMap.put( clazz, castDao );
		}

		return dao;
	}

	/**
	 * Returns a DAO with String key for the given class.
	 *
	 * @param clazz the class.
	 * @param <T> the element type.
	 * @return the dao.
	 */
	public <T> PersistenceExceptionDao<T, String> dao_s( Class<T> clazz ) {
		return this.dao( clazz );
	}

	/**
	 * Returns a DAO with integer key for the given class.
	 *
	 * @param clazz the class.
	 * @param <T> the element type.
	 * @return the dao.
	 */
	public <T> PersistenceExceptionDao<T, Integer> dao_i( Class<T> clazz ) {
		return this.dao( clazz );
	}

	/**
	 * Runs the DaoInitRunner for clazz if any.
	 *
	 * @param clazz the to run for.
	 * @param dao the dao for the clazz.
	 */
	protected <D extends PersistenceExceptionDao<T, ?>, T> void runDaoInit( Class<T> clazz, D dao ) {
		@SuppressWarnings( "unchecked" )
		DaoInitRunner<T> onInit = (DaoInitRunner<T>) this.daoInitRunners.get( clazz );
		if ( onInit != null ) {
			onInit.daoInit( clazz, dao );
		}
	}

	/**
	 * Get a PersistenceExceptionDao for given class.
	 * This uses the {@link com.j256.ormlite.dao.DaoManager}
	 * to cache the DAO for future gets.
	 *
	 * @param clazz the class object to get Dao for.
	 */
	protected <D extends PersistenceExceptionDao<T, ?>, T> D getExceptionDao(Class<T> clazz) {
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
	 * @param context
	 *            Associated content from the application. This is needed to locate the database.
	 * @param databaseName
	 *            Name of the database we are opening.
	 * @param factory
	 *            Cursor factory or null if none.
	 * @param databaseVersion
	 *            Version of the database we are opening. This causes {@link #onUpgrade(SQLiteDatabase, int, int)} to be
	 *            called if the stored database is a different version.
	 */
	public BaseOrmHelper(Context context, String databaseName, CursorFactory factory, int databaseVersion) {
		super( context, databaseName, factory, databaseVersion );
	}

	/**
	 * Same as the other constructor with the addition of a file-id of the table config-file. See
	 * {@link com.j256.ormlite.android.apptools.OrmLiteConfigUtil} for details.
	 *
	 * @param context
	 *            Associated content from the application. This is needed to locate the database.
	 * @param databaseName
	 *            Name of the database we are opening.
	 * @param factory
	 *            Cursor factory or null if none.
	 * @param databaseVersion
	 *            Version of the database we are opening. This causes {@link #onUpgrade(SQLiteDatabase, int, int)} to be
	 *            called if the stored database is a different version.
	 * @param configFileId
	 *            file-id which probably should be a R.raw.ormlite_config.txt or some static value.
	 */
	public BaseOrmHelper( Context context, String databaseName, CursorFactory factory, int databaseVersion, int configFileId ) {
		super( context, databaseName, factory, databaseVersion, configFileId );
	}

	/**
	 * Same as the other constructor with the addition of a config-file. See {@link com.j256.ormlite.android.apptools.OrmLiteConfigUtil} for details.
	 *
	 * @param context
	 *            Associated content from the application. This is needed to locate the database.
	 * @param databaseName
	 *            Name of the database we are opening.
	 * @param factory
	 *            Cursor factory or null if none.
	 * @param databaseVersion
	 *            Version of the database we are opening. This causes {@link #onUpgrade(SQLiteDatabase, int, int)} to be
	 *            called if the stored database is a different version.
	 * @param configFile
	 *            Configuration file to be loaded.
	 */
	public BaseOrmHelper( Context context, String databaseName, CursorFactory factory, int databaseVersion, File configFile ) {
		super( context, databaseName, factory, databaseVersion, configFile );
	}

	/**
	 * Same as the other constructor with the addition of a input stream to the table config-file. See
	 * {@link com.j256.ormlite.android.apptools.OrmLiteConfigUtil} for details.
	 *
	 * @param context
	 *            Associated content from the application. This is needed to locate the database.
	 * @param databaseName
	 *            Name of the database we are opening.
	 * @param factory
	 *            Cursor factory or null if none.
	 * @param databaseVersion
	 *            Version of the database we are opening. This causes {@link #onUpgrade(SQLiteDatabase, int, int)} to be
	 *            called if the stored database is a different version.
	 * @param stream
	 *            Stream opened to the configuration file to be loaded.
	 */
	public BaseOrmHelper(Context context, String databaseName, CursorFactory factory, int databaseVersion, InputStream stream) {
		super(context, databaseName, factory, databaseVersion, stream );
	}

	/**
	 * Called when database is created (on install of android app).<br/>
	 * Creates all tables, etc.
	 */
	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
		try {
			for ( Class<?> clazz : this.classes() ) {
				TableUtils.createTable( connectionSource, clazz );
			}
		} catch ( SQLException e ) {
			Log.e( tag(), "Fatal error: Couldn't create database." );
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

		// Execute the migration.
		MigrationExecutor mig = this.migrationExecutor();
		boolean success = mig.execute( cs, db, oldVersion, newVersion );

		// Rebuild on failure.
		if ( !success ) {
			Log.e( tag(), "Fatal error: Couldn't upgrade database." );
			this.rebuild();
		}

		// Nuke caches.
		this.nukeCache();
	}

	/**
	 * Returns a Dao which you can run raw statements on.
	 *
	 * @return the Dao.
	 */
	public PersistenceExceptionDao<?, ?> rawDao() {
		return this.dao( classes()[0] );
	}

	/**
	 * Drops all DB tables for clazzes.
	 *
	 * @param clazzes the classes to drop tables for.
	 * @return this.
	 */
	public BaseOrmHelper drop( Class<?>[] clazzes ) {
		try {
			for ( Class<?> clazz : clazzes ) {
				TableUtils.dropTable( this.getConnectionSource(), clazz, true );
			}
		} catch ( SQLException e ) {
			Log.e( tag(), "Can't drop databases", e );
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
	public BaseOrmHelper clear( Class<?>[] clazzes ) {
		try {
			for ( Class<?> clazz : clazzes ) {
				TableUtils.clearTable( this.getConnectionSource(), clazz );
			}
		} catch ( SQLException e ) {
			Log.e( tag(), "Can't drop databases", e );
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
	public BaseOrmHelper drop( Class<?> clazz ) {
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
	public BaseOrmHelper clear( Class<?> clazz ) {
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
		this.drop( this.classes() );
		this.onCreate( db, this.getConnectionSource() );
	}

	/**
	 * Closes the database connections and clear any cached DAOs.
	 */
	@Override
	public void close() {
		super.close();
		this.nukeCache();
	}

	/**
	 * Nukes the cache if any (Dao + Object.
	 */
	protected void nukeCache() {
		// Get rid of reference.
		this.daoMap.clear();

		// Clear all caches (Dao + Object).
		DaoManager.clearCache();
	}
}