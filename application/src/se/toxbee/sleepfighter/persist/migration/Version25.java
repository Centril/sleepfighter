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
