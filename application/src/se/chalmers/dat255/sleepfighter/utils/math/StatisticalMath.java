package se.chalmers.dat255.sleepfighter.utils.math;

/**
 * StatisticalMath provides simple math for statistical analysis.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Sep 30, 2013
 */
public class StatisticalMath {
	/**
	 * Computes the coefficient of variance of an integer array.
	 *
	 * @param arr the array.
	 * @return the resulting coefficient.
	 */
	public static double computeVariance( int[] arr ) {
		double mean = computeMean( arr );
		double stdDev = computeStdDev( arr, mean );

		return stdDev / mean;
	}

	/**
	 * Computes the standard deviation of an integer array given its mean.<br/>
	 * It is not defined for an empty array.
	 *
	 * @param arr the array.
	 * @param mean the mean.
	 * @return the resulting standard deviation.
	 */
	public static double computeStdDev( int[] arr, double mean ) {
		double diffSquareSum = 0;
		for ( int i : arr ) {
			double diff = i - mean;
			diffSquareSum += diff * diff;
		}

		return Math.sqrt( diffSquareSum / arr.length );
	}

	/**
	 * Computes the mean of an integer array.<br/>
	 * It is not defined for an empty array.
	 *
	 * @param arr the array.
	 * @return the resulting mean.
	 */
	public static double computeMean( int[] arr ) {
		double sum = 0;
		for ( int i : arr ) {
			sum += i;
		}

		return sum / arr.length;
	}
}