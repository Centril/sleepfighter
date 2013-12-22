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
