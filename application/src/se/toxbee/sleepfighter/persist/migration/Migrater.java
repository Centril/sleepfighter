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

import se.toxbee.sleepfighter.utils.migration.IMigration;

/**
 * {@link Migrater} is an interface all migrations must implement.<br/>
 * Note that the migrater must have a no-argument constructor.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 2.0
 * @since Nov 12, 2013
 */
public interface Migrater extends IMigration<MigrationUtil> {
	/**
	 * {@link Adapter} is the base implementation of {@link Migrater}.<br/>
	 * It basically doesn't skip any versions.
	 *
	 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
	 * @version 2.0
	 * @since Nov 13, 2013
	 */
	public abstract class Adapter extends IMigration.Adapter<MigrationUtil> implements Migrater {
	}
}
