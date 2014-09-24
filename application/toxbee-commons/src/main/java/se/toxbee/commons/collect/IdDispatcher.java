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

package se.toxbee.commons.collect;

import com.google.common.collect.Maps;

import java.util.Map;

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