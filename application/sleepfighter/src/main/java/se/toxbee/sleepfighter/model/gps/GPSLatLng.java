/*
 * Copyright 2014 toxbee.se
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package se.toxbee.sleepfighter.model.gps;

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
	 * Constructs a GPSLatLng with (lat, lng) from character sequences.
	 *
	 * @param lat the latitude.
	 * @param lng the longitude.
	 */
	public GPSLatLng( CharSequence lat, CharSequence lng ) {
		this( Double.parseDouble( lat.toString() ), Double.parseDouble( lng.toString() ) );
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