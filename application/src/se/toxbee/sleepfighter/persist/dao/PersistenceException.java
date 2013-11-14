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
package se.toxbee.sleepfighter.persist.dao;

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
