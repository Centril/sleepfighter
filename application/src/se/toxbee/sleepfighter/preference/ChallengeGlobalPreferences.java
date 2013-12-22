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

import se.toxbee.sleepfighter.utils.model.LocalizationProvider;
import se.toxbee.sleepfighter.utils.prefs.PreferenceNode;
import se.toxbee.sleepfighter.utils.prefs.PreferenceNode.PreferenceEditCallback;

/**
 * {@link ChallengeGlobalPreferences} handles global challenge related preferences.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Dec 15, 2013
 */
public class ChallengeGlobalPreferences extends AppPreferenceNode {
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
