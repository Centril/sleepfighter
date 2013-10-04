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
package se.chalmers.dat255.sleepfighter.utils.math;

import java.math.RoundingMode;

import com.google.common.base.Preconditions;
import com.google.common.math.IntMath;

/**
 * Conversion provides static methods for handling conversions involving math.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Sep 24, 2013
 */
public class Conversion {
	/**
	 * Construction forbidden.
	 */
	private Conversion() {
	}

	/**
	 * Converts a boolean array to an integer.
	 *
	 * @param b the boolean array.
	 * @return the resultant integer.
	 */
	public static int boolArrayToInt( boolean[] b ) {
		Preconditions.checkNotNull( b );

		int n = 0;
		for ( int i = b.length - 1; i >= 0; --i ) {
		    n = (n << 1) + (b[i] ? 1 : 0);
		}
		return n;
	}

	/**
	 * Converts an integer with known base to a boolean array.
	 *
	 * @param i the integer.
	 * @param b the base.
	 * @return the resultant boolean array.
	 */
	public static boolean[] intToBoolArray( final int i, int b ) {
	    boolean[] bits = new boolean[b];
	    for (int k = --b; k >= 0; --k) {
	        bits[k] = (i & (1 << k)) != 0;
	    }

		return bits;
	}

	/**
	 * Converts an integer with unknown base to a boolean array.
	 *
	 * @param i the integer.
	 * @return the resultant boolean array.
	 */
	public static boolean[] intToBoolArray( final int i ) {
		return intToBoolArray( i, IntMath.log2( i, RoundingMode.FLOOR ) );
	}
}
