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
package se.toxbee.sleepfighter.challenge;

import se.toxbee.sleepfighter.utils.message.MessageBusHolder;
import android.app.Activity;
import android.os.Bundle;

/**
 * Interface implemented by challenges.
 * 
 * It works by giving access to an empty {@link Activity} which should be
 * modified. The implementing class must at some point publish a
 * {@link ChallengeProgressEvent} with the type set as
 * {@link ChallengeProgressEvent.Type#COMPLETED} through the event bus that has
 * been set.
 */
public interface Challenge extends MessageBusHolder {
	/**
	 * <p>Called from the outside when the challenge is to be started from scratch.</p>
	 * 
	 * <p>Here should at least {@code setContentView()},<br/>
	 * or similar method in Activity, be called.</p>
	 * 
	 * @param activity the Activity that the Challenge modifies.
	 * @param params the resolved params (may have no params defined).
	 */
	public void start( Activity activity, ChallengeResolvedParams params );

	/**
	 * <p>Called from the outside when the challenge is to be started again.</p>
	 * 
	 * <p>Here should at least {@code setContentView()},<br/>
	 * or similar method in Activity, be called.</p>
	 * 
	 * @param activity the Activity that the Challenge modifies.
	 * @param params the resolved params (may have no params defined).
	 * @param state the state that was saved by Challenge before.
	 */
	public void start( Activity activity, ChallengeResolvedParams params, Bundle state );

	/**
	 * Called when challenge must save its state.
	 *
	 * @return a Bundle wherein the challenge's state should have been saved.
	 */
	public Bundle savedState();

	/**
	 * Called when the user leaves the activity, when it's no longer visible.
	 */
	public void onPause();

	/**
	 * Called when a users enters the activity, either the first time or when
	 * returning.
	 */
	public void onResume();

	/**
	 * Called when the activity is destroyed.
	 */
	public void onDestroy();
}