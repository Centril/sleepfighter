/*
 * Copyright 2014 toxbee.se
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package se.toxbee.sleepfighter.preference;

import se.toxbee.commons.prefs.PreferenceNode;

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
