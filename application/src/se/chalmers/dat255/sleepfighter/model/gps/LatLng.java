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

/**
 * LatLng models a (latitude, longitude) vector.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Oct 5, 2013
 */
public class LatLng {
	private double lat;
	private double lng;

	/**
	 * Default constructor or: (0, 0).
	 */
	public LatLng() {
		this( 0, 0 );
	}

	/**
	 * Constructs a LatLng with (lat, lng).
	 *
	 * @param lat the latitude.
	 * @param lng the longitude.
	 */
	public LatLng( double lat, double lng ) {
		this.lat = lat;
		this.lng = lng;
	}

	/**
	 * Copy constructor.
	 *
	 * @param rhs the LatLng to copy from.
	 */
	public LatLng( LatLng rhs ) {
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
}