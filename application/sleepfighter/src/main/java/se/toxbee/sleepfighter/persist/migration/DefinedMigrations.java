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
