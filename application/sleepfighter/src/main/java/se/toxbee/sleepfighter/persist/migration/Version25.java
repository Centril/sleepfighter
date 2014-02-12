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

/**
 * Migration to version 25.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Nov 15, 2013
 */
public class Version25 extends Migrater.Adapter {
	@Override
	public void applyMigration( MigrationUtil util ) throws MigrationException {
		try {
			util.table( "alarm" )
				.addColumn( "time", "INTEGER DEFAULT 0" )
				.addColumn( "countdownTime", "BIGINT" )
				.update( "time = (hour << 12) | (minute << 6) | second", null )
				.dropColumns( new String[] { "hour", "minute", "second" } );
		} catch ( SQLException e ) {
			MigrationException.fail( e, this );
		}
	}
}
