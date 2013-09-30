package se.chalmers.dat255.sleepfighter.challenge.sort;

import java.util.Arrays;
import java.util.Random;

/**
 * SortModel is the model for SortChallenge.<br/>
 * <strong>NOTE:</strong> the implementation is <strong>NOT</strong> thread safe.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Sep 30, 2013
 */
public class SortModel {
	/**
	 * Enumeration of sorting orders<br/>
	 * There are two, {@link #ASCENDING} and {@link #DESCENDING}.
	 *
	 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
	 * @version 1.0
	 * @since Sep 30, 2013
	 */
	public enum Order {
		ASCENDING, DESCENDING;

		/**
		 * Returns an Order given a boolean b.
		 *
		 * @param b the boolean value.
		 * @return the order it corresponds to.
		 */
		public static Order fromBool( boolean b ) {
			return b ? ASCENDING : DESCENDING;
		}

		/**
		 * Returns a boolean given an Order.
		 *
		 * @param order the order value.
		 * @return the boolean it corresponds to.
		 */
		public static boolean toBool( Order order ) {
			return order == ASCENDING;
		}
	}

	/* --------------------------------
	 * Private fields.
	 * --------------------------------
	 */
	private int[] generatedList;

	private int stepIndex = 0;

	private Order sortOrder;

	/* --------------------------------
	 * Public interface.
	 * --------------------------------
	 */

	/**
	 * Sets the size of the generated numbers list.<br/>
	 * The size must be > 0 and must be represented as <code>size = a * b, where a, b ∈  Z+</code>
	 *
	 * @param size
	 */
	public void setSize( int size ) {
		if ( size < 1 ) {
			throw new IllegalArgumentException( "Only integers > 0 are allowed" );
		}

		int[] sizes = findClosestFactors( size );

		if ( sizes == null ) {
			throw new IllegalArgumentException( "There is no 2-factor solution for the size: " + size );
		}

		this.sizes = sizes;
	}

	/**
	 * Sets the range of numbers possible to generate.<br/>
	 * If not set, sensible default values [101, 999] are used.
	 *
	 * @param min the minimum value, inclusive.
	 * @param max the maximum value, inclusive.
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
	 * <p>Generates the list of numbers, sets sort order.<br/>
	 * This resets the model.</p>
	 *
	 * <p>Before generating the list, you must call {@link #setSize(int)}.</p>
	 */
	public void generateList() {
		int[] numbers = this.computeList();

		this.stepIndex = 0;

		// Finally, sort list.
		this.sortOrder = Order.fromBool( new Random().nextBoolean() );
		Arrays.sort( numbers );
		if ( this.sortOrder == Order.DESCENDING ) {
			reverseOrder( numbers );
		}

		this.generatedList = numbers;
	}

	/**
	 * Returns a shuffled representation of the generated list.
	 *
	 * @return the shuffled list.
	 */
	public int[] getShuffledList() {
		int[] list = this.generatedList.clone();
		shuffle( list, new Random() );
		return list;
	}

	public void advanceStep( int number ) {
		if ( !this.isNextNumber( number ) ) {
			throw new IllegalArgumentException( "Can not advance to next step, given number: " + number + ", is not correct." );
		}

		++this.stepIndex;
	}

	/**
	 * Returns true if the given number is the next step in list.
	 *
	 * @param number the number.
	 * @return true if it's the next step.
	 */
	public boolean isNextNumber( int number ) {
		return this.generatedList[this.stepIndex] == number;
	}

	/**
	 * Returns true if there is no next step, all have been passed.
	 *
	 * @return true if we're finished.
	 */
	public boolean isFinished() {
		return this.stepIndex == this.generatedList.length;
	}

	/* --------------------------------
	 * Configuration variables.
	 * --------------------------------
	 */
	private static final int INNER_MIN = 1;
	private static final int INNER_MAX = 99;

	// Config variables.
	private int min = 101;
	private int max = 999;
	private double varianceOuterMin = 100.0; // The numbers here were just picked "at random".
	private double varianceInnerMax = 33.0;

	private int[] sizes;

	/* --------------------------------
	 * Private implementation details.
	 * --------------------------------
	 */

	/**
	 * Here goes the actual generation of the list.
	 *
	 * @return the generated list.
	 */
	private int[] computeList() {
		int innerSize = sizes[0];
		int outerSize = sizes[1];

		// java.util.Random is maybe not optimal, but for now...
		Random rng = new Random();

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
					small[i] = big[i] + nextRandomNon10( rng, INNER_MIN, INNER_MAX );
					numbers[innerSize * i + j] = small[i];
				}
			}
		}

		return numbers;
	}

	/**
	 * Reverses the order of an integer array.
	 *
	 * @param arr the array.
	 */
	private void reverseOrder( int[] arr ) {
		for ( int i = 0; i < arr.length / 2; ++i ) {
			swap( arr, i, arr.length - i - 1);
		}
	}

	/**
	 * Finds the two closest factor that produce val, or null if there are no such factors.<br/>
	 *
	 * @param val the integer value.
	 * @return the resulting factors as <code>[j, k]</code>, where <code>j <= k</code>
	 */
	private int[] findClosestFactors( int val ) {
		int i = (int) Math.ceil( Math.sqrt( val ) );
		for ( int j = i;  j >= 0; --j ) {
			for ( int k = i; i <= val; ++i ) {
				if ( j * k == val ) {
					return new int[] { j * k };
				}
			}
		}

		return null;
	}

	/**
	 * Calculates the first random number in range [min, max] that is not modulo 10.
	 *
	 * @param rng the random number generator.
	 * @param min the minimum number, inclusive.
	 * @param max the maximum number, inclusive.
	 * @return the randomly generated number.
	 */
	private int nextRandomNon10( Random rng, int min, int max )  {
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
	private int nextRandomRanged( Random rng, int min, int max ) {
		// Maybe use nextGaussian here instead?
		return min + (int) (rng.nextDouble() * ((max - min) + 1));
	}

	/**
	 * Performs a Fisher-Yates shuffle.
	 * see http://stackoverflow.com/questions/2249520/javas-collections-shuffle-is-doing-what
	 *
	 * @param arr the array.
	 * @param rng the RNG (random number generator).
	 */
	private void shuffle( int[] arr, Random rng ) {
		for ( int i = arr.length; i > 1; i-- ) {
			swap( arr, i - 1, rng.nextInt( i ) );
		}
	}

	/**
	 * Swaps indexes a & b in arr.
	 *
	 * @param arr the integer array.
	 * @param a index a.
	 * @param b index b.
	 */
	private void swap( int[] arr, int a, int b ) {
		int t = arr[a];
		arr[a] = arr[b];
		arr[b] = t;
	}

	/**
	 * Computes the coefficient of variance of an integer array.
	 *
	 * @param arr the array.
	 * @return the resulting coefficient.
	 */
	private double computeVariance( int[] arr ) {
		double mean = computeMean( arr );
		double stdDev = computeStdDev( arr, mean );

		return stdDev / mean;
	}

	/**
	 * Computes the standard deviation of an integer array given its mean.
	 *
	 * @param arr the array.
	 * @param mean the mean.
	 * @return the resulting standard deviation.
	 */
	private double computeStdDev( int[] arr, double mean ) {
		double diffSquareSum = 0;
		for ( int i : arr ) {
			double diff = i - mean;
			diffSquareSum += diff * diff;
		}

		return Math.sqrt( diffSquareSum / arr.length );
	}

	/**
	 * Computes the mean of an integer array.
	 *
	 * @param arr the array.
	 * @return the resulting mean.
	 */
	private double computeMean( int[] arr ) {
		double sum = 0;
		for ( int i : arr ) {
			sum += i;
		}

		return sum / arr.length;
	}
}