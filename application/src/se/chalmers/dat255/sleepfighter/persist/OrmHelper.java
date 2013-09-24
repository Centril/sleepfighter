package se.chalmers.dat255.sleepfighter.persist;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import se.chalmers.dat255.sleepfighter.model.Alarm;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

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
	private static final int DATABASE_VERSION = 2;

	// the DAO object we use to access the SimpleData table
	private PersistenceExceptionDao<Alarm, Integer> alarmDao = null;

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
			Log.i( OrmHelper.class.getName(), "onCreate" );
			TableUtils.createTable( connectionSource, Alarm.class );
		} catch ( SQLException e ) {
			Log.e( OrmHelper.class.getName(), "Can't create database", e );
			throw new PersistenceException( e );
		}
	}

	/**
	 * <p>Called when application is updates from google play or something<br/>
	 * and this entails a changed model and therefore changed DB structure.</p>
	 *
	 * <p>Migration should be handled here.<br/>
	 * For now, the DB is just dropped and recreated so all info is lost.</p>
	 *
	 * TODO: don't drop info.
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
		try {
			// Drop DB & create new ones.
			Log.i(OrmHelper.class.getName(), "onUpgrade");
			TableUtils.dropTable(connectionSource, Alarm.class, true);
			onCreate(db, connectionSource);
		} catch (SQLException e) {
			Log.e(OrmHelper.class.getName(), "Can't drop databases", e);
			throw new PersistenceException(e);
		}
	}

	/**
	 * Rebuilds all data-structures. Any data is lost.
	 */
	public void rebuild() {
		Log.d(OrmHelper.class.getName(), "onRestart");

		// Drop all tables.
		List<String> tables = new ArrayList<String>();
		Cursor cursor = this.getReadableDatabase().rawQuery("SELECT * FROM sqlite_master WHERE type='table';", null );
		cursor.moveToFirst();
		while ( !cursor.isAfterLast() ) {
			String tableName = cursor.getString( 1 );
			if ( !tableName.equals( "android_metadata" ) && !tableName.equals( "sqlite_sequence" ) ) {
				tables.add( tableName );
			}
			cursor.moveToNext();
		}
		cursor.close();

		SQLiteDatabase db = this.getWritableDatabase();
		for(String tableName : tables) {
		    db.execSQL("DROP TABLE IF EXISTS " + tableName);
		}

		onCreate(db, connectionSource);
	}

	/**
	 * Returns DAO for Alarm model.<br/>
	 * It either creates or returns a cached object.
	 *
	 * @return the DAO for Alarm model.
	 */
	public PersistenceExceptionDao<Alarm, Integer> getAlarmDao() throws PersistenceException {
		if ( this.alarmDao == null ) {
			this.alarmDao = this.getExceptionDao( Alarm.class );
		}
		return this.alarmDao;
	}

	/**
	 * Get a PersistenceExceptionDao for given class. This uses the {@link DaoManager} to cache the DAO for future gets.
	 *
	 * @param clazz the class object to get Dao for.
	 */
	public <D extends PersistenceExceptionDao<T, ?>, T> D getExceptionDao(Class<T> clazz) {
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
	 * Closes the database connections and clear any cached DAOs.
	 */
	@Override
	public void close() {
		super.close();

		this.alarmDao = null;
	}
}