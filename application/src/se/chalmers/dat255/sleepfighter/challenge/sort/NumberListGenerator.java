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

import java.util.Random;

/**
 * NumberListGenerator is an interface that provides logic<br/>
 * for generating a list of numbers from a source of randomness.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Sep 30, 2013
 */
public interface NumberListGenerator {
	/**
	 * Generates the list of numbers given a random number generator.
	 *
	 * @param rng the random number generator
	 * @param size the size of the list to generate, where <code>size > 0</code>.
	 * @return the list.
	 */
	public int[] generateList( Random rng, int size );

	/**
	 * Sets the amount of digits > 1 a generated number must exactly have.<br/>
	 * The default is 3 digits.
	 *
	 * @param digits the number of digits in every number, e.g: 3 for 100-999.
	 */
	public void setNumDigits( int digits );

	/**
	 * Returns the amount of digits > 1 a generated number must exactly have.
	 *
	 * @return n(digits) in generated numbers.
	 */
	public int getNumDigits();
}
