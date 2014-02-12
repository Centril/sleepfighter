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
		int r;
		while ( (r = nextRandomRanged( rng, min, max )) % 10 == 0 );
		return r;
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
		return min + (int) (rng.nextDouble() * ((max - min) + 1));
	}

	/**
	 * Calculates random number in range [min, max], but the value exclude is never 
	 * returned by this function 
	 *
	 * @param rng the random number generator.
	 * @param min the minimum number, inclusive.
	 * @param max the maximum number, inclusive.
	 * @param exclude this value will never be returned by this function
	 * @return the randomly generated number.
	 */
	public static int nextRandomRanged( Random rng, int min, int max, int exclude ) {
		int r;
		while( (r = nextRandomRanged( rng, min, max )) == exclude);
		return r;
	}

	/**
	 * Calculates random number in range [min, max], but 0 is never 
	 * returned by this function if noZeros = true.
	 *
	 * @param rng the random number generator.
	 * @param min the minimum number, inclusive.
	 * @param max the maximum number, inclusive.
	 * @param noZeros should 0 be allowed as resulting value?
	 * @return the randomly generated number.
	 */
	public static int nextRandomRanged(Random rng, final int MIN_INT, final int MAX_INT, boolean noZeros) {
		return	noZeros
			?	nextRandomRanged(rng, MIN_INT, MAX_INT, 0)
			:	nextRandomRanged(rng, MIN_INT, MAX_INT);
	}

	/**
	 * <p>Returns a random generated gaussian / normal distributed<br/>
	 * number with given mean and standard deviation.</p>
	 *
	 * <p>99.7% of all numbers lie within the 3rd standard deviation, see:<br/>
	 * http://en.wikipedia.org/wiki/68-95-99.7_rule</p>
	 *
	 * @see http://en.wikipedia.org/wiki/68-95-99.7_rule
	 * @param rng the random number generator.
	 * @param mean the mean to distribute around.
	 * @param stdDev the standard deviation, which in layman terms can be put as how much the curve is stretched.
	 * @return the randomly generated number.
	 */
	public static double nextGaussian( Random rng, double mean, double stdDev ) {
		return mean + rng.nextGaussian() * stdDev;
	}

	/**
	 * <p>Returns a random generated pseudo-gaussian / normal<br/>
	 * distributed number in a given range [min, max].<br/>
	 * It is not gaussian in the sense that it has a range<br/>
	 * whereas gaussian distributions are unbounded.</p>
	 *
	 * @param rng the random number generator.
	 * @param min the minimum value, inclusive.
	 * @param max the maximum value, inclusive.
	 * @param maxIterations the hard limit of iterations to run before ending loop, used as a safety measure.
	 * @param deviations how many standard deviations to use.
	 * @return the random number.
	 */
	public static int nextGaussianRanged( Random rng, int min, int max, int maxIterations, double deviations ) {
		// See http://stackoverflow.com/questions/1303368/how-to-generate-normally-distributed-random-from-an-integer-range?rq=1
        int r = 0;
        for ( int i = 0; (i < maxIterations) && ((r = (int) nextGaussian( rng, min + (max - min) / 2.0, (max - min) / 2.0 / deviations )) > max || r < min); ++i );
        return r;
	}

	/**
	 * Like {@link #nextGaussianRanged(Random, int, int, int)} but excluding numbers modulo 10.
	 *
	 * @param rng the random number generator.
	 * @param min the minimum value, inclusive.
	 * @param max the maximum value, inclusive.
	 * @param maxIterations the hard limit of iterations to run before ending loop, used as a safety measure.
	 * @param deviations how many standard deviations to use.
	 * @return the random number.
	 */
	public static int nextGaussianNon10( Random rng, int min, int max, int maxIterations, double deviations ) {
		int r;
		while ( (r = nextGaussianRanged( rng, min, max, maxIterations, deviations )) % 10 == 0 );
		return r;
	}

	/**
	 * Returns a random enum value.
	 * 
	 * @param rng the RNG to use.
	 * @param clazz the Class object of enum.
	 * @return the value of enum.
	 */
	public static <T extends Enum<?>> T randomEnum( Random rng, Class<T> clazz ) {
		int x = rng.nextInt( clazz.getEnumConstants().length );
		return clazz.getEnumConstants()[x];
	}
}
