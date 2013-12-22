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
package se.toxbee.sleepfighter.persist.migration;

import java.sql.SQLException;
import java.util.concurrent.Callable;

import se.toxbee.sleepfighter.utils.migration.IMigrationException;
import se.toxbee.sleepfighter.utils.migration.IMigrationExecutor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.misc.TransactionManager;
import com.j256.ormlite.support.ConnectionSource;

/**
 * MigrationExecutor is responsible for executing migrations.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Nov 14, 2013
 */
public final class MigrationExecutor extends IMigrationExecutor<MigrationUtil, Migrater> {
	private static final String TAG = MigrationExecutor.class.getSimpleName();

	@Override
	protected Class<Migrater> clazz() {
		return Migrater.class;
	}
	
	@Override
	protected Class<?>[] definedMigrations() {
		return DefinedMigrations.get();
	}

	private boolean fail( Throwable e ) {
		Log.e( TAG, "Error during migration.", e );
		return false;
	}

	/**
	 * Performs migration from oldVersion to newVersion returning true on success and false on failure.
	 *
	 * @param cs the ConnectionSource.
	 * @param db the SQLiteDatabase.
	 * @param originVersion the origin version we're starting from.
	 * @param targetVersion the target version we're heading to.
	 * @return true if the migration was successful.
	 */
	public boolean execute( final ConnectionSource cs, final SQLiteDatabase db, final int originVersion, final int targetVersion ) {
		// Get in a transaction.
		try {
			return TransactionManager.callInTransaction( cs, new Callable<Boolean>() {
				@Override
				public Boolean call() throws Exception {
					return perform( cs, db, originVersion, targetVersion );
				}
			} );
		} catch ( SQLException e ) {
			return fail( e );
		}
	}

	/**
	 * Actually performs the migration.
	 *
	 * @param cs the ConnectionSource.
	 * @param db the SQLiteDatabase.
	 * @param originVersion the origin version we're starting from.
	 * @param targetVersion the target version we're heading to.
	 * @return true if the migration was successful.
	 */
	private boolean perform( ConnectionSource cs, SQLiteDatabase db, int originVersion, int targetVersion ) {
		try {
			IMigrationException.tooOld( originVersion, DefinedMigrations.REBUILD_BELOW_VERSION );
			this.apply( new MigrationUtil( db, cs ), originVersion, targetVersion );
			return true;
		} catch ( IMigrationException e ) {
			return fail( e );
		}
	}

}
