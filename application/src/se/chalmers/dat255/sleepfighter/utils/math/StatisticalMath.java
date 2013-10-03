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
