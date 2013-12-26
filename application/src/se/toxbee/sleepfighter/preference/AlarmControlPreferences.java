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
package se.toxbee.sleepfighter.preference;

import se.toxbee.sleepfighter.utils.prefs.PreferenceNode;

/**
 * {@link AlarmControlPreferences} contains preferences for when an alarm has been issued.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Dec 15, 2013
 */
public class AlarmControlPreferences extends AppPreferenceNode {
	protected AlarmControlPreferences( PreferenceNode b ) {
		super( b, "alarmControl" );
	}

	/**
	 * Returns whether or not to turn the screen on when an Alarm rings.<br/>
	 * Default is true.
	 *
	 * @return true if the screen should be turned on.
	 */
	public boolean turnScreenOn() {
		return p.getBoolean( "turnScreenOn", true );
	}

	/**
	 * Returns whether or not to bypass the lockscreen when an Alarm rings.<br/>
	 * Default is true.
	 *
	 * @return true if the lockscreen should be bypassed.
	 */
	public boolean bypassLockscreen() {
		return p.getBoolean( "bypassLockScreen", true );
	}
}
