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

package se.toxbee.sleepfighter.preference.migration;

import se.toxbee.sleepfighter.utils.migration.IMigrationException;
import se.toxbee.sleepfighter.utils.prefs.PreferenceNode;

public class Version8 extends Migrater.Adapter {
	@Override
	public void applyMigration( PreferenceNode p ) throws IMigrationException {
		p.remove( "pref_temp_weather" );

		// control
		b( p, "pref_alarm_turn_screen_on", "alarmControl.turnScreenOn", true );
		b( p, "pref_alarm_bypass_lock_screen", "alarmControl.bypassLockScreen", true );

		// location filter
		b( p, "pref_location_filter_enabled", "locfilter.isEnabled", true );
		i( p, "pref_location_filter_ttff", "locfilter.firstRequestDT", 15 );
		i( p, "pref_location_filter_refresh_interval", "locfilter.requestRefreshInterval", 1 );
		i( p, "pref_location_filter_min_distance", "locfilter.minDistance", 100 );
		i( p, "pref_location_filter_max_age", "locfilter.maxAllowedAge", 20 );

		// challenge points
		b( p, "challenges_activated", "challenges.isActivated", true );
		i( p, "challenge_points", "challenges.points", 0 );
		l( p, "lastChallengePointsGained", "challenges.lastTimeEarned", 0 );

		// display
		b( p, "pref_global_when_or_in_how_much", "display.earliestAsPeriod", false );
	}

	private void b( PreferenceNode p, String f, String t, boolean d ) {
		boolean v = p.getBoolean( f, d );
		p.remove( f );
		p.setBoolean( t, v );
	}

	private void i( PreferenceNode p, String f, String t, int d ) {
		int v = p.getInt( f, d );
		p.remove( f );
		p.setInt( t, v );
	}

	private void l( PreferenceNode p, String f, String t, long d ) {
		long v = p.getLong( f, d );
		p.remove( f );
		p.setLong( t, v );
	}
};