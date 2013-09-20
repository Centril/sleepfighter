package se.chalmers.dat255.sleepfighter.utils;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Calendar;
import java.util.List;

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
	 * @return the built earliest text string.
	 */
	
	public static final String getEarliestText( Resources res, Calendar now, EarliestInfo earliestInfo ) {
		return getEarliestText(res.getStringArray( R.array.earliest_time_formats), 
				 res.getStringArray( R.array.earliest_time_formats_parts ),
				 earliestInfo.getDiff( now ), earliestInfo );
	}
	
	// diff is how long it is until the alarm goes off the next time.
	public static final String getEarliestText( String[] formats , String[] partFormats, Calendar diff, EarliestInfo earliestInfo ) {
		String earliestText;

		// Not real? = we don't have any alarms active.
		if ( earliestInfo.isReal() ) {
			// Compute diff.
			int[] diffVal = { diff.get( Calendar.DAY_OF_MONTH ), diff.get( Calendar.HOUR_OF_DAY ), diff.get( Calendar.MINUTE ) };

			
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