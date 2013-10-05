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

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import se.chalmers.dat255.sleepfighter.model.IdProvider;

/**
 * ExcludeArea defines an exclusion area.<br/>
 * It is made up of a {@link ExcludePolygon}, name and id.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Oct 5, 2013
 */
@DatabaseTable(tableName = "exclude_area")
public class ExcludeArea implements IdProvider {
	@DatabaseField(generatedId = true)
	private int id;

	@DatabaseField
	private String name;

	@DatabaseField(dataType = DataType.SERIALIZABLE)
	private ExcludePolygon poly;

	/**
	 * Default constructor
	 */
	public ExcludeArea() {
	}


	@Override
	public int getId() {
		return this.id;
	}

	/**
	 * Returns the user defined name of this area.
	 *
	 * @return the name.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Returns the polygon that defines this area.
	 *
	 * @return the polygon, or null if not in memory yet.
	 */
	public ExcludePolygon getPolygon() {
		return this.poly;
	}
}