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

import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import se.toxbee.commons.reflect.ROJava6Exception;
import se.toxbee.commons.reflect.ReflectionUtil;
import se.toxbee.commons.string.StringUtils;

/**
 * {@link IMigrationExecutor} is an abstraction of executing migrations.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Dec 22, 2013
 */
public abstract class IMigrationExecutor<U, T extends IMigration<U>> {
	/**
	 * Returns the {@link Class} of the {@link IMigration} subinterface.
	 *
	 * @return the clazz.
	 */
	protected abstract Class<T> clazz();

	/**
	 * Returns the {@link Class}es of all defined migrations.
	 *
	 * @return an array of the migrations.
	 */
	protected abstract Class<?>[] definedMigrations();

	/**
	 * Called when execution of migration fails.
	 *
	 * @param e the Throwable that caused failure.
	 * @return false.
	 */
	protected boolean fail( Throwable e ) {
		return false;
	}

	/**
	 * Applies all migrations between originVersion & targetVersion.
	 *
	 * @param util the util object to pass to {@link IMigration#applyMigration(Object)}
	 * @param originVersion the version we're coming from.
	 * @param targetVersion the version we're moving to.
	 * @throws IMigrationException If there was some reflection-error or if a migration failed.
	 */
	protected void apply( U util, int originVersion, int targetVersion ) throws IMigrationException {
		// Find & apply all migraters.
		for ( T m : this.assemble( originVersion, targetVersion ) ) {
			m.applyMigration( util );
		}
	}

	/**
	 * Assembles any migraters available that are applicable.
	 *
	 * @param originVersion the version we're coming from.
	 * @param targetVersion the version we're moving to.
	 * @return collection of all migraters, in ascending version order.
	 * @throws IMigrationException If there was some reflection-error.
	 */
	protected Collection<T> assemble( int originVersion, int targetVersion ) throws IMigrationException {
		// Find all migraters.
		Map<Integer, T> migraters = this.makeMigraters( originVersion, targetVersion );

		// Filter out ones to skip.
		this.filter( migraters, originVersion, targetVersion );

		return migraters.values();
	}

	/**
	 * Filters out any {@link IMigration} that are unnecessary.
	 *
	 * @param migraters the migraters to filter.
	 * @param originVersion the version we're coming from.
	 * @param targetVersion the version we're moving to.
	 */
	protected void filter( Map<Integer, T> migraters, int originVersion, int targetVersion ) {
		// Let the migraters report what versions to skip.
		Set<Integer> skipVersions = Sets.newHashSet();
		for ( T m : migraters.values() ) {
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
	 * @return a map of version -> migraters.
	 * @throws IMigrationException If there was some reflection-error.
	 */
	protected Map<Integer, T> makeMigraters( int originVersion, int targetVersion ) throws IMigrationException {
		Map<Integer, T> migs = Maps.newTreeMap( Ordering.natural() );

		Class<?>[] clazzes = this.definedMigrations();
		for ( Class<?> _clazz : clazzes ) {
			// Skip the class if the version is not appropriate.
			int version = StringUtils.getDigitsIn( _clazz.getSimpleName() );
			if ( originVersion >= version ) {
				continue;
			}

			// Load the class, skip if not a migrater.
			Class<? extends T> clazz = ReflectionUtil.asSubclass( _clazz, this.clazz() );
			if ( clazz == null ) {
				continue;
			}

			// Time to construct the migrater.
			T migrater;
			try {
				migrater = ReflectionUtil.newInstance( clazz );
			} catch ( ROJava6Exception e ) {
				throw new IMigrationException( "Couldn't construct migrater", e, version );
			}

			int v = migrater.versionCode();

			// Double check to ensure migrater version is appropriate.
			if ( originVersion >= v ) {
				continue;
			}

			// Finally, we've got a migrater.
			migs.put( v, migrater );
		}

		return migs;
	}
}