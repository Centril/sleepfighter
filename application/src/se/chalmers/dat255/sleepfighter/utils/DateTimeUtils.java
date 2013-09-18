package se.chalmers.dat255.sleepfighter.utils;

import java.util.Calendar;

/**
 * Provides utilities for time & date.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Sep 19, 2013
 */
public class DateTimeUtils {
	/**
	 * Construction forbidden.
	 */
	private DateTimeUtils() {
	}

	/**
	 * Returns difference between first and second calendars as a Calendar.
	 *
	 * @param first first calendar.
	 * @param second second calendar.
	 * @return the difference.
	 */
	public static Calendar getDateDiff( Calendar first, Calendar second ) {
		return getDateDiff( first, second.getTimeInMillis() );
	}

	/**
	 * Returns difference between first calendar and second timestamp as a Calendar.
	 *
	 * @param first first calendar.
	 * @param second second timestamp.
	 * @return the difference.
	 */
	public static Calendar getDateDiff( Calendar first, long second ) {
		Calendar diff = (Calendar) first.clone();
		diff.setTimeInMillis( Math.abs( first.getTimeInMillis() - second ) );
		return diff;
	}
}