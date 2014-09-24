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

package se.toxbee.sleepfighter.persist;

import se.toxbee.ormlite.migration.MigrationExecutor;
import se.toxbee.sleepfighter.persist.migration.Version25;
import se.toxbee.sleepfighter.persist.migration.Version27;

/**
 * UpgradeExecutor defines and executes migrations for the app.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Feb, 19, 2014
 */
public class UpgradeExecutor extends MigrationExecutor {
	@Override
	protected int rebuildBelowVersion() {
		return 23;
	}

	@Override
	protected Class<?>[] definedMigrations() {
		return new Class<?>[] {
				Version25.class, Version27.class
		};
	}
}
