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

import java.sql.SQLException;

import se.toxbee.ormlite.migration.Migrater.Adapter;
import se.toxbee.ormlite.migration.MigrationException;
import se.toxbee.ormlite.migration.MigrationUtil;

/**
 * Migration to version 27.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Dec 13, 2013
 */
public class Version27 extends Adapter {
	@Override
	public void applyMigration( MigrationUtil util ) throws MigrationException {
		try {
			util.table( "alarm" )
				.addColumn( "order", "INTEGER DEFAULT 0" )
				.update( "`order` = `id`", null );
		} catch ( SQLException e ) {
			MigrationException.fail( e, this );
		}
	}
}
