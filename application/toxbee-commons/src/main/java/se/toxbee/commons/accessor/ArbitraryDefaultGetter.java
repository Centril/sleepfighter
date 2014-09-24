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

package se.toxbee.commons.accessor;

/**
 * {@link ArbitraryDefaultGetter} provides getter for V<br/>
 * on a key-value data structure where the key is K.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Dec 15, 2013
 */
public interface ArbitraryDefaultGetter<K, V> {
	/**
	 * Returns a V value for key.
	 *
	 * @param key the key to get value for.
	 * @param def the default value when there's no value for the given key.
	 * @return the value.
	 */
	public <U extends V> U get( K key, U def );
}
