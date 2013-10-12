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
package se.chalmers.dat255.sleepfighter.challenge.sort;

import java.util.BitSet;
import java.util.Random;

import se.chalmers.dat255.sleepfighter.utils.collect.PrimitiveArrays;
import se.chalmers.dat255.sleepfighter.utils.math.IntMath;
import se.chalmers.dat255.sleepfighter.utils.math.RandomMath;

/**
 * ClusteredGaussianListGenerator is an implementation of NumberListGenerator
 * provides a list of numbers that have clusters with low standard deviation (stdDev),
 * and high stdDev as a whole.
 *
 * We want a list that looks like for example:
 * [131, 135, 136, 315, 317, 328, 931, 935, 941].
 * There is nothing special about the numbers in themselves,
 * other than that the whole set has a large stdDev while
 * the tripples form clusters with low stdDev internally.
 *
 * In order to accomplish this, we first randomize a list
 * with outerSize which make up the higher numbers like: x00
 * where x is a randomized digit. This value is standard-distributed from 1-9.
 *
 * After, we do for each number in the higher/outer numbers
 * a new list of innerSize which is calculated as higher[i] + randGaussian(innerRange)
 * where innerRange is some random generated range within [1, 99] if say numDigits == 3.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Oct 1, 2013
 */
public class ClusteredGaussianListGenerator implements NumberListGenerator {
	private static final double INNER_STANDARD_DEVIATIONS = 2.0;
	private static final int MAX_GAUSS_ITERATIONS = 100;

	private int numDigits = 3;

	@Override
	public void setNumDigits( int digits ) {
		if ( this.numDigits < 2 ) {
			throw new IllegalArgumentException( "n(digits) must be > 1, but " + digits + " was provided" );
		}

		this.numDigits = digits;
	}

	@Override
	public int getNumDigits() {
		return this.numDigits;
	}

	/**
	 * {@inheritDoc}<br/>
	 * Implementation note: the size must have integer factors: <code>size = a * b, where a, b ∈  Z+</code>
	 */
	@Override
	public int[] generateList( Random rng, int size ) {
		// outer = index 0, inner = index 1.
		int[] sizes = computeSizes( size );
		int[] numbers = new int[sizes[0] * sizes[1]];
		int outerMultiplier = com.google.common.math.IntMath.pow( 10, this.numDigits - 1 );
		int innerMax = outerMultiplier - 1;

		// Fill outer array first.
		int[] outer = new int[sizes[0]];
		for ( int i = 0; i < sizes[0]; ++i ) {
			outer[i] = RandomMath.nextRandomRanged( rng, 1, 9 ) * outerMultiplier;
		}

		// Fill inner array for each outer array.
		for ( int i = 0; i < sizes[0]; ++i ) {
			// Calculate bounds [min, max].
			int[] innerBounds = new int[] { RandomMath.nextRandomNon10( rng, 1, innerMax ), RandomMath.nextRandomNon10( rng, 1, innerMax ) };
			int diff = innerBounds[1] - innerBounds[0];
			if ( diff < 0 ) {
				// Wrong order, swap!
				PrimitiveArrays.swap( innerBounds, 0, 1 );
				diff = -diff;
			}
			if ( diff < sizes[1] ) {
				// Difference is too small, make sure we got room!
				innerBounds[0] = Math.max( 1, innerBounds[0] - sizes[1] );
				innerBounds[1] = innerBounds[0] + sizes[1];

				diff = innerBounds[1] - innerBounds[0];
			}

			BitSet bits = new BitSet( diff );
			boolean filledModulo10 = false;

			// Now do the filling.
			int[] inner = new int[sizes[1]];
			for ( int j = 0; j < sizes[1]; ++j ) {
				inner[j] = RandomMath.nextGaussianNon10( rng, innerBounds[0], innerBounds[1], MAX_GAUSS_ITERATIONS, INNER_STANDARD_DEVIATIONS );

				// Protect against same numbers all the time, can we do away with this loop? not O(n) but still...
				boolean hasDuplicate = false;
				for ( int k = 0; k < j; ++k ) {
					if ( inner[k] == inner[j] ) {
						hasDuplicate = true;
					}
				}

				if ( hasDuplicate ) {
					if ( !filledModulo10 ) {
						// Set all numbers that end with 0 in BitSet, we don't want them!
						// This assumes that neither innerBounds[0, 1] are modulo 10.
						for ( int l = ((innerBounds[0] / 10) + 1) * 10; l <= innerBounds[1]; l += 10 ) {
							bits.set( l - innerBounds[0] );
						}

						filledModulo10 = true;
					}

					// Find first false bit.
					// This beats the idea of randomness, but avoiding duplicates is more important!
					inner[j] = bits.nextClearBit( 0 ) + innerBounds[0];
				}

				bits.set( inner[j] - innerBounds[0] );

				numbers[sizes[1] * i + j] = outer[i] + inner[j];
			}
		}

		return numbers;
	}

	/* --------------------------------
	 * Private interface.
	 * --------------------------------
	 */

	/**
	 * Computes outer & inner sizes.
	 *
	 * @param size the size to break down.
	 * @return the sizes.
	 */
	private int[] computeSizes( int size ) {
		if ( size < 1 ) {
			throw new IllegalArgumentException( "Only integers > 0 are allowed" );
		}

		int[] sizes = IntMath.findClosestFactors( size );

		if ( sizes == null ) {
			throw new IllegalArgumentException( "There is no 2-factor solution for the size: " + size );
		}

		return sizes;
	}
}