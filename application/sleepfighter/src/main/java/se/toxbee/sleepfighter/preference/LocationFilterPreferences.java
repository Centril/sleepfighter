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
 * {@link LocationFilterPreferences} handles location-filter specific preferences.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Dec 15, 2013
 */
public class LocationFilterPreferences extends AppPreferenceNode {
	protected LocationFilterPreferences( PreferenceNode b ) {
		super( b, "locfilter" );
	}

	/**
	 * Returns whether or not the location filter feature is enabled.
	 *
	 * @return true if it is enabled.
	 */
	public boolean isEnabled() {
		return p.getBoolean( "isEnabled", true );
	}

	/**
	 * Sets whether or not the location filter feature is enabled.
	 *
	 * @param enabled true if it is enabled.
	 */
	public void setEnabled( boolean enabled ) {
		p.setBoolean( "isEnabled", enabled );
	}

	/**
	 * Returns the "first request time delta" value.
	 *
	 * @return the time in minutes, default is 15 atm.
	 */
	public int firstRequestDT() {
		return p.getInt( "firstRequestDT", 15 );
	}

	/**
	 * Returns the refresh interval time in minutes.
	 *
	 * @return the time in minutes, default is 1 atm.
	 */
	public int requestRefreshInterval() {
		return p.getInt( "requestRefreshInterval", 1 );
	}

	/**
	 * Returns the minimum distance in meters a user has to move before location update.
	 *
	 * @return the minimum distance in meters.
	 */
	public int minDistance() {
		return p.getInt( "minDistance", 100 );
	}

	/**
	 * Returns the maximum age of the last known location in minutes.<br/>
	 * If the age exceeds this, location filter won't run. 0 is unbounded max age.
	 *
	 * @return the minimum distance in meters.
	 */
	public int maxAllowedAge() {
		return p.getInt( "maxAllowedAge", 20 );
	}
}
