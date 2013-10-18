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
package se.chalmers.dat255.sleepfighter.utils;

import java.util.Arrays;
import java.util.Locale;

import junit.framework.TestCase;

import org.joda.time.MutablePeriod;
import org.joda.time.Period;

import se.chalmers.dat255.sleepfighter.model.AlarmTimestamp;
import se.chalmers.dat255.sleepfighter.text.DateTextUtils;

public class DateTextUtilsTests extends TestCase {
	private static final String[] formats = { "No alarm active",
			"In &lt; 1 minute", "In %1$s", "In %1$s and %2$s",
			"In %1$s, %2$s and %3$s" };

	private static final String[] partFormats = { "%1$dd", "%2$dh", "%3$dm" };

	private Period diff( int d, int h, int m ) {
		MutablePeriod diff = new MutablePeriod();
		diff.setDays( 2 );
		diff.setHours( 3 );
		diff.setMinutes( 0 );
		return diff.toPeriod();
	}

	private void test( String text, int d, int h, int m ) {
		AlarmTimestamp info = new AlarmTimestamp( Long.valueOf( 1 ), null );
		Period diff = diff( d, h, m );
		text = DateTextUtils.getTimeToText( formats, partFormats, diff, info );
		assertEquals( text, text );
	}

	public void testGetEarliestTextTest() {
		// not active
		AlarmTimestamp info = AlarmTimestamp.INVALID;
		String earliest = DateTextUtils.getTimeToText( formats, partFormats, null, info );
		assertEquals( formats[0], earliest );

		// days and hours
		test( "In 2d and 3h", 2, 3, 0 );

		// days and minutes
		test( "In 2d and 3m", 2, 0, 0 );

		// hours and minutes
		test( "In 2h and 3m", 0, 2, 3 );

		// days
		test( "In 2d", 2, 0, 0 );

		// minutes
		test( "In 3m", 0, 0, 3 );

		// days, hours, and seconds.
		test( "In 1d, 2h and 3m", 1, 2, 3 );
	}
	
	//public static final String[] getWeekdayNames( int indiceLength, Locale locale ) {
    public void testGetWeekdayNames(  ) {
    	String[] days = DateTextUtils.getWeekdayNames(2, Locale.ENGLISH);
    	String[] expected = { "Mo", "Tu", "We", "Th", "Fr", "Sa", "Su" };
    	
    	assertTrue(Arrays.equals(expected, days));
    	
    	String[] dagar = DateTextUtils.getWeekdayNames(3, new Locale("sv"));
    	String[] expected2 = { "mån", "tis", "ons", "tor", "fre", "lör", "sön" };
    	
    	assertTrue(Arrays.equals(expected2, dagar));
    	
    }
		
}
