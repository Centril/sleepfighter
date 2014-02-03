/*
 * Copyright (c) 2014. See AUTHORS file.
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
 */

/*
 * Copyright 2013 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package se.toxbee.sleepfighter.model.gps;

import static java.lang.Math.PI;
import static java.lang.Math.sin;
import static java.lang.Math.tan;
import static java.lang.Math.toRadians;

/**
 * PolyUtil provides {@link #containsLocation(GPSLatLng, double[][])}.
 * Original source from: com.google.maps.android.PolyUtil,
 * adapted to our purposes.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Feb, 3, 2014
 */
public class PolyUtil {
	/**
	 * Computes whether the given point lies inside the specified geodisic polygon.
	 *
	 * The polygon is always cosidered closed,
	 * regardless of whether the last point equals the first or not.
	 * Inside is defined as not containing the South Pole -- the South Pole is always outside.
	 * The polygon is formed of great circle segments.
	 *
	 * @param point the point to check for.
	 * @param polygon the polygon, [[latitudes...], [longitudes...]].
	 * @return true if the point lies inside.
	 */
	public static boolean containsLocation( GPSLatLng point, double[][] polygon ) {
		if ( polygon == null ) {
			return false;
		}

		final int size = polygon[0].length;
		if ( size == 0 ) {
			return false;
		}

		double lat3 = toRadians( point.getLat() );
		double lng3 = toRadians( point.getLng() );

		int last = size - 1;
		double lat1 = latitude( polygon, last );
		double lng1 = longitude( polygon, last );

		int nIntersect = 0;

		for ( int i = 0; i < size; ++i ) {
			double dLng3 = wrap( lng3 - lng1, -PI, PI );
			// Special case: point equal to vertex is inside.
			if ( lat3 == lat1 && dLng3 == 0 ) {
				return true;
			}
			double lat2 = latitude( polygon, i );
			double lng2 = longitude( polygon, i );

			// Offset longitudes by -lng1.
			if ( intersects( lat1, lat2, wrap( lng2 - lng1, -PI, PI ), lat3, dLng3 ) ) {
				++nIntersect;
			}
			lat1 = lat2;
			lng1 = lng2;
		}

		return (nIntersect & 1) != 0;
	}

	private static double latitude( double[][] polygon, int i ) {
		return toRadians( polygon[0][i] );
	}

	private static double longitude( double[][] polygon, int i ) {
		return toRadians( polygon[1][i] );
	}

	/**
	 * Computes whether the vertical segment (lat3, lng3) to South Pole intersects the segment
	 * (lat1, lng1) to (lat2, lng2).
	 * Longitudes are offset by -lng1; the implicit lng1 becomes 0.
	 */
	private static boolean intersects( double lat1, double lat2, double lng2, double lat3, double lng3 ) {
		// Both ends on the same side of lng3.
		if ( (lng3 >= 0 && lng3 >= lng2) || (lng3 < 0 && lng3 < lng2) ) {
			return false;
		}
		// Point is South Pole.
		if ( lat3 <= -PI / 2 ) {
			return false;
		}
		// Any segment end is a pole.
		if ( lat1 <= -PI / 2 || lat2 <= -PI / 2 || lat1 >= PI / 2 || lat2 >= PI / 2 ) {
			return false;
		}
		if ( lng2 <= -PI ) {
			return false;
		}
		double linearLat = (lat1 * (lng2 - lng3) + lat2 * lng3) / lng2;
		// Northern hemisphere and point under lat-lng line.
		if ( lat1 >= 0 && lat2 >= 0 && lat3 < linearLat ) {
			return false;
		}
		// Southern hemisphere and point above lat-lng line.
		if ( lat1 <= 0 && lat2 <= 0 && lat3 >= linearLat ) {
			return true;
		}
		// North Pole.
		if ( lat3 >= PI / 2 ) {
			return true;
		}
		// Compare lat3 with latitude on the GC/Rhumb segment corresponding to lng3.
		// Compare through a strictly-increasing function (tan() or mercator()) as convenient.
		return tan( lat3 ) >= tanLatGC( lat1, lat2, lng2, lng3 );
	}

	/**
	 * Returns tan(latitude-at-lng3) on the great circle (lat1, lng1) to (lat2, lng2). lng1==0.
	 * See http://williams.best.vwh.net/avform.htm .
	 */
	private static double tanLatGC( double lat1, double lat2, double lng2, double lng3 ) {
		return (tan( lat1 ) * sin( lng2 - lng3 ) + tan( lat2 ) * sin( lng3 )) / sin( lng2 );
	}

	/**
	 * Wraps the given value into the inclusive-exclusive interval between min and max.
	 * @param n   The value to wrap.
	 * @param min The minimum.
	 * @param max The maximum.
	 */
	static double wrap( double n, double min, double max ) {
		return (n >= min && n < max) ? n : (mod( n - min, max - min ) + min);
	}

	/**
	 * Returns the non-negative remainder of x / m.
	 * @param x The operand.
	 * @param m The modulus.
	 */
	static double mod( double x, double m ) {
		return ((x % m) + m) % m;
	}
}
