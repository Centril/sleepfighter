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
package se.chalmers.dat255.sleepfighter.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

/**
 * GlobalPreferencesReader provides a layer of abstraction above SharedPreferences.<br/>
 * It is more direct and knows about the global options in the application.<br/>
 * This also allows for one central location to edit strings, default values, etc.
 * 
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Sep 29, 2013
 */
public class GlobalPreferencesManager {

	// Preference keys
	private static final String PREF_LOCATION_FILTER_MAX_AGE = "pref_location_filter_max_age";
	private static final String PREF_LOCATION_FILTER_MIN_DISTANCE = "pref_location_filter_min_distance";
	private static final String PREF_LOCATION_FILTER_REFRESH_INTERVAL = "pref_location_filter_refresh_interval";
	private static final String PREF_LOCATION_FILTER_TTFF = "pref_location_filter_ttff";
	private static final String PREF_LOCATION_FILTER_ENABLED = "pref_location_filter_enabled";
	private static final String CHALLENGES_ACTIVATED = "challenges_activated";
	private static final String LAST_CHALLENGE_POINTS_GAINED = "lastChallengePointsGained";
	private static final String CHALLENGE_POINTS = "challenge_points";
	private static final String PREF_GLOBAL_WHEN_OR_IN_HOW_MUCH = "pref_global_when_or_in_how_much";
	private static final String PREF_ALARM_BYPASS_LOCK_SCREEN = "pref_alarm_bypass_lock_screen";
	private static final String PREF_ALARM_TURN_SCREEN_ON = "pref_alarm_turn_screen_on";
	private static final String PREF_TEMP_WEATHER = "pref_temp_weather";

	private SharedPreferences prefs;

	/**
	 * Constructs a GlobalPreferencesReader given the application context.
	 * 
	 * @param context application android context.
	 */
	public GlobalPreferencesManager( Context context ) {
		this.prefs = PreferenceManager.getDefaultSharedPreferences( context );
	}

	/**
	 * Returns whether or not to turn the screen on when an Alarm rings.<br/>
	 * Default is true.
	 *
	 * @return true if the screen should be turned on.
	 */
	public boolean turnScreenOn() {
		return this.prefs.getBoolean( PREF_ALARM_TURN_SCREEN_ON, true );
	}

	/**
	 * Returns whether or not to bypass the lockscreen when an Alarm rings.<br/>
	 * Default is true.
	 *
	 * @return true if the lockscreen should be bypassed.
	 */
	public boolean bypassLockscreen() {
		return this.prefs.getBoolean( PREF_ALARM_BYPASS_LOCK_SCREEN, true );
	}

	/**
	 * Returns whether or not to show the earliest Alarm as a period or as exact time.<br/>
	 * Default is false.
	 *
	 * @return true if a period should be displayed instead of exact time.
	 */
	public boolean displayPeriodOrTime() {
		return this.prefs.getBoolean( PREF_GLOBAL_WHEN_OR_IN_HOW_MUCH, false );
	}
	
	/**
	 * @return the number of challenge points stored in the shared preferences
	 */
	public int getChallengePoints() {
		return this.prefs.getInt(CHALLENGE_POINTS, 0);
	}
	
	/**
	 * Adds points to the challenge points stored in the shared preferences
	 * 
	 * @param points the number of points to be added
	 */
	public void addChallengePoints(int points) {
		long now = System.currentTimeMillis();
		
		if (now - prefs.getLong(LAST_CHALLENGE_POINTS_GAINED, 0) >= 6*60*60*1000
				|| points < 0) {
			int stored = prefs.getInt(CHALLENGE_POINTS, 0);
			if (stored + points >= 9999) {
				stored = 9999;
				points = 0;
			}
			else if (stored + points <= -999) {
				stored = -999;
				points = 0;
			}
			
			Editor edit = prefs.edit();
			edit.putInt(CHALLENGE_POINTS, stored + points);
			if (points > 0) {
				edit.putLong(LAST_CHALLENGE_POINTS_GAINED, now);
			}
			edit.commit();
		}
	}
	
	public boolean isChallengesActivated() {
		return prefs.getBoolean(CHALLENGES_ACTIVATED, true);
	}
	
	public void setChallengesActivated(boolean isActivated) {
		Editor edit = prefs.edit();
		edit.putBoolean(CHALLENGES_ACTIVATED, isActivated);
		edit.commit();
	}

	/**
	 * Returns whether or not the location filter feature is enabled.
	 *
	 * @return true if it is enabled.
	 */
	public boolean isLocationFilterEnabled() {
		return this.prefs.getBoolean( PREF_LOCATION_FILTER_ENABLED, true );
	}

	/**
	 * Sets whether or not the location filter feature is enabled.
	 *
	 * @param enabled true if it is enabled.
	 */
	public void setLocationFilterEnabled( boolean enabled ) {
		Editor edit = prefs.edit();
		edit.putBoolean(PREF_LOCATION_FILTER_ENABLED, enabled );
		edit.commit();
	}

	/**
	 * Returns the "first request time delta" value.
	 *
	 * @return the time in minutes, default is 15 atm.
	 */
	public int getLocationFRTD() {
		return this.prefs.getInt( PREF_LOCATION_FILTER_TTFF, 15 );
	}

	/**
	 * Returns the refresh interval time in minutes.
	 *
	 * @return the time in minutes, default is 1 atm.
	 */
	public int getLocationRefreshInterval() {
		return this.prefs.getInt( PREF_LOCATION_FILTER_REFRESH_INTERVAL, 1 );
	}

	/**
	 * Returns the minimum distance in meters a user has to move before location update.
	 *
	 * @return the minimum distance in meters.
	 */
	public int getLocationMinDistance() {
		return this.prefs.getInt( PREF_LOCATION_FILTER_MIN_DISTANCE, 100 );
	}

	/**
	 * Returns the maximum age of the last known location in minutes.<br/>
	 * If the age exceeds this, location filter won't run. 0 is unbounded max age.
	 *
	 * @return the minimum distance in meters.
	 */
	public int getLocationMaxAge() {
		return this.prefs.getInt( PREF_LOCATION_FILTER_MAX_AGE, 20 );
	}
	
	
	/**
	 * Used to temporarily store the weather. Some seconds before the app starts, the weather is fetched.
	 * This preference is used to temporarily store the weather info. 
	 * @param weather
	 */
	public void setWeather(String weather) {
		
		Editor edit = prefs.edit();
		edit.putString(PREF_TEMP_WEATHER, weather);
		edit.commit();
	}

	public String getWeather() {
		return this.prefs.getString(PREF_TEMP_WEATHER, null);
	}
}
