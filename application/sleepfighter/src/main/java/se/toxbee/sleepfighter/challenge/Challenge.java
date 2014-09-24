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

package se.toxbee.sleepfighter.challenge;

import android.app.Activity;
import android.os.Bundle;

import se.toxbee.commons.message.MessageBusHolder;

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