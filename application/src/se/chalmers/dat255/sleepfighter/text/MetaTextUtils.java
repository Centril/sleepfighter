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
package se.chalmers.dat255.sleepfighter.text;

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
