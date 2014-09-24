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

package se.toxbee.commons.migration;

import java.util.Collection;

import se.toxbee.commons.string.StringUtils;

/**
 * {@link IMigration} is an abstract interface that should be extended.<br/>
 * Note that the migrater must have a no-argument constructor.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Dec 22, 2013
 */
public interface IMigration<U> {
	/**
	 * MigrationAdapter is the base implementation of Migrater.<br/>
	 * It basically doesn't skip any versions.
	 *
	 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
	 * @version 1.0
	 * @since Dec 22, 2013
	 */
	public abstract class Adapter<U> implements IMigration<U> {
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
	 * Applies a migration.
	 *
	 * @param util the utility object to perform work with.
	 */
	public void applyMigration( U util ) throws IMigrationException;

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
	 * Returns the version-code the migration is upgrading for.
	 *
	 * @return the version.
	 */
	public int versionCode();
}
