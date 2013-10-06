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

import com.google.common.base.Objects;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import se.chalmers.dat255.sleepfighter.model.IdProvider;

/**
 * GPSFilterArea defines an exclusion area.<br/>
 * It is made up of a {@link GPSFilterPolygon}, name and id.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Oct 5, 2013
 */
@DatabaseTable(tableName = "gpsfilter_area")
public class GPSFilterArea implements IdProvider {
	@DatabaseField(generatedId = true)
	private int id;

	@DatabaseField
	private String name;

	@DatabaseField
	private boolean enabled;

	private GPSFilterMode mode;

	@DatabaseField(dataType = DataType.SERIALIZABLE)
	private GPSFilterPolygon poly;

	/**
	 * Default constructor
	 */
	public GPSFilterArea() {
	}

	/**
	 * Constructs an GPSFilterArea with name & enabled/disabled.<br/>
	 * The polygon will be null.
	 *
	 * @param name the user defined name of area.
	 * @param enabled whether or not the area is enabled.
	 */
	public GPSFilterArea( String name, boolean enabled, GPSFilterMode mode ) {
		this( name, enabled, mode, null );
	}

	/**
	 * Constructs an GPSFilterArea with name, enabled/disabled and a polygon.
	 *
	 * @param name the user defined name of area.
	 * @param enabled whether or not the area is enabled.
	 * @param poly the polygon to use.
	 */
	public GPSFilterArea( String name, boolean enabled, GPSFilterMode mode, GPSFilterPolygon poly ) {
		this.setName( name );
		this.setEnabled( enabled );
		this.setMode( mode );
		this.setPolygon( poly );
	}

	/**
	 * Returns Whether or not the polygon contains the given GPSLatLng point.<br/>
	 * {@link #getPolygon()} may not return null before a call to {@link #contains(GPSGPSLatLng)}.
	 *
	 * @see GPSFilterArea#contains(GPSGPSLatLng)
	 * @param pos the point to check for.
	 * @return true if it contains the point, otherwise false.
	 */
	public boolean contains( GPSLatLng pos ) {
		return this.getPolygon().contains( pos );
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

	public GPSFilterMode getMode() {
		return this.mode;
	}

	/**
	 * Returns the polygon that defines this area.
	 *
	 * @return the polygon, or null if not in memory yet.
	 */
	public GPSFilterPolygon getPolygon() {
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
		if ( Objects.equal(  this.name, name ) ) {
			return;
		}

		this.name = name;
	}

	/**
	 * Sets whether or not the area is enabled.
	 *
	 * @param enabled true if it should be enabled, false otherwise.
	 */
	public void setEnabled( boolean enabled ) {
		if ( this.enabled == enabled ) {
			return;
		}

		this.enabled = enabled;
	}

	/**
	 * Changes the GPSMode the area operates with.
	 *
	 * @param mode
	 * @return true if the mode was change, or false.
	 */
	public boolean setMode( GPSFilterMode mode ) {
		if ( this.mode == mode ) {
			return false;
		}

		this.mode = mode;
		return true;
	}

	/**
	 * Sets the GPSFilterPolygon of this area.
	 *
	 * @param poly the polygon.
	 */
	public void setPolygon( GPSFilterPolygon poly ) {
		if ( Objects.equal( this.poly, poly ) ) {
			return;
		}

		this.poly = poly;
	}
}