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

import se.toxbee.sleepfighter.persist.prefs.PersistPreference;

import com.j256.ormlite.table.TableUtils;

/**
 * Migration to version 28.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Dec 15, 2013
 */
public class Version28 extends Migrater.Adapter {

	@Override
	public void applyMigration( MigrationUtil util ) throws MigrationException {
		try {
			TableUtils.createTable( util.getConnectionSource(), PersistPreference.class );
		} catch ( SQLException e ) {
			throw new MigrationException( "Migration v28 failed.", e, this );
		}
	}
}
