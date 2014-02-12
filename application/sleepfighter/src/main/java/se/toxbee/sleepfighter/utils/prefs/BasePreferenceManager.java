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

package se.toxbee.sleepfighter.utils.prefs;

/**
 * {@link BasePreferenceManager} is the base class for all {@link PreferenceManager}s.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Dec 15, 2013
 */
public abstract class BasePreferenceManager implements PreferenceManager {
	@Override
	public PreferenceNode apply( PreferenceEditCallback cb ) {
		return this._apply( this, cb );
	}

	@Override
	public boolean applyForResult( PreferenceEditCallback cb ) {
		return this._applyForResult( this, cb );
	}

	@Override
	public PreferenceNode sub( String ns ) {
		return ns == null ? this : new ChildPreferenceNode( this, ns );
	}

	@Override
	public PreferenceNode parent() {
		return this;
	}
}