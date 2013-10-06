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

/**
 * GPSLatLng models a (latitude, longitude) vector.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Oct 5, 2013
 */
public class GPSLatLng {
	private double lat;
	private double lng;

	/**
	 * Default constructor or: (0, 0).
	 */
	public GPSLatLng() {
		this( 0, 0 );
	}

	/**
	 * Constructs a GPSLatLng with (lat, lng).
	 *
	 * @param lat the latitude.
	 * @param lng the longitude.
	 */
	public GPSLatLng( double lat, double lng ) {
		this.lat = lat;
		this.lng = lng;
	}

	/**
	 * Copy constructor.
	 *
	 * @param rhs the GPSLatLng to copy from.
	 */
	public GPSLatLng( GPSLatLng rhs ) {
		this( rhs.lat, rhs.lng );
	}

	/**
	 * Returns the longitude component.
	 *
	 * @return the longitude.
	 */
	public double getLng() {
		return this.lng;
	}

	/**
	 * Returns the latitude component.
	 *
	 * @return the latitude.
	 */
	public double getLat() {
		return this.lat;
	}

	public int hashCode() {
		return Objects.hashCode( this.lat, this.lng );
	}

	public boolean equals( Object obj ) {
		if ( this == obj ) {
			return true;
		}

		if ( obj == null || this.getClass() != obj.getClass() ) {
			return false;
		}

		GPSLatLng rhs = (GPSLatLng) obj;
		return this.lat == rhs.lat && this.lng == rhs.lng;
	}
}