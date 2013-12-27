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
package se.toxbee.sleepfighter.utils.collect;

import java.util.Arrays;
import java.util.Random;

import se.toxbee.sleepfighter.utils.reflect.ReflectionUtil;

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

	public static void shuffle( byte[] arr, Random rng ) {
		for ( int i = arr.length; i > 1; i-- ) {
			swap( arr, i - 1, rng.nextInt( i ) );
		}
	}

	public static void shuffle( short[] arr, Random rng ) {
		for ( int i = arr.length; i > 1; i-- ) {
			swap( arr, i - 1, rng.nextInt( i ) );
		}
	}

	public static void shuffle( long[] arr, Random rng ) {
		for ( int i = arr.length; i > 1; i-- ) {
			swap( arr, i - 1, rng.nextInt( i ) );
		}
	}

	public static void shuffle( float[] arr, Random rng ) {
		for ( int i = arr.length; i > 1; i-- ) {
			swap( arr, i - 1, rng.nextInt( i ) );
		}
	}

	public static void shuffle( double[] arr, Random rng ) {
		for ( int i = arr.length; i > 1; i-- ) {
			swap( arr, i - 1, rng.nextInt( i ) );
		}
	}

	public static void shuffle( boolean[] arr, Random rng ) {
		for ( int i = arr.length; i > 1; i-- ) {
			swap( arr, i - 1, rng.nextInt( i ) );
		}
	}

	public static void shuffle( char[] arr, Random rng ) {
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

	public static void reverseOrder( byte[] arr ) {
		for ( int i = 0; i < arr.length / 2; ++i ) {
			swap( arr, i, arr.length - i - 1);
		}
	}

	public static void reverseOrder( short[] arr ) {
		for ( int i = 0; i < arr.length / 2; ++i ) {
			swap( arr, i, arr.length - i - 1);
		}
	}

	public static void reverseOrder( long[] arr ) {
		for ( int i = 0; i < arr.length / 2; ++i ) {
			swap( arr, i, arr.length - i - 1);
		}
	}

	public static void reverseOrder( float[] arr ) {
		for ( int i = 0; i < arr.length / 2; ++i ) {
			swap( arr, i, arr.length - i - 1);
		}
	}

	public static void reverseOrder( double[] arr ) {
		for ( int i = 0; i < arr.length / 2; ++i ) {
			swap( arr, i, arr.length - i - 1);
		}
	}

	public static void reverseOrder( boolean[] arr ) {
		for ( int i = 0; i < arr.length / 2; ++i ) {
			swap( arr, i, arr.length - i - 1);
		}
	}

	public static void reverseOrder( char[] arr ) {
		for ( int i = 0; i < arr.length / 2; ++i ) {
			swap( arr, i, arr.length - i - 1);
		}
	}

	public static void swap( byte[] arr, int a, int b ) {
		byte t = arr[a];
		arr[a] = arr[b];
		arr[b] = t;
	}

	public static void swap( short[] arr, int a, int b ) {
		short t = arr[a];
		arr[a] = arr[b];
		arr[b] = t;
	}

	public static void swap( int[] arr, int a, int b ) {
		int t = arr[a];
		arr[a] = arr[b];
		arr[b] = t;
	}

	public static void swap( long[] arr, int a, int b ) {
		long t = arr[a];
		arr[a] = arr[b];
		arr[b] = t;
	}

	public static void swap( float[] arr, int a, int b ) {
		float t = arr[a];
		arr[a] = arr[b];
		arr[b] = t;
	}

	public static void swap( double[] arr, int a, int b ) {
		double t = arr[a];
		arr[a] = arr[b];
		arr[b] = t;
	}

	public static void swap( boolean[] arr, int a, int b ) {
		boolean t = arr[a];
		arr[a] = arr[b];
		arr[b] = t;
	}

	public static void swap( char[] arr, int a, int b ) {
		char t = arr[a];
		arr[a] = arr[b];
		arr[b] = t;
	}

	public static byte[] filled( byte v, int s ) {
		byte[] a = new byte[s];
		Arrays.fill( a, v );
		return a;
	}

	public static short[] filled( short v, int s ) {
		short[] a = new short[s];
		Arrays.fill( a, v );
		return a;
	}

	public static int[] filled( int v, int s ) {
		int[] a = new int[s];
		Arrays.fill( a, v );
		return a;
	}

	public static long[] filled( long v, int s ) {
		long[] a = new long[s];
		Arrays.fill( a, v );
		return a;
	}

	public static float[] filled( float v, int s ) {
		float[] a = new float[s];
		Arrays.fill( a, v );
		return a;
	}

	public static double[] filled( double v, int s ) {
		double[] a = new double[s];
		Arrays.fill( a, v );
		return a;
	}

	public static boolean[] filled( boolean v, int s ) {
		boolean[] a = new boolean[s];
		Arrays.fill( a, v );
		return a;
	}

	public static char[] filled( char v, int s ) {
		char[] a = new char[s];
		Arrays.fill( a, v );
		return a;
	}

	public static void shift( byte[] array, int stepSize ) {
		if ( stepSize == 0 ) {
			return;
		}

		byte[] tmp = new byte[stepSize =  shiftStepSize( array.length, stepSize )];
		shift( array, tmp, array.length, stepSize );
	}

	public static void shift( short[] array, int stepSize ) {
		if ( stepSize == 0 ) {
			return;
		}

		short[] tmp = new short[stepSize =  shiftStepSize( array.length, stepSize )];
		shift( array, tmp, array.length, stepSize );
	}

	/**
	 * Shifts the elements of an array circularly to the right, or if stepSize < 0 to the left.
	 *
	 * @param array the array who's element to shift.
	 * @param stepSize the number of steps to shift.
	 */
	public static void shift( int[] array, int stepSize ) {
		if ( stepSize == 0 ) {
			return;
		}

	    int[] tmp = new int[stepSize =  shiftStepSize( array.length, stepSize )];
		shift( array, tmp, array.length, stepSize );
	}

	public static void shift( long[] array, int stepSize ) {
		if ( stepSize == 0 ) {
			return;
		}

		long[] tmp = new long[stepSize =  shiftStepSize( array.length, stepSize )];
		shift( array, tmp, array.length, stepSize );
	}

	public static void shift( float[] array, int stepSize ) {
		if ( stepSize == 0 ) {
			return;
		}

		float[] tmp = new float[stepSize =  shiftStepSize( array.length, stepSize )];
		shift( array, tmp, array.length, stepSize );
	}

	public static void shift( double[] array, int stepSize ) {
		if ( stepSize == 0 ) {
			return;
		}

		double[] tmp = new double[stepSize =  shiftStepSize( array.length, stepSize )];
		shift( array, tmp, array.length, stepSize );
	}

	public static void shift( boolean[] array, int stepSize ) {
		if ( stepSize == 0 ) {
			return;
		}

		boolean[] tmp = new boolean[stepSize =  shiftStepSize( array.length, stepSize )];
		shift( array, tmp, array.length, stepSize );
	}

	public static void shift( char[] array, int stepSize ) {
		if ( stepSize == 0 ) {
			return;
		}


		char[] tmp = new char[stepSize =  shiftStepSize( array.length, stepSize )];
		shift( array, tmp, array.length, stepSize );
	}

	public static <T> void shift( T[] array, int stepSize ) {
		if ( stepSize == 0 ) {
			return;
		}

		if ( stepSize < 0 ) {
			stepSize = (array.length + stepSize);
		}

		stepSize %= array.length;

		T[] tmp = ReflectionUtil.genericArray( array, stepSize );
		shift( array, tmp, array.length, stepSize );
	}

	private static int shiftStepSize( int length, int stepSize ) {
		if ( stepSize < 0 ) {
			stepSize = (length + stepSize);
		}

		stepSize %= length;

		return stepSize;
	}

	private static void shift( Object array, Object tmp, int length, int stepSize ) {
		System.arraycopy( array, length - stepSize, tmp, 0, stepSize );
		System.arraycopy( array, 0, array, stepSize, length - stepSize );
		System.arraycopy( tmp, 0, array, 0, stepSize );
	}
}
