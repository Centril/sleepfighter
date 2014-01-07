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
package se.toxbee.sleepfighter.challenge.sort;

import java.util.Random;

import se.toxbee.sleepfighter.utils.collect.PrimitiveArrays;

import com.badlogic.gdx.utils.IntArray;

/**
 * PermutatingListGenerator uses "permutations" for generating numbers.<br/>
 * Algorithm contributed by Niclas Alexandersson / Zarth.<br/>
 * Public interface defined by Mazdak.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @author Niclas Alexandersson / Zarth.
 * @version 1.0
 * @since Oct 12, 2013
 */
public class PermutatingListGenerator implements NumberListGenerator {
	@Override
	public int[] generateList( Random rng, int size ) {
		IntArray arr = new IntArray( size );

		// Put an initial number in the list
		arr.add( this.generateNumber( rng ) );

		while ( arr.size < size ) {
			/*
			 * Randomly choose a number from the list, manipulate it, and put
			 * the result back if it is not already present.
			 */
			int number = this.manipulateNumber( arr.get( rng.nextInt( arr.size ) ), rng );

			if ( !arr.contains( number ) ) {
				arr.add( number );
			}
		}

		PrimitiveArrays.shuffle( arr.items, rng );

		return arr.items;
	}

	@Override
	public void setNumDigits( int digits ) {
		// ignored.
	}

	@Override
	public int getNumDigits() {
		return 3;
	}

	private int manipulateNumber( int number, Random rng ) {
		/*
		 * Choose one of several possible manipulations to perform on the
		 * provided number. Each of these operations should result in a number
		 * that has at least something in common with the original number,
		 * hopefully making it harder to distinguish from the old one than by
		 * just generating a completely random number.
		 */
		switch ( rng.nextInt( 8 ) ) {
		case 0:
		case 1:
		case 2:
		case 3:
			/*
			 * Rotate is the only operation that can change the first digit, so
			 * to give an illusion of having a good distribution of numbers, we
			 * give it a larger chance to be chosen than the other operations.
			 */
			return rotate( number, rng );

		case 4:
			return permuteFinalDigits( number );

		case 5:
			return changeMiddleDigit( number, rng );

		case 6:
			return changeLastDigit( number, rng );

		case 7:
			return changeLastDigit( changeMiddleDigit( number, rng ), rng );

		default:
			return number;
		}
	}

	private int generateNumber( Random rng ) {
		return rng.nextInt( 1000 - 100 ) + 100;
	}

	private int permuteFinalDigits( int number ) {
		return number + ((number % 10) * 10) + ((number / 10) % 10) - (number % 100);
	}

	private int changeLastDigit( int number, Random rng ) {
		return number + rng.nextInt( 10 ) - (number % 10);
	}

	private int changeMiddleDigit( int number, Random rng ) {
		return number + (rng.nextInt( 10 ) * 10) - (((number / 10) % 10) * 10);
	}

	private int rotate( int number, Random rng ) {
		/*
		 * If a number is divisible by 100, we cannot rotate it without the
		 * first digit becoming 0.
		 */
		if ( number % 100 == 0 ) {
			return number;
		}

		/*
		 * If the number contains a 0, we choose the direction which won't place
		 * the 0 in the position of the first digit. Otherwise, we flip a coin
		 * to determine whether to rotate left or right.
		 */
		if ( (number % 10 == 0) || (((number / 10) % 10 != 0) && rng.nextBoolean()) ) {
			// rotate left
			return ((number * 10) % 1000) + (number / 100);
		} else {
			// rotate right
			return (number / 10) + ((number % 10) * 100);
		}
	}
}