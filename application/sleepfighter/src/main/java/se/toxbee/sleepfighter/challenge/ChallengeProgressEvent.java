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