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

package se.toxbee.commons.math;

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

	/**
	 * Computes the coefficient of variance of a double array.
	 *
	 * @param arr the array.
	 * @return the resulting coefficient.
	 */
	public static double computeVariance( double[] arr ) {
		double mean = computeMean( arr );
		double stdDev = computeStdDev( arr, mean );

		return stdDev / mean;
	}

	/**
	 * Computes the standard deviation of an double array given its mean.<br/>
	 * It is not defined for an empty array.
	 *
	 * @param arr the array.
	 * @param mean the mean.
	 * @return the resulting standard deviation.
	 */
	public static double computeStdDev( double[] arr, double mean ) {
		double diffSquareSum = 0;
		for ( double i : arr ) {
			double diff = i - mean;
			diffSquareSum += diff * diff;
		}

		return Math.sqrt( diffSquareSum / arr.length );
	}

	/**
	 * Computes the mean of an double array.<br/>
	 * It is not defined for an empty array.
	 *
	 * @param arr the array.
	 * @return the resulting mean.
	 */
	public static double computeMean( double[] arr ) {
		double sum = 0;
		for ( double i : arr ) {
			sum += i;
		}

		return sum / arr.length;
	}

}