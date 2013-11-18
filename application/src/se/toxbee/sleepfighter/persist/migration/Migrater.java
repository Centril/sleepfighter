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

import java.util.Collection;

import se.toxbee.sleepfighter.utils.string.StringUtils;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.support.ConnectionSource;

/**
 * Migrater is an interface all migrations must implement.<br/>
 * Note that the migrater must have a no-argument constructor.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Nov 12, 2013
 */
public interface Migrater {
	/**
	 * MigrationAdapter is the base implementation of Migrater.<br/>
	 * It basically doesn't skip any versions.
	 *
	 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
	 * @version 1.0
	 * @since Nov 13, 2013
	 */
	public abstract class Adapter implements Migrater {
		private int version = -1;

		@Override
		public Collection<Integer> skipVersions( int originVersion, int targetVersion ) {
			return null;
		}

		/**
		 * <p>Default behavior is to deduce the version from the class name using any digits in it.</p>
		 *
		 * {@inheritDoc}
		 */
		public int versionCode() {
			if ( this.version == -1 ) {
				this.version = StringUtils.getDigitsIn( this.getClass().getSimpleName() );
			}

			return this.version;
		}
	}

	/**
	 * Returns a list of versions this migrater considers unnecessary.<br/>
	 * These migrations will not be run.
	 *
	 * @param originVersion the origin version that the client started from.
	 * @param targetVersion the target version that the client will land in.
	 * @return list of version codes to skip - or null if none.
	 */
	public Collection<Integer> skipVersions( int originVersion, int targetVersion );

	/**
	 * Applies a migration.
	 *
	 * @param cs a ConnectionSource.
	 * @param db a SQLiteDatabase.
	 * @param rawDao a Dao to run raw statements on.
	 */
	public void applyMigration( ConnectionSource cs, SQLiteDatabase db ) throws MigrationException;

	/**
	 * Returns the version-code (database) the migration is upgrading for.
	 *
	 * @return the version.
	 */
	public int versionCode();
}
