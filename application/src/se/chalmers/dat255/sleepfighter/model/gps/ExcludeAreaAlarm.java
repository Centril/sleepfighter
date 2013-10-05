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
package se.chalmers.dat255.sleepfighter.model.gps;

import se.chalmers.dat255.sleepfighter.model.Alarm;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * ExcludeAreaAlarm exists for the sole purpose of having a junction-table<br/>
 * in database between Alarm & ExcludeArea forming a N:M relationship.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Oct 5, 2013
 */
@DatabaseTable(tableName = "exclude_area_alarm")
public class ExcludeAreaAlarm {
	public static final String ALARM_ID_FIELD = "alarm_id";
	public static final String EXCLUDE_AREA_ID_FIELD = "exclude_area_id";

	@DatabaseField(generatedId = true)
	private int id;

	@DatabaseField(foreign = true, columnName = ALARM_ID_FIELD)
	private Alarm alarm;

	@DatabaseField(foreign = true, columnName = EXCLUDE_AREA_ID_FIELD)
	private ExcludeArea area;

	/**
	 * Default constructor.
	 */
	public ExcludeAreaAlarm() {
	}

	/**
	 * Constructs a relationship between alarm & area.
	 *
	 * @param alarm the alarm.
	 * @param area the area.
	 */
	public ExcludeAreaAlarm( Alarm alarm, ExcludeArea area ) {
		this.alarm = alarm;
		this.area = area;
	}

	/**
	 * Returns the alarm of the relationship.
	 *
	 * @return the alarm.
	 */
	public Alarm getAlarm() {
		return this.alarm;
	}

	/**
	 * Returns the ExcludeArea of the relationship.
	 *
	 * @return the area.
	 */
	public ExcludeArea getArea() {
		return this.area;
	}
}