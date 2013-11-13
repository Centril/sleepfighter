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

/**
 * MigrationAdapter is the base implementation of VersionMigrater.<br/>
 * It basically doesn't skip any versions.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Nov 13, 2013
 */
public abstract class MigrationAdapter implements VersionMigrater {
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