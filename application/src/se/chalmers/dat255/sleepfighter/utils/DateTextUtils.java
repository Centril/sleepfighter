package se.chalmers.dat255.sleepfighter.utils;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import org.joda.time.Period;

import se.chalmers.dat255.sleepfighter.R;
import se.chalmers.dat255.sleepfighter.debug.Debug;
import se.chalmers.dat255.sleepfighter.model.AlarmsManager.EarliestInfo;
import android.content.res.Resources;

/**
 * String/text specific date utility methods.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Sep 19, 2013
 */
public class DateTextUtils {
	/**
	 * Construction forbidden.
	 */
	private DateTextUtils() {
	}

	/**
	 * Builds and returns earliest text when given a resources bundle.
	 *
	 * @param res Android resources.
	 * @param now current time in unix epoch timestamp.
	 * @param earliestInfo the information about the earliest alarm.
	 * @return the built earliest text string.
	 */
	public static final String getEarliestText( Resources res, long now, EarliestInfo earliestInfo ) {
		Period diff = new Period( now, earliestInfo.getMillis() );
		String[] formats = res.getStringArray( R.array.earliest_time_formats);
		String[] partFormats =  res.getStringArray( R.array.earliest_time_formats_parts );

		return getEarliestText( formats, partFormats, diff, earliestInfo );
	}

	/**
	 * Builds and returns earliest text when given a resources bundle.
	 *
	 * @param formats an array of strings containing formats for [no-alarm-active, < minute, xx-yy-zz, xx-yy, xx]
	 * 		where xx, yy, zz can be either day, hours, minutes (non-respectively).
	 * @param Calendar a Calendar containing the difference from earliest Alarm to now.
	 * @param earliestInfo the information about the earliest alarm.
	 * @return the built earliest text string.
	 */
	public static final String getEarliestText( String[] formats, String[] partFormats, Period diff, EarliestInfo earliestInfo ) {
		String earliestText;

		// Not real? = we don't have any alarms active.
		if ( earliestInfo.isReal() ) {
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
				
				Debug.d("formatting: " +  earliestText);
				earliestText = String.format( earliestText, diffVal[0], diffVal[1], diffVal[2] );
			} else {
				// only seconds remains until it goes off.
				earliestText = formats[1];
			}
		} else {
			earliestText = formats[0];
		}

		return earliestText;
	}
}