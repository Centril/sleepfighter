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

package se.toxbee.commons.math;

import com.google.common.base.Preconditions;
import com.google.common.math.IntMath;

import java.math.RoundingMode;

/**
 * Conversion provides static methods for handling conversions involving math.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Sep 24, 2013
 */
public class Conversion {
	/**
	 * Converts a boolean array to a positive integer.
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
	 * Converts a positive integer with known base to a boolean array.
	 *
	 * @param i the positive integer.
	 * @param b the base.
	 * @return the resultant boolean array.
	 * @throws java.lang.IllegalArgumentException if i < 0, or b < 0.
	 */
	public static boolean[] intToBoolArray( final int i, int b ) {
		checkPositive( i );
		checkPositive( b );

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
	 * @throws java.lang.IllegalArgumentException if i < 0.
	 */
	public static boolean[] intToBoolArray( final int i ) {
		checkPositive( i );
		return  i == 0
			?   new boolean[] {}
			:   intToBoolArray( i, IntMath.log2( i, RoundingMode.FLOOR ) + 1 );
	}

	private static void checkPositive( final int i ) {
		if ( i < 0 ) {
			throw new IllegalArgumentException( "i (" + i + ") must be > 0" );
		}
	}
}
