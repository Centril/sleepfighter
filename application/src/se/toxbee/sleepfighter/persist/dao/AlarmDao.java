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
package se.toxbee.sleepfighter.persist.dao;

import java.sql.SQLException;

import se.toxbee.sleepfighter.model.Alarm;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

/**
 * AlarmDao is the DAO for {@link Alarm}.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Dec 13, 2013
 */
public class AlarmDao extends BaseDaoImpl<Alarm, Integer> {
	@Override
	public int create( Alarm data ) throws SQLException {
		int affected = super.create( data );
		if ( affected == 1 ) {
			data.setOrder();
		}
		return affected;
	}

	// this constructor must be defined
	public AlarmDao( ConnectionSource connectionSource ) throws SQLException {
		super( connectionSource, Alarm.class );
	}
}
