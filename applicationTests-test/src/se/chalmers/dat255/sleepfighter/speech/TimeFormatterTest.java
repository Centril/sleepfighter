package se.chalmers.dat255.sleepfighter.speech;

import junit.framework.TestCase;

public class TimeFormatterTest extends TestCase{
	public void testFormatTime() {
		assertEquals("quarter to ten", TimeFormatter.formatTime(9, 45));
		assertEquals("quarter past twelve", TimeFormatter.formatTime(12, 15));
		assertEquals("half past ten", TimeFormatter.formatTime(10, 30));
		assertEquals("twenty-five past four", TimeFormatter.formatTime(4, 25));
		assertEquals("five to eight", TimeFormatter.formatTime(7, 55));
		
		// test the rounding as well
		
		assertEquals("five to eight", TimeFormatter.formatTime(7, 56));
		assertEquals("five to eight", TimeFormatter.formatTime(7, 57));
		assertEquals("five to eight", TimeFormatter.formatTime(7, 54));
		assertEquals("five to eight", TimeFormatter.formatTime(7, 53));
		
		assertEquals("half past ten", TimeFormatter.formatTime(10, 28));
		assertEquals("half past ten", TimeFormatter.formatTime(10, 29));
		assertEquals("half past ten", TimeFormatter.formatTime(10, 31));
		assertEquals("half past ten", TimeFormatter.formatTime(10, 32));
		
		
		assertEquals("ten", TimeFormatter.formatTime(10, 2));
		assertEquals("ten", TimeFormatter.formatTime(10, 0));
		assertEquals("ten", TimeFormatter.formatTime(10, 1));
		
		assertEquals("ten", TimeFormatter.formatTime(9, 58));
		assertEquals("ten", TimeFormatter.formatTime(9, 59));
	}
}
