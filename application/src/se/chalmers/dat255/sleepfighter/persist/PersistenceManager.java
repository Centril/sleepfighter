package se.chalmers.dat255.sleepfighter.persist;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;

/**
 * Handles all reads and writes to persistence.<br/>
 * There should be no reason to keep more than 1 instance of this object.
 * 
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Sep 21, 2013
 */
public class PersistenceManager {
	/**
	 * Constructs the PersistenceManager.
	 */
	public PersistenceManager() {
	}

	private OrmHelper ormHelper = null;

	/*
	@Override
	protected void onDestroy() {
		if ( this.ormHelper != null ) {
			OpenHelperManager.releaseHelper();
			this.ormHelper = null;
		}
	}

	private OrmHelper getHelper() {
		if ( this.ormHelper == null ) {
			this.ormHelper = OpenHelperManager.getHelper( this, OrmHelper.class );
		}
		return this.ormHelper;
	}

	/**
	 * Sets up the manager, any futher calls = noop.
	 */
	public void setup() {
		// Guard.
		if ( this.isSetup ) {
			return;
		}
		this.isSetup = true;

		this.setupImpl();
	}

	private void setupImpl() {
	}

	private OrmLiteSqliteOpenHelper helper;

	private ConnectionSource conn;

	private boolean isSetup = false;
}