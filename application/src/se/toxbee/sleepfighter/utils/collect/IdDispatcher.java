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
package se.toxbee.sleepfighter.utils.collect;

import java.util.Map;

import com.google.common.collect.Maps;

/**
 * {@link IdDispatcher} dispatches integer-IDs to a {@link Runnable}.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Dec 23, 2013
 */
public class IdDispatcher<T extends IdDispatcher<T>> {
	/** The id -> runnable entries. */
	protected Map<Integer, Runnable> entries = Maps.newHashMap();

	/**
	 * Binds id -> callback.
	 *
	 * @param id the id to bind for callback.
	 * @param callback the callback to bind to id.
	 * @return this.
	 */
	public IdDispatcher<T> bind( int id, Runnable callback ) {
		this.entries.put( id, callback );
		return this;
	}

	/**
	 * Dispatches the id to one of the bound runnable:s.
	 *
	 * @param id the id to dispatch.
	 * @return true if dispatched, otherwise false.
	 */
	public boolean dispatch( int id ) {
		Runnable r = this.entries.get( id );

		if( r == null ) {
			return false;
		}

		r.run();
		return true;
	}
}