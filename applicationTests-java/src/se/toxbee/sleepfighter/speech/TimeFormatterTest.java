package se.toxbee.sleepfighter.speech;

import java.util.Locale;

import org.junit.Test;
import static org.junit.Assert.*;

public class TimeFormatterTest {
	@Test
	public void testFormatTime() {
		
		TimeFormatter tm = new TimeFormatter(Locale.ENGLISH);
		
		assertEquals("quarter to 10 a.m. ", tm.formatTime(9, 45));
		assertEquals("quarter past 12 p.m. ", tm.formatTime(12, 15));
		assertEquals("half past 10 a.m. ", tm.formatTime(10, 30));
		assertEquals("25 past 4 a.m. ", tm.formatTime(4, 25));
		assertEquals("5 to 8 a.m. ", tm.formatTime(7, 55));
		
		// test the rounding as well
		
		assertEquals("5 to 8 a.m. ", tm.formatTime(7, 56));
		assertEquals("5 to 8 a.m. ", tm.formatTime(7, 57));
		assertEquals("5 to 8 a.m. ", tm.formatTime(7, 54));
		assertEquals("5 to 8 a.m. ", tm.formatTime(7, 53));
		
		assertEquals("half past 10 a.m. ", tm.formatTime(10, 28));
		assertEquals("half past 10 a.m. ", tm.formatTime(10, 29));
		assertEquals("half past 10 a.m. ", tm.formatTime(10, 31));
		assertEquals("half past 10 a.m. ", tm.formatTime(10, 32));
		
		
		assertEquals("10 a.m. ", tm.formatTime(10, 2));
		assertEquals("10 a.m. ", tm.formatTime(10, 0));
		assertEquals("10 a.m. ", tm.formatTime(10, 1));
	
		assertEquals("10 a.m. ", tm.formatTime(9, 58));
		assertEquals("10 a.m. ", tm.formatTime(9, 59));
		
		// now let's test the am and pm functionality rigorously 
		assertEquals("25 past 3 p.m. ", tm.formatTime(15, 23));
		
		assertEquals("5 to 12 p.m. ", tm.formatTime(23, 53));
		
		assertEquals("12 a.m. ", tm.formatTime(23, 58));
		assertEquals("12 a.m. ", tm.formatTime(00, 2));
		assertEquals("1 a.m. ", tm.formatTime(00, 59));
		
		assertEquals("5 to 1 a.m. ", tm.formatTime(00, 54));
		
		
		assertEquals("1 a.m. ", tm.formatTime(01, 00));
		assertEquals("1 a.m. ", tm.formatTime(01, 2));
		assertEquals("5 past 3 a.m. ", tm.formatTime(03, 4));
		
		assertEquals("10 to 12 a.m. ", tm.formatTime(11, 52));
		assertEquals("12 p.m. ", tm.formatTime(11, 59));
		assertEquals("half past 12 p.m. ", tm.formatTime(12, 30));
		assertEquals("quarter to 1 p.m. ", tm.formatTime(12, 45));
		assertEquals("quarter to 1 p.m. ", tm.formatTime(12, 46));
		assertEquals("quarter to 1 p.m. ", tm.formatTime(12, 44));

		assertEquals("5 to 1 p.m. ", tm.formatTime(12, 56));
		assertEquals("1 p.m. ", tm.formatTime(12, 58));
		assertEquals("1 p.m. ", tm.formatTime(13, 0));
		assertEquals("1 p.m. ", tm.formatTime(13, 2));
		assertEquals("quarter past 4 p.m. ", tm.formatTime(16, 14));
		assertEquals("quarter to 5 p.m. ", tm.formatTime(16, 44));

	}

	@Test
	public void testFormatTimeSwedish() {
		
		TimeFormatter tm = new TimeFormatter(new Locale("sv", "SE"));
		
		assertEquals("kvart i 10 på förmiddagen", tm.formatTime(9, 45));
		assertEquals("kvart över 12 på eftermiddagen", tm.formatTime(12, 15));
		assertEquals("halv 11 på förmiddagen", tm.formatTime(10, 30));
		assertEquals("5 i halv 5 på förmiddagen", tm.formatTime(4, 25));
		assertEquals("5 i 8 på förmiddagen", tm.formatTime(7, 55));
		
		// test the rounding as well
		
		assertEquals("5 i 8 på förmiddagen", tm.formatTime(7, 56));
		assertEquals("5 i 8 på förmiddagen", tm.formatTime(7, 57));
		assertEquals("5 i 8 på förmiddagen", tm.formatTime(7, 54));
		assertEquals("5 i 8 på förmiddagen", tm.formatTime(7, 53));
		
		assertEquals("halv 11 på förmiddagen", tm.formatTime(10, 28));
		assertEquals("halv 11 på förmiddagen", tm.formatTime(10, 29));
		assertEquals("halv 11 på förmiddagen", tm.formatTime(10, 31));
		assertEquals("halv 11 på förmiddagen", tm.formatTime(10, 32));
		
		assertEquals("10 på förmiddagen", tm.formatTime(10, 2));
		assertEquals("10 på förmiddagen", tm.formatTime(10, 0));
		assertEquals("10 på förmiddagen", tm.formatTime(10, 1));
		
		assertEquals("10 på förmiddagen", tm.formatTime(9, 58));
		assertEquals("10 på förmiddagen", tm.formatTime(9, 59));
		
		// now let's test the am and pm functionality rigorously 
		assertEquals("5 i halv 4 på eftermiddagen", tm.formatTime(15, 23));
		
		assertEquals("5 i 12 på eftermiddagen", tm.formatTime(23, 53));
		
		assertEquals("12 på förmiddagen", tm.formatTime(23, 58));
		assertEquals("12 på förmiddagen", tm.formatTime(00, 2));
		assertEquals("1 på förmiddagen", tm.formatTime(00, 59));
		
		assertEquals("5 i 1 på förmiddagen", tm.formatTime(00, 54));
			
		assertEquals("1 på förmiddagen", tm.formatTime(01, 00));
		assertEquals("1 på förmiddagen", tm.formatTime(01, 2));
		assertEquals("5 över 3 på förmiddagen", tm.formatTime(03, 4));
		
		assertEquals("10 i 12 på förmiddagen", tm.formatTime(11, 52));
		assertEquals("12 på eftermiddagen", tm.formatTime(11, 59));
		assertEquals("halv 1 på eftermiddagen", tm.formatTime(12, 30));
		assertEquals("kvart i 1 på eftermiddagen", tm.formatTime(12, 45));
		assertEquals("kvart i 1 på eftermiddagen", tm.formatTime(12, 46));
		assertEquals("kvart i 1 på eftermiddagen", tm.formatTime(12, 44));

		assertEquals("5 i 1 på eftermiddagen", tm.formatTime(12, 56));
		assertEquals("1 på eftermiddagen", tm.formatTime(12, 58));
		assertEquals("1 på eftermiddagen", tm.formatTime(13, 0));
		assertEquals("1 på eftermiddagen", tm.formatTime(13, 2));
		assertEquals("kvart över 4 på eftermiddagen", tm.formatTime(16, 14));
		assertEquals("kvart i 5 på eftermiddagen", tm.formatTime(16, 44));

		assertEquals("halv 12 på förmiddagen", tm.formatTime(11, 30));

		// tests unique to the swedish version.
		assertEquals("5 i halv 1 på eftermiddagen", tm.formatTime(12, 24));
		assertEquals("5 över halv 1 på eftermiddagen", tm.formatTime(12, 34));


	}
}
