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

import se.toxbee.commons.model.LocalizationProvider;
import se.toxbee.commons.prefs.PreferenceNode;
import se.toxbee.commons.prefs.PreferenceNode.PreferenceEditCallback;

/**
 * {@link ChallengeGlobalPreferences} handles global challenge related preferences.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Dec 15, 2013
 */
public class ChallengeGlobalPreferences extends AppPreferenceNode {
	private static final int CHALLENGE_COMPLETED_EARNING = 5;

	private static final int EMERGENCY_COST_MAX = 100;
	private static final float EMERGENCY_COST_FACTOR = 0.2f;

	private static final int SNOOZE_COST_MAX = 10;
	private static final float SNOOZE_COST_FACTOR = 0.05f;

	private static final int HOURS_BETWEEN_POINTS = 6;
	private static final int MAX_POINTS = 9999;
	private static final int MIN_POINTS = -MAX_POINTS;

	private final LocalizationProvider lp;

	protected ChallengeGlobalPreferences( PreferenceNode b, LocalizationProvider lp ) {
		super( b, "challenges" );
		this.lp = lp;
	}

	/**
	 * Returns whether or not the challenge system is globally enabled or disabled.
	 *
	 * @return true = enabled.
	 */
	public boolean isActivated() {
		return p.getBoolean( "isActivated", true );
	}

	/**
	 * Sets if system is activated or not.
	 *
	 * @param isActivated flag.
	 */
	public void setActivated( boolean isActivated ) {
		p.setBoolean( "isActivated", isActivated );
	}

	/**
	 * Returns the current amount of challenge points.
	 *
	 * @return the points.
	 */
	public int getChallengePoints() {
		return p.getInt( "points", 0 );
	}

	/**
	 * Earn points for a completed challenge.
	 */
	public void earnCompleted() {
		this.addChallengePoints( CHALLENGE_COMPLETED_EARNING );
	}

	/**
	 * Computes the emergency cost.
	 *
	 * @return the emergency cost.
	 */
	public int calcEmergencyCost() {
		int p = this.getChallengePoints();
		return Math.max( EMERGENCY_COST_MAX, (int) EMERGENCY_COST_FACTOR * p );
	}

	/**
	 * Withdraws the emergency cost from points.
	 */
	public void withdrawEmergencyCost() {
		this.addChallengePoints( -this.calcEmergencyCost() );
	}

	/**
	 * Computes the snooze cost.
	 *
	 * @return the cost of snoozing.
	 */
	public int calcSnoozeCost() {
		int p = this.getChallengePoints();
		return Math.max( SNOOZE_COST_MAX, (int) SNOOZE_COST_FACTOR * p );
	}

	/**
	 * Withdraws the snooze cost from points.
	 */
	public void withdrawSnoozeCost() {
		this.addChallengePoints( -this.calcSnoozeCost() );
	}

	private long lastTimeEarned() {
		return p.getLong( "lastTimeEarned", 0 );
	}

	private boolean canEarnPoints() {
		return this.lp.now() - this.lastTimeEarned() >= HOURS_BETWEEN_POINTS * 60 * 60 * 1000;
	}

	/**
	 * Adds points to the challenge points.
	 * 
	 * @param points the number of points to be added.
	 */
	public void addChallengePoints( int points ) {
		if ( points < 0 || this.canEarnPoints() ) {
			int setPoints = this.getChallengePoints() + points;

			// Clamp setPoints: [MAX_POINTS, MIN_POINTS].
			if ( setPoints >= MAX_POINTS ) {
				setPoints = MAX_POINTS;
				points = 0;
			} else if ( setPoints <= MIN_POINTS ) {
				setPoints = MIN_POINTS;
				points = 0;
			}

			this.setChallengePoints( setPoints, points > 0 );
		}
	}

	private void setChallengePoints( final int points, final boolean setLastTime ) {
		p.apply( new PreferenceEditCallback() {
			@Override
			public void editPreference( PreferenceNode p ) {
				p.setInt( "points", points );
		
				if ( setLastTime ) {
					p.setLong( "lastTimeEarned", lp.now() );
				}
			}
		} );
	}
}
