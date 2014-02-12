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

package se.toxbee.sleepfighter.utils.collect;

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
