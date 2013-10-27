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
package se.toxbee.sleepfighter.utils.math;

import java.util.Arrays;

import junit.framework.TestCase;

public class ConversionTest extends TestCase {
	public void testBoolArrayToInt() {
		try {
			Conversion.boolArrayToInt( null );
			fail("Should throw NullPointerException");
		} catch( Exception e ) {
			assertTrue( e instanceof NullPointerException );
		}

		boolean[][] arrays = new boolean[][] {
			{ false, false, false, false },
			{ true, true, true, true },
			{ true, true, true, false },
			{ true, true, false, true }
		};

		int[] tests = new int[] { 0, 15, 7, 11 };

		for ( int i = 0; i < arrays.length; i++ ) {
			assertTrue( Conversion.boolArrayToInt( arrays[i] ) == tests[i] );
		}
	}

	public void testIntToBoolArray() {
		int[] tests = new int[] { 0, 15, 7, 11 };

		// Test first version.
		boolean[][] arrays = new boolean[][] {
				{ false, false, false, false },
				{ true, true, true, true },
				{ true, true, true, false },
				{ true, true, false, true }
			};
		for ( int i = 0; i < arrays.length; i++ ) {
			Arrays.equals( arrays[i], Conversion.intToBoolArray( tests[i], arrays[i].length) );
		}

		// Test second version.
		boolean[][] arrays2 = new boolean[][] {
				{  },
				{ true, true, true, true },
				{ true, true, true },
				{ true, true, false, true }
			};
		for ( int i = 0; i < arrays2.length; i++ ) {
			Arrays.equals( arrays2[i], Conversion.intToBoolArray( tests[i], arrays2[i].length) );
		}
	}
}
