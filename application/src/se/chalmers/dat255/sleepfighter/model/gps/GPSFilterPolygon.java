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

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import nsidc.spheres.Point;
import nsidc.spheres.SphericalPolygon;

/**
 * GPSFilterPolygon defines a spherical polygon with (lat, lng) vectors to filter.<br/>
 * Altitude (or the radius of the sphere) is not relevant.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Oct 5, 2013
 */
public class GPSFilterPolygon implements Serializable {
	private static final long serialVersionUID = -4728521809228656822L;

	private List<GPSLatLng> poly;

	/**
	 * Default constructor.
	 */
	public GPSFilterPolygon() {
	}

	/**
	 * Constructs a GPSFilterPolygon from a given list of GPSLatLng points.
	 *
	 * @param points the points.
	 */
	public GPSFilterPolygon( List<GPSLatLng> points ) {
		this.setPoints( points );
	}

	/**
	 * Sets the list of GPSLatLng points.
	 *
	 * @param points the points.
	 */
	public void setPoints( List<GPSLatLng> points ) {
		this.poly = points;
	}

	/**
	 * Whether or not the polygon contains the given GPSLatLng point.
	 *
	 * @param point the point to check for.
	 * @return true if it contains the point, otherwise false.
	 */
	public boolean contains( GPSLatLng point ) {
		SphericalPolygon spherical = this.convertToSpherical();
		Point p = new Point( point.getLat(), point.getLng() );
		return spherical.contains( p );
	}

	/**
	 * Converts polygon to a SphericalPolygon for math operations.
	 * 
	 * @return the SphericalPolygon.
	 */
	private SphericalPolygon convertToSpherical() {
		// First convert to a SphericalPolygon.
		double latitudes[] = new double[this.poly.size()];
		double longitudes[] = new double[this.poly.size()];

		for ( int i = 0; i < this.poly.size(); ++i ) {
			GPSLatLng point = this.poly.get( i );

			latitudes[i] = point.getLat();
			longitudes[i] = point.getLng();
		}

		return new SphericalPolygon( latitudes, longitudes );
	}

	/**
	 * Returns an immutable view of the GPSLatLng points.
	 *
	 * @return the list of points that make up this polygon.
	 */
	public List<GPSLatLng> getPoints() {
		return Collections.unmodifiableList( this.poly );
	}

	/**
	 * Returns whether or not this is a valid polygon.<br/>
	 * It is when {@link #getEdgeCount()} >= 3.
	 *
	 * @return true if it's a valid polygon.
	 */
	public boolean isValid() {
		return this.getEdgeCount() >= 3;
	}

	/**
	 * Returns the number of edges in polygon.
	 *
	 * @return the edge count.
	 */
	public int getEdgeCount() {
		return this.poly.size();
	}
}