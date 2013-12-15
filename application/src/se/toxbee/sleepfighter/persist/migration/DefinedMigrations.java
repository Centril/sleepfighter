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

/**
 * DefinedMigrations provides all defined migrations.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Nov 14, 2013
 */
public class DefinedMigrations {
	// Any version below this will cause the database to be rebuilt.
	public static final int REBUILD_BELOW_VERSION = 23;

	/**
	 * Returns the defined migrations, avoid class loading before we don't need migration.
	 *
	 * @return the available migrations.
	 */
	public static final Class<?>[] get() {
		// reflections was thought of, but is error prone.
		return new Class<?>[] {
			Version25.class, Version27.class
		};
	}
}
