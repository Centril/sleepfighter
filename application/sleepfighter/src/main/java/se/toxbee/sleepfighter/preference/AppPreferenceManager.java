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

import se.toxbee.sleepfighter.utils.model.LocalizationProvider;
import se.toxbee.sleepfighter.utils.prefs.PreferenceNode;

/**
 * {@link AppPreferenceManager} is the central API for application wide preferences.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Dec 15, 2013
 */
public final class AppPreferenceManager extends AppPreferenceNode {
	public AppPreferenceManager( PreferenceNode b, LocalizationProvider lp ) {
		super( b, null );

		alarmControl = new AlarmControlPreferences( b );
		display = new DisplayPreferences( p );
		challenge = new ChallengeGlobalPreferences( p, lp );
		locFilter = new LocationFilterPreferences( p );
		weather = new WeatherPreferences( p );
	}

	public final DisplayPreferences display;
	public final AlarmControlPreferences alarmControl;
	public final LocationFilterPreferences locFilter;
	public final ChallengeGlobalPreferences challenge;
	public final WeatherPreferences weather;

	/**
	 * Returns whether or not proprietary code is allowed.<br/>
	 * If this returns false NOTHING proprietary is allowed to run.
	 *
	 * @return
	 */
	public boolean isProprietaryAllowed() {
		return p.getBoolean( "isProprietaryAllowed", true );
	}

	/**
	 * Returns
	 *
	 * @return
	 */
	public boolean isOpenSourceOnly() {
		return !this.isProprietaryAllowed();
	}

	/**
	 * Sets the current versionCode in preferences.
	 *
	 * @param versionCode the current versionCode to set.
	 */
	public void versionCode( int versionCode ) {
		p.setInt( "versionCode", versionCode );
	}

	/**
	 * Returns the current versionCode in preferences.
	 *
	 * @return the current versionCode.
	 */
	public int versionCode() {
		return p.getInt( "versionCode", -1 );
	}
}
