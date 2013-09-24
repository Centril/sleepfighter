package se.chalmers.dat255.sleepfighter.utils.math;

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