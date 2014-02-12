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

package se.toxbee.sleepfighter.preference;

import se.toxbee.sleepfighter.utils.prefs.PreferenceNode;

/**
 * {@link AppPreferenceNode} is the base node for all app preference nodes.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Dec 15, 2013
 */
public class AppPreferenceNode {
	protected final PreferenceNode p;

	public AppPreferenceNode( PreferenceNode backend, String ns ) {
		this.p = backend.sub( ns );
	}

	public final PreferenceNode backend() {
		return this.p;
	}
}
