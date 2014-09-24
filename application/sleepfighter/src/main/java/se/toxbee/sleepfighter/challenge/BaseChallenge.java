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

import se.toxbee.sleepfighter.challenge.ChallengeProgressEvent.Type;
import se.toxbee.commons.message.Message;
import se.toxbee.commons.message.MessageBus;

/**
 * BaseChallenge is the base class for all challenges.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Oct 18, 2013
 */
public abstract class BaseChallenge implements Challenge {
	private MessageBus<Message> bus;
	private Activity activity;
	private ChallengeResolvedParams params;

	/**
	 * {@inheritDoc}<br/>
	 * This method must be called in both<br/>
	 * {@link #start(Activity, ChallengeResolvedParams)} and<br/>
	 * {@link #start(Activity, ChallengeResolvedParams, android.os.Bundle)} of the subclass.
	 */
	public void start( Activity activity, ChallengeResolvedParams params ) {
		this.activity = activity;
		this.params = params;
	}

	@Override
	public void onPause() {
	}

	@Override
	public void onResume() {
	}

	@Override
	public void onDestroy() {
	}

	public void setMessageBus( MessageBus<Message> bus ) {
		this.bus = bus;
	}

	public MessageBus<Message> getMessageBus() {
		return this.bus;
	}

	/**
	 * Returns the activity the challenge is operating on.
	 *
	 * @return the activity.
	 */
	protected Activity activity() {
		return this.activity;
	}

	/**
	 * Returns the resolved parameters for the challenge.
	 *
	 * @return the params.
	 */
	protected ChallengeResolvedParams params() {
		return this.params;
	}

	/**
	 * Publishes a {@link Type#COMPLETED} to bus.
	 */
	protected void complete() {
		this.publish( new ChallengeProgressEvent( this, Type.COMPLETED ) );
	}

	/**
	 * Publishes a {@link Type#FAILED} to bus.
	 */
	protected void fail() {
		this.publish( new ChallengeProgressEvent( this, Type.FAILED ) );
	}

	/**
	 * Publishes an event to event bus.
	 *
	 * @param event the event to publish.
	 */
	protected void publish( ChallengeProgressEvent event ) {
		if ( this.bus == null ) {
			return;
		}

		this.bus.publish( event );
	}
}