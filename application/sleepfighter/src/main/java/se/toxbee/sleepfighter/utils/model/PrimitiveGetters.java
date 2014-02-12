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
 * {@link PrimitiveGetters} provides getters for all primitive<br/>
 * types on a key-value data structure where the key is K.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Dec 15, 2013
 */
public interface PrimitiveGetters<K> {
	/**
	 * Returns a boolean value for key.
	 *
	 * @param key the key to get value for.
	 * @return the value.
	 */
	public boolean getBoolean( K key );

	/**
	 * Returns a char value for key.
	 *
	 * @param key the key to get value for.
	 * @return the value.
	 */
	public char getChar( K key );

	/**
	 * Returns a short value for key.
	 *
	 * @param key the key to get value for.
	 * @return the value.
	 */
	public short getShort( K key );

	/**
	 * Returns a integer value for key.
	 *
	 * @param key the key to get value for.
	 * @return the value.
	 */
	public int getInt( K key );

	/**
	 * Returns a long value for key.
	 *
	 * @param key the key to get value for.
	 * @return the value.
	 */
	public long getLong( K key );

	/**
	 * Returns a float value for key.
	 *
	 * @param key the key to get value for.
	 * @return the value.
	 */
	public float getFloat( K key );

	/**
	 * Returns a double value for key.
	 *
	 * @param key the key to get value for.
	 * @return the value.
	 */
	public double getDouble( K key );
}
