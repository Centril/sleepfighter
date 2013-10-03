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
package se.chalmers.dat255.sleepfighter.utils.collect;

import java.util.Random;

/**
 * PrimitiveArrays provides various operations for primitive arrays.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Sep 30, 2013
 */
public class PrimitiveArrays {
	/**
	 * Performs a Fisher-Yates shuffle.
	 * see http://stackoverflow.com/questions/2249520/javas-collections-shuffle-is-doing-what
	 *
	 * @param arr the array.
	 * @param rng the RNG (random number generator).
	 */
	public static void shuffle( int[] arr, Random rng ) {
		for ( int i = arr.length; i > 1; i-- ) {
			swap( arr, i - 1, rng.nextInt( i ) );
		}
	}

	/**
	 * Reverses the order of an integer array.
	 *
	 * @param arr the array.
	 */
	public static void reverseOrder( int[] arr ) {
		for ( int i = 0; i < arr.length / 2; ++i ) {
			swap( arr, i, arr.length - i - 1);
		}
	}

	/**
	 * Swaps indexes a & b in arr.
	 *
	 * @param arr the integer array.
	 * @param a index a.
	 * @param b index b.
	 */
	public static void swap( int[] arr, int a, int b ) {
		int t = arr[a];
		arr[a] = arr[b];
		arr[b] = t;
	}
}
