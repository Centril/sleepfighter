package se.chalmers.dat255.sleepfighter.utils.math;

import java.util.Random;

/**
 * RandomMath provides utility methods for dealing with random numbers.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Sep 30, 2013
 */
public class RandomMath {
	/**
	 * Calculates the first random number in range [min, max] that is not modulo 10.
	 *
	 * @param rng the random number generator.
	 * @param min the minimum number, inclusive.
	 * @param max the maximum number, inclusive.
	 * @return the randomly generated number.
	 */
	public static int nextRandomNon10( Random rng, int min, int max )  {
		int random;
		do {
			random = nextRandomRanged( rng, min, max );
		} while ( random % 10 == 0 );

		return random;
	}

	/**
	 * Calculates random number in range [min, max]
	 *
	 * @param rng the random number generator.
	 * @param min the minimum number, inclusive.
	 * @param max the maximum number, inclusive.
	 * @return the randomly generated number.
	 */
	public static int nextRandomRanged( Random rng, int min, int max ) {
		// Maybe use nextGaussian here instead?
		return min + (int) (rng.nextDouble() * ((max - min) + 1));
	}
}