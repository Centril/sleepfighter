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
package se.toxbee.sleepfighter.utils.math;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;

public class ConversionTest {
	@Test
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

	@Test
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
