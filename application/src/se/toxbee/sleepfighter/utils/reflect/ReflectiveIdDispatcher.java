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
package se.toxbee.sleepfighter.utils.reflect;

import se.toxbee.sleepfighter.utils.collect.IdDispatcher;

/**
 * {@link ReflectiveIdDispatcher} adds reflection utilities on top of {@link IdDispatcher}
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Dec 23, 2013
 */
public class ReflectiveIdDispatcher<R, T extends ReflectiveIdDispatcher<R, T>> extends IdDispatcher<T> {
	public ReflectiveIdDispatcher( R receiver ) {
		this.receiver = receiver;
	}

	/** The receiving object to call methods on in case of reflection. */
	protected R receiver;

	/**
	 * Binds id -> callback.<br/>
	 * See {@link ReflectiveRunnable#ReflectiveRunnable(Object, String, Object...)}
	 *
	 * @param id the id to bind for callback.
	 * @param name
	 * @param a
	 * @return this.
	 */
	public IdDispatcher<T> bind( int id, String name, Object... a ) {
		return bind( id, new ReflectiveRunnable( this.receiver, name, a ) );
	}
}
