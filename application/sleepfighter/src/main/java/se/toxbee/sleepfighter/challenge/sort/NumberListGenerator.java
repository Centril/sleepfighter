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

package se.toxbee.sleepfighter.challenge.sort;

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
