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
