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

package se.toxbee.sleepfighter.utils.math;

/**
 * IntMath provides integer math utilities.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Sep 30, 2013
 */
public class IntMath {
	/**
	 * Finds the two closest factor that produce val, or null if there are no such factors.<br/>
	 *
	 * @param val the integer value.
	 * @return the resulting factors as <code>[j, k]</code>, where <code>j <= k</code>
	 */
	public static int[] findClosestFactors( int val ) {
		int i = (int) Math.ceil( Math.sqrt( val ) );
		for ( int j = i;  j >= 0; --j ) {
			for ( int k = i; i <= val; ++i ) {
				if ( j * k == val ) {
					return new int[] { j, k };
				}
			}
		}

		return null;
	}
}
