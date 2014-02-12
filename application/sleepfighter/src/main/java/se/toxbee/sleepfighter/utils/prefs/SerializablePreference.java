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

import java.io.Serializable;

/**
 * {@link SerializablePreference} is the basic interface of all serializable
 * preferences.
 * 
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Dec 14, 2013
 */
public interface SerializablePreference {
	/**
	 * Returns the key of the preference.
	 * 
	 * @return the key.
	 */
	public String key();

	/**
	 * Returns the serializable value.
	 * 
	 * @return the value.
	 */
	public Serializable value();
}
