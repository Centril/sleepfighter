package se.chalmers.dat255.sleepfighter.persist;

import java.io.File;
import java.io.InputStream;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;

/**
 * Provides an OrmLiteSqliteOpenHelper for application.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Sep 21, 2013
 */
public class OrmHelper extends OrmLiteSqliteOpenHelper {
	static {
		OpenHelperManager.setOpenHelperClass( OrmHelper.class );
	}

	public OrmHelper( Context context, String databaseName,
			CursorFactory factory, int databaseVersion ) {
		super( context, databaseName, factory, databaseVersion );
	}

	public OrmHelper( Context context, String databaseName,
			CursorFactory factory, int databaseVersion, int configFileId ) {
		super( context, databaseName, factory, databaseVersion, configFileId );
	}

	public OrmHelper( Context context, String databaseName,
			CursorFactory factory, int databaseVersion, File configFile ) {
		super( context, databaseName, factory, databaseVersion, configFile );
	}

	public OrmHelper( Context context, String databaseName,
			CursorFactory factory, int databaseVersion, InputStream stream ) {
		super( context, databaseName, factory, databaseVersion, stream );
	}

	@Override
	public void onCreate( SQLiteDatabase database, ConnectionSource connectionSource ) {
	}

	@Override
	public void onUpgrade( SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion ) {
	}
}