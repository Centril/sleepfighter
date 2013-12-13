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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

import se.toxbee.sleepfighter.persist.migration.MigrationException.Reason;
import se.toxbee.sleepfighter.utils.reflect.ReflectionUtil;
import se.toxbee.sleepfighter.utils.string.StringUtils;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;
import com.j256.ormlite.misc.TransactionManager;
import com.j256.ormlite.support.ConnectionSource;

/**
 * MigrationExecutor is responsible for executing migrations.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Nov 14, 2013
 */
public class MigrationExecutor {
	private static final String TAG = MigrationExecutor.class.getSimpleName();

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
			if ( originVersion < DefinedMigrations.REBUILD_BELOW_VERSION ) {
				throw new MigrationException( "The version is too old", Reason.TOO_OLD, originVersion );
			}

			// Find all migraters.
			Map<Integer, Migrater> migraters = this.assemble( originVersion );

			// Filter out ones to skip.
			this.filter( migraters, originVersion, targetVersion );

			MigrationUtil util = new MigrationUtil( db, cs );

			// Apply migrations.
			for ( Migrater m : migraters.values() ) {
				m.applyMigration( util );
			}

			return true;
		} catch ( MigrationException e ) {
			return fail( e );
		} catch ( Exception e ) {
			return fail( e );
		}
	}

	private boolean fail( Throwable e ) {
		Log.e( TAG, "Error during migration.", e );
		return false;
	}

	/**
	 * Filters out any migraters that are unnecessary.
	 *
	 * @param migraters the migraters to filter.
	 * @param originVersion the version we're coming from.
	 * @param targetVersion the version we're moving to.
	 */
	private void filter( Map<Integer, Migrater> migraters, int originVersion, int targetVersion ) {
		// Let the migraters report what versions to skip.
		Set<Integer> skipVersions = Sets.newHashSet();
		for ( Migrater m : migraters.values() ) {
			Collection<Integer> skip = m.skipVersions( originVersion, targetVersion );
			if ( skip != null ) {
				skipVersions.addAll( skip );
			}
		}

		// Remove any migraters to skip.
		for ( int v : skipVersions ) {
			migraters.remove( v );
		}
	}

	/**
	 * Assembles any migraters available that are above originVersion.
	 *
	 * @param originVersion the version we're coming from.
	 * @return a list of migraters.
	 * @throws MigrationException If there was some error in finding migraters,
	 *							  this is super almost {@link AssertionError} level serious.
	 */
	private Map<Integer, Migrater> assemble( int originVersion ) throws MigrationException {
		Map<Integer, Migrater> migs = Maps.newTreeMap( Ordering.natural() );

		try {
			Class<?>[] clazzes = DefinedMigrations.get();

			for ( Class<?> _clazz : clazzes ) {
				// Skip the class if the version is not appropriate.
				int version = StringUtils.getDigitsIn( _clazz.getSimpleName() );
				if ( originVersion >= version ) {
					continue;
				}

				// Load the class, skip if not a migrater.
				Class<? extends Migrater> clazz = ReflectionUtil.asSubclass( _clazz, Migrater.class );
				if ( clazz == null ) {
					continue;
				}

				// Time to construct the migrater.
				Migrater migrater;

				try {
					Constructor<? extends Migrater> ctor = clazz.getConstructor();
					migrater = (Migrater) ctor.newInstance();
				} catch ( NoSuchMethodException e ) {
					throw new MigrationException( "No no-arg constructor found for migrater", e, version );
				} catch ( InstantiationException e ) {
					throw new MigrationException( "Could not instantiate migrater", e, version );
				} catch ( IllegalAccessException e ) {
					throw new MigrationException( "Could not access migrater constructor", e, version );
				} catch ( InvocationTargetException e ) {
					throw new MigrationException( "Migrater constructor threw exception.", e, version );
				}

				int v = migrater.versionCode();

				// Double check to ensure migrater version is appropriate.
				if ( originVersion > v ) {
					continue;
				}

				// Finally, we've got a migrater.
				migs.put( v, migrater );
			}
		} catch( RuntimeException e ) {
			throw new MigrationException( "Failure in finding migraters", e, originVersion );
		}

		return migs;
	}
}
