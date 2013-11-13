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
package se.toxbee.sleepfighter.persist.upgrades;

import java.sql.SQLException;

import se.toxbee.sleepfighter.persist.PersistenceExceptionDao;
import se.toxbee.sleepfighter.persist.migration.MigrationAdapter;
import se.toxbee.sleepfighter.persist.migration.MigrationException;
import se.toxbee.sleepfighter.persist.migration.MigrationUtil;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.support.ConnectionSource;

/**
 * Migration to version 24.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Nov 13, 2013
 */
public class Version24 extends MigrationAdapter {
	@Override
	public void applyMigration( ConnectionSource cs, SQLiteDatabase db, PersistenceExceptionDao<?, Integer> rawDao ) throws MigrationException {
		try {
			MigrationUtil.addColumn( db, "alarm", "time", "INTEGER DEFAULT 0" );
			MigrationUtil.update( db, "alarm", "time = (alarm.hour << 12) | (minute << 6) | second", null );
			MigrationUtil.dropColumns( db, "alarm", new String[] { "hour", "minute", "second" } );
		} catch ( SQLException e ) {
			throw new MigrationException( "Migration v24 failed.", e, this );
		}
	}
}
