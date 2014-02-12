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

import com.google.common.collect.Lists;

import java.io.Serializable;
import java.util.List;

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

	private double[][] poly;

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
		this.poly = this.asArray( points );
	}

	/**
	 * Whether or not the polygon contains the given GPSLatLng point.
	 *
	 * @param point the point to check for.
	 * @return true if it contains the point, otherwise false.
	 */
	public boolean contains( GPSLatLng point ) {
		return PolyUtil.containsLocation( point, this.poly );
	}

	/**
	 * Returns an immutable view of the GPSLatLng points.
	 *
	 * @return the list of points that make up this polygon.
	 */
	public List<GPSLatLng> getPoints() {
		return this.asPoints( this.poly );
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
		return this.poly[0].length;
	}

	/**
	 * Returns the polygon represented as arrays of doubles.<br/>
	 * [0] contains latitudes, [1] contains longitudes.
	 *
	 * @return the array.
	 */
	private double[][] asArray( List<GPSLatLng> points ) {
		double latitudes[] = new double[points.size()];
		double longitudes[] = new double[points.size()];

		for ( int i = 0; i < points.size(); ++i ) {
			GPSLatLng point = points.get( i );

			latitudes[i] = point.getLat();
			longitudes[i] = point.getLng();
		}

		return new double[][] { latitudes, longitudes };
	}

	/**
	 * Returns the polygon represented as a list of {@link GPSLatLng} points.
	 *
	 * @return the points.
	 */
	private List<GPSLatLng> asPoints( double[][] arrays ) {
		List<GPSLatLng> list = Lists.newArrayListWithCapacity( arrays[0].length );

		for ( int i = 0; i < arrays[0].length; ++i ) {
			list.add( new GPSLatLng( arrays[0][i], arrays[1][i] )  );
		}

		return list;
	}

	public String toString() {
		StringBuilder builder = new StringBuilder( this.getEdgeCount() );
		builder.append( "GPSFilterPolygon{" );

		for ( int i = 0; i < this.getEdgeCount(); ++i ) {
			builder.append( "[" + this.poly[0][i] + ", " + this.poly[1][i] + "]" );
		}

		builder.append( "}" );

		return builder.toString();
	}
}