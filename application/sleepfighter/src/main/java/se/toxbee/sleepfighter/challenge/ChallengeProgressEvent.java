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

import se.toxbee.sleepfighter.utils.message.Message;

/**
 * ChallengeProgressEvent occurs when a Challenge is:<br/>
 * completed or failed to be completed.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Oct 18, 2013
 */
public class ChallengeProgressEvent implements Message {
	/**
	 * Type enumerates the various progress event types there are.
	 *
	 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
	 * @version 1.0
	 * @since Oct 18, 2013
	 */
	public enum Type {
		COMPLETED, FAILED
	}

	private final Challenge challenge;
	private final Type type;
	private final Object extras;

	/**
	 * Constructs the event given a challenge, type and any extras.
	 *
	 * @param challenge the challenge that is firing the event.
	 * @param type the type of the event.
	 * @param extras any extra data available to provide.
	 */
	public ChallengeProgressEvent( Challenge challenge, Type type, Object extras ) {
		this.challenge = challenge;
		this.type = type;
		this.extras = extras;
	}

	/**
	 * Constructs the event given a challenge, type and no extras.
	 *
	 * @param challenge the challenge that is firing the event.
	 * @param type the type of the event.
	 */
	public ChallengeProgressEvent( Challenge challenge, Type type ) {
		this( challenge, type, null );
	}

	/**
	 * Returns the challenge that fired the event.
	 *
	 * @return the challenge.
	 */
	public Challenge getChallenge() {
		return this.challenge;
	}

	/**
	 * Returns the type of event.
	 *
	 * @return the type.
	 */
	public Type getType() {
		return this.type;
	}

	/**
	 * Returns any extra data, or null if not available.
	 *
	 * @return the provided extras.
	 */
	public Object getExtras() {
		return this.extras;
	}
}