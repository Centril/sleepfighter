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

/**
 * {@link ArbitraryOldSetter} provides setter for V<br/>
 * on a key-value data structure where the key is K.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Dec 15, 2013
 */
public interface ArbitraryOldSetter<K, V> {
	/**
	 * Sets a V value to key.
	 *
	 * @param key the key to set value for.
	 * @param val the value to set for given key.
	 * @return the old value or null if not previously set.
	 */
	public <U extends V> U set( K key, V val );
}
