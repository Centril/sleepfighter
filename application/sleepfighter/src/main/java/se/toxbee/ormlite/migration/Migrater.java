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

package se.toxbee.ormlite.migration;

import se.toxbee.commons.migration.IMigration;

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
	 * Adapter is the base implementation of {@link Migrater}.<br/>
	 * It basically doesn't skip any versions.
	 *
	 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
	 * @version 2.0
	 * @since Nov 13, 2013
	 */
	public abstract class Adapter extends IMigration.Adapter<MigrationUtil> implements Migrater {
	}
}
