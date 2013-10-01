package se.chalmers.dat255.sleepfighter.utils.math;

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