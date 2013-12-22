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
 * {@link DisplayPreferences} carries preferences for display, style, theme related things.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Dec 15, 2013
 */
public class DisplayPreferences extends AppPreferenceNode {
	protected DisplayPreferences( PreferenceNode b ) {
		super( b, "display" );
	}

	/**
	 * Returns whether or not to show the earliest Alarm as a period as opposed to exact time.<br/>
	 * Default is false.
	 *
	 * @return true if a period should be displayed instead of exact time.
	 */
	public boolean earliestAsPeriod() {
		return p.getBoolean( "earliestAsPeriod", false );
	}
}
