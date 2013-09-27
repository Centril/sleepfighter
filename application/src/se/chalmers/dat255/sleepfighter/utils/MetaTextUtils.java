package se.chalmers.dat255.sleepfighter.utils;

import se.chalmers.dat255.sleepfighter.R;
import se.chalmers.dat255.sleepfighter.model.Alarm;
import android.content.Context;

/**
 * MetaTextUtils provides text utilities for meta information in Alarm.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Sep 25, 2013
 */
public class MetaTextUtils {
	/**
	 * Prints (returns) the name of alarm as a string.
	 *
	 * @param context android context.
	 * @param alarm the alarm.
	 * @return the alarm name as text.
	 */
	public static final String printAlarmName( Context context, final Alarm alarm ) {
		if ( !alarm.isUnnamed() ) {
			return alarm.getName();
		}

		String format = context.getResources().getString( R.string.alarm_unnamed_format );
		return String.format( format, alarm.getUnnamedPlacement() );
	}

	/**
	 * Construction forbidden.
	 */
	private MetaTextUtils() {
	}
}