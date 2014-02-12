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

package se.toxbee.sleepfighter.persist.migration;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.misc.TransactionManager;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.concurrent.Callable;

import se.toxbee.sleepfighter.utils.migration.IMigrationException;
import se.toxbee.sleepfighter.utils.migration.IMigrationExecutor;

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

	protected boolean fail( Throwable e ) {
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
