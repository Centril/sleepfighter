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
 * ExcludePolygon defines a spherical polygon with (lat, lng) vectors to exclude.<br/>
 * Altitude (or the radius of the sphere) is not relevant.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Oct 5, 2013
 */
public class ExcludePolygon implements Serializable {
	private static final long serialVersionUID = -4728521809228656822L;

	private List<LatLng> poly;

	/**
	 * Default constructor.
	 */
	public ExcludePolygon() {
	}

	/**
	 * Constructs a ExcludePolygon from a given list of LatLng points.
	 *
	 * @param points the points.
	 */
	public ExcludePolygon( List<LatLng> points ) {
		this.setPoints( points );
	}

	/**
	 * Adds a LatLng point to polygon changing its shape.
	 *
	 * @param point the point.
	 */
	public void addPoint( LatLng point ) {
		this.poly.add( point );
	}

	/**
	 * Sets the list of LatLng points.
	 *
	 * @param points the points.
	 */
	public void setPoints( List<LatLng> points ) {
		this.poly = points;
	}

	/**
	 * Whether or not the polygon contains the given LatLng point.
	 *
	 * @param point the point to check for.
	 * @return true if it contains the point, otherwise false.
	 */
	public boolean contains( LatLng point ) {
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
			LatLng point = this.poly.get( i );

			latitudes[i] = point.getLat();
			longitudes[i] = point.getLng();
		}

		return new SphericalPolygon( latitudes, longitudes );
	}

	/**
	 * Returns an immutable view of the LatLng points.
	 *
	 * @return the list of points that make up this polygon.
	 */
	public List<LatLng> getPoints() {
		return Collections.unmodifiableList( this.poly );
	}
}