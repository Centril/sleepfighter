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
package se.toxbee.sleepfighter.text;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;
import org.joda.time.MutableDateTime;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import se.toxbee.sleepfighter.R;
import se.toxbee.sleepfighter.app.SFApplication;
import se.toxbee.sleepfighter.model.Alarm;
import se.toxbee.sleepfighter.model.AlarmTimestamp;
import se.toxbee.sleepfighter.utils.collect.PrimitiveArrays;
import android.content.res.Resources;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;

/**
 * String/text specific date utility methods.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Sep 19, 2013
 */
public class DateTextUtils {
	/**
	 * Builds and returns the time of an {@link AlarmTimestamp} in the preferred manner<br/>
	 * according to {@link GlobalPreferencesManager#displayPeriodOrTime()}.
	 *
	 * @param stamp the alarm and its timestamp info.
	 * @return the time.
	 */
	public static final String printTime( long now, AlarmTimestamp stamp ) {
		SFApplication app = SFApplication.get();
		boolean periodOrTime = app.getPrefs().display.earliestAsPeriod();

		Resources res = app.getResources();

		String text = periodOrTime
					? getTime( res, now, stamp, app.locale() )
					: getTimeToText( res, now, stamp );

		return text;
	}

	/**
	 * Builds and returns text for the "exact" time an alarm occurs as opposed to the period left for it to occur.<br/>
	 * In English, 12:00 today would become "Today 12:00", tomorrow would be come "Tomorrow 12:00", and on Monday it would become
	 *
	 * @param res resources bundle
	 * @param now current time in Unix epoch timestamp.
	 * @param ats an AlarmTimestamp: the information about the alarm & its timestamp.
	 * @param locale the locale to use for weekdays.
	 * @return the built time-to string.
	 */
	public static final String getTime( Resources res, long now, AlarmTimestamp ats, Locale locale ) {
		String noActive = res.getStringArray( R.array.earliest_time_formats)[0];
		String[] formats = res.getStringArray( R.array.exact_time_formats );
		return getTime( formats, noActive, now, ats, locale );
	}

	/**
	 * Builds and returns text for the "exact" time an alarm occurs as opposed to the period left for it to occur.<br/>
	 * In English, 12:00 today would become "Today 12:00", tomorrow would be come "Tomorrow 12:00", and on Monday it would become
	 *
	 * @param formats the formats to use, e.g: [Today %1$s, Tomorrow %1$s, %2$s %1$s].
	 * @param noActive if no alarm was active, this is used.
	 * @param now current time in Unix epoch timestamp.
	 * @param ats an AlarmTimestamp: the information about the alarm & its timestamp.
	 * @param locale the locale to use for weekdays.
	 * @return the built time-to string.
	 */
	public static final String getTime( String[] formats, String noActive, long now, AlarmTimestamp ats, Locale locale ) {	
		if ( ats == AlarmTimestamp.INVALID ) {
			return noActive;
		} else {
			// Due to threading, now could be after ats - so we're forgiving here.
			// Prepare replacements.
			Alarm alarm = ats.getAlarm();
			String timeReplacement = alarm.getTime().exact().getTimeString();
			DateTime timeWork = new DateTime( ats.getMillis() );

			// Calculate start of tomorrow.
			DateTime nowTime = new DateTime( now );
			LocalDate tomorrow = new LocalDate( nowTime ).plusDays( 1 );
			DateTime startOfTomorrow = tomorrow.toDateTimeAtStartOfDay( nowTime.getZone() );

			if ( timeWork.isBefore( startOfTomorrow ) ) {
				// Alarm is today.
				return String.format( formats[0], timeReplacement );
			}

			// Calculate start of the day after tomorrow.
			LocalDate afterTomorrow = tomorrow.plusDays( 1 );
			DateTime startOfAfterTomorrow = afterTomorrow.toDateTimeAtStartOfDay( nowTime.getZone() );

			if ( timeWork.isBefore( startOfAfterTomorrow ) ) {
				// Alarm is tomorrow.
				return String.format( formats[1], timeReplacement );
			}

			// Alarm is after tomorrow.
			String weekday = new DateTime( ats.getMillis() ).dayOfWeek().getAsText( locale );
			return String.format( formats[2], timeReplacement, weekday );
		}
	}

	/**
	 * Builds and returns text for the time to an alarm given resources, current time, and AlarmTimestamp when given a resources bundle.
	 *
	 * @param res Android resources.
	 * @param now current time in unix epoch timestamp.
	 * @param ats an AlarmTimestamp: the information about the alarm & its timestamp.
	 * @return the built time-to string.
	 */
	public static final String getTimeToText( Resources res, long now, AlarmTimestamp ats ) {
		Period diff = null;
		
		/*
		 * ats is invalid when all the alarms have been turned off.
		 * INVALID has the value null.
		 * therefore, we must do a nullcheck, otherwise we get an exception.
		 */
		if ( ats != AlarmTimestamp.INVALID ) {
			diff = new Period( now, ats.getMillis() );
		}
		
		String[] formats = res.getStringArray( R.array.earliest_time_formats);
		String[] partFormats =  res.getStringArray( R.array.earliest_time_formats_parts );

		return getTimeToText( formats, partFormats, diff, ats );
	}

	/**
	 * Builds and returns earliest text when given a resources bundle.
	 *
	 * @param formats an array of strings containing formats for [no-alarm-active, < minute, xx-yy-zz, xx-yy, xx]
	 * 		where xx, yy, zz can be either day, hours, minutes (non-respectively).
	 * @param partsFormat an array of strings containing part formats for [day, month, hour].
	 * @param diff difference between now and ats.
	 * @param ats an AlarmTimestamp: the information about the alarm & its timestamp.
	 * @return the built time-to string.
	 */
	public static final String getTimeToText( String[] formats, String[] partFormats, Period diff, AlarmTimestamp ats ) {
		String earliestText;

		// Not real? = we don't have any alarms active.
		if ( ats == AlarmTimestamp.INVALID ) {
			earliestText = formats[0];
		} else {
			int[] diffVal = { Math.abs( diff.getDays() ), Math.abs( diff.getHours() ), Math.abs( diff.getMinutes() ) };

			// What fields are set?
			BitSet setFields = new BitSet(3);
			setFields.set( 0, diffVal[0] != 0 );
			setFields.set( 1, diffVal[1] != 0 );
			setFields.set( 2, diffVal[2] != 0 );
			int cardinality = setFields.cardinality();

			earliestText = formats[cardinality + 1];

			if ( cardinality > 0 ) {
				List<String> args = new ArrayList<String>(3);

				for (int i = setFields.nextSetBit(0); i >= 0; i = setFields.nextSetBit(i + 1) ) {
					args.add( partFormats[i] );
				}

				// Finally format everything.
				earliestText = String.format( earliestText, args.toArray() );
				earliestText = String.format( earliestText, diffVal[0], diffVal[1], diffVal[2] );
			} else {
				// only seconds remains until it goes off.
				earliestText = formats[1];
			}
		}

		return earliestText;
	}

	/**
	 * Returns an array of strings with weekday names.
	 *
	 * @param indiceLength how long each string should be.
	 * @param locale the desired locale.
	 * @return the array of strings.
	 */
	public static final String[] getWeekdayNames( int indiceLength, Locale locale ) {
		DateTimeFormatter fmt = DateTimeFormat.forPattern( Strings.repeat( "E", indiceLength ) ).withLocale( locale );

		MutableDateTime time = new MutableDateTime();
		time.setDayOfWeek( DateTimeConstants.MONDAY );

		String[] names = new String[DateTimeConstants.DAYS_PER_WEEK];

		for ( int day = 0; day < DateTimeConstants.DAYS_PER_WEEK; day++ ) {
			String name = fmt.print( time );

			if ( name.length() > indiceLength ) {
				name = name.substring( 0, indiceLength );
			}

			names[day] = name;

			time.addDays( 1 );
		}

		return names;
	}

	/**
	 * Returns a SpannableString of the enabled days in an alarm,<br/>
	 * where the days are differently colored if enabled/disabled.
	 *
	 * @param alarm the alarm to make text for.
	 * @return the string.
	 */
	public static final SpannableString makeEnabledDaysText( final Alarm alarm ) {
		// Compute weekday names & join.
		String[] names = getWeekdayNamesShort();
		boolean[] enabledDays = alarm.getEnabledDays();

		// Shift weekdays.
		Locale l = SFApplication.get().locale();
		int shiftSteps = -(getFirstDayOfWeek( l ) - 1);
		PrimitiveArrays.shift( enabledDays, shiftSteps );
		PrimitiveArrays.shift( names, shiftSteps );

		// Join string.
		SpannableString text = new SpannableString( WEEKDAYS_JOINER.join( names ) );

		// Stateful coloring.
		Resources res = SFApplication.get().getResources();
		int enabledColor = Color.WHITE;
		int disabledColor = res.getColor( R.color.nearly_background_text );

		int start = 0;
		for ( int i = 0; i < enabledDays.length; i++ ) {
			boolean enabled = enabledDays[i];
			int length = names[i].length();

			int color = enabled ? enabledColor : disabledColor;
			text.setSpan( new ForegroundColorSpan( color ), start, start + length, 0 );

			start += length + 2;
		}

		return text;
	}

	private static final String[] getWeekdayNamesShort() {
		Resources res = SFApplication.get().getResources();
		String[] days = res.getStringArray( R.array.weekdays_short_names );
		return days;
	}

	private static final int getFirstDayOfWeek( Locale l ) {
	  return ((Calendar.getInstance( l ).getFirstDayOfWeek() + 5) % 7) + 1;
	}

	private static final Joiner WEEKDAYS_JOINER = Joiner.on( "  " );
}
