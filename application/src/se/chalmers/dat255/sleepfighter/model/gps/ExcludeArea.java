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

	@DatabaseField
	private boolean enabled;

	@DatabaseField(dataType = DataType.SERIALIZABLE)
	private ExcludePolygon poly;

	/**
	 * Default constructor
	 */
	public ExcludeArea() {
	}

	/**
	 * Constructs an ExcludeArea with name & enabled/disabled.<br/>
	 * The polygon will be null.
	 *
	 * @param name the user defined name of area.
	 * @param enabled whether or not the area is enabled.
	 */
	public ExcludeArea( String name, boolean enabled ) {
		this( name, enabled, null );
	}

	/**
	 * Constructs an ExcludeArea with name, enabled/disabled and a polygon.
	 *
	 * @param name the user defined name of area.
	 * @param enabled whether or not the area is enabled.
	 * @param poly the polygon to use.
	 */
	public ExcludeArea( String name, boolean enabled, ExcludePolygon poly ) {
		this.setName( name );
		this.setEnabled( enabled );
		this.setPolygon( poly );
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

	/**
	 * Returns whether or not the area is enabled.
	 *
	 * @return true if it is enabled.
	 */
	public boolean isEnabled() {
		return this.enabled;
	}

	/**
	 * Sets the name of the area.
	 *
	 * @param name the name.
	 */
	public void setName( String name ) {
		this.name = name;
	}

	/**
	 * Sets whether or not the area is enabled.
	 *
	 * @param enabled true if it should be enabled, false otherwise.
	 */
	public void setEnabled( boolean enabled ) {
		this.enabled = enabled;
	}

	/**
	 * Sets the ExcludePolygon of this area.
	 *
	 * @param poly the polygon.
	 */
	public void setPolygon( ExcludePolygon poly ) {
		this.poly = poly;
	}
}