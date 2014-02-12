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

import se.toxbee.sleepfighter.model.SortMode;
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

	/**
	 * Returns the sort mode currently used.
	 *
	 * @return the sort mode.
	 */
	public SortMode getSortMode() {
		return new SortMode( SortMode.Field.from( p.getInt( "sortField", 0 ) ),
				p.getBoolean( "sortDirection", true ) );
	}

	/**
	 * Sets the sort mode to currently use.
	 *
	 * @param mode the mode to use.
	 */
	public void setSortMode( SortMode mode ) {
		p.setInt( "sortField", mode.field().ordinal() ).setBoolean( "sortDirection", mode.direction() );
	}
}
