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

package se.toxbee.sleepfighter.utils.model;

import java.util.Locale;

/**
 * LocalizationProvider provides locale dependent information for keys.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Dec 13, 2013
 */
public interface LocalizationProvider {
	/**
	 * Returns the current UNIX epoch timestamp.
	 *
	 * @return the timestamp.
	 */
	public long now();

	/**
	 * Returns the locale used.
	 *
	 * @return the locale.
	 */
	public Locale locale();

	/**
	 * Returns the format for a given key.
	 *
	 * @param key the key.
	 * @return the format.
	 */
	public String format( Object key );
}
