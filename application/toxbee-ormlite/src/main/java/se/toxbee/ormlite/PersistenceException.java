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

package se.toxbee.ormlite;

/**
 * <p>PersitenceError indicates an error related to persistence.<br/>
 * This is a serious, but can be recovered from.</p>
 *
 * <p>While this is a RuntimeException, it should be checked for and handled.</p>
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Sep 22, 2013
 */
public class PersistenceException extends RuntimeException {
	private static final long serialVersionUID = -5914772855383078239L;

	/**
	 * Constructs a PersitenceException with detailed message.<br/>
	 * This should not be used under normal circumstances,<br/>
	 * rather, a Throwable should be passed to constructor.
	 *
	 * @param detailMessage the message to pass.
	 */
	public PersistenceException( String detailMessage ) {
		super( detailMessage );
	}

	/**
	 * Wraps a throwable in a PersitenceException.
	 *
	 * @param throwable the throwable to wrap in.
	 */
	public PersistenceException( Throwable throwable ) {
		super( throwable );
	}

	/**
	 * Wraps a throwable in a PersitenceException and adds a detailed message.
	 *
	 * @param detailMessage the message to pass.
	 * @param throwable the throwable to wrap in.
	 */
	public PersistenceException( String detailMessage, Throwable throwable ) {
		super( detailMessage, throwable );
	}
}
