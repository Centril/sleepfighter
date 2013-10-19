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
package se.chalmers.dat255.sleepfighter.challenge;

import se.chalmers.dat255.sleepfighter.challenge.ChallengeProgressEvent.Type;
import se.chalmers.dat255.sleepfighter.utils.message.Message;
import se.chalmers.dat255.sleepfighter.utils.message.MessageBus;
import android.app.Activity;

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