package se.chalmers.dat255.sleepfighter.challenge.sort;

import java.util.Random;

import se.chalmers.dat255.sleepfighter.utils.math.IntMath;
import static se.chalmers.dat255.sleepfighter.utils.math.StatisticalMath.*;
import static se.chalmers.dat255.sleepfighter.utils.math.RandomMath.*;

/**
 * DeviatedListGenerator is an implementation of NumberListGenerator
 * using standard deviation and coefficient of variation for good spread.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Sep 30, 2013
 */
public class DeviatedListGenerator implements NumberListGenerator {
	/* --------------------------------
	 * Configuration variables.
	 * --------------------------------
	 */
	private int min = 101;
	private int max = 999;
	private int innerMin = 1;
	private int innerMax = 99;

	private double varianceOuterMin = 33.0; // The numbers here were just picked "at random".
	private double varianceInnerMax = 33.0;

	/* --------------------------------
	 * Public interface.
	 * --------------------------------
	 */

	/**
	 * {@inheritDoc}<br/>
	 * If not set, sensible default values [101, 999] are used.
	 */
	public void setRange( int min, int max ) {
		this.min = min;
		this.max = max;
	}

	/**
	 * Sets the variance coefficient for outer (big parts of numbers),
	 * and inner (smaller digits of numbers).<br/>
	 * If not set, sensible default values [TODO, TODO] are used.
	 *
	 * @param outerMin the minimum variance in the calculation of bigger numbers.
	 * @param innerMax the maximum variance in the calculation of smaller numbers.
	 */
	public void setVariance( int outerMin, int innerMax ) {
		this.varianceOuterMin = outerMin;
		this.varianceInnerMax = innerMax;
	}

	/**
	 * Sets the range of numbers for the computation of<br/>
	 * small numbers to add to the big ones when generating.
	 *
	 * @param min minimum number, inclusive.
	 * @param max maximum number, exclusive.
	 */
	public void setInnerRange( int min, int max ) {
		this.innerMax = min;
		this.innerMax = max;
	}

	/**
	 * {@inheritDoc}<br/>
	 * Implementation note: the size must have integer factors: <code>size = a * b, where a, b ∈  Z+</code>
	 */
	@Override
	public int[] generateList( Random rng, int size ) {
		int[] sizes = computeSizes( size );

		int innerSize = sizes[0];
		int outerSize = sizes[1];

		int[] numbers = new int[outerSize * innerSize];

		// Fill big array first.
		int[] big = new int[outerSize];
		while ( computeVariance( big ) < varianceOuterMin ) {
			for ( int i = 0; i < outerSize; ++i ) {
				big[i] = nextRandomNon10( rng, min, max );
			}
		}

		// Fill a small array for each big array.
		for ( int i = 0; i < outerSize; ++i ) {
			int[] small = new int[innerSize];
			while ( computeVariance( small ) > varianceInnerMax ) {
				for ( int j = 0; i < innerSize; ++i ) {
					small[i] = big[i] + nextRandomNon10( rng, innerMin, innerMax );
					numbers[innerSize * i + j] = small[i];
				}
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