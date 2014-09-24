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
 * {@link PrimitiveFluidSetters} provides setters for all primitive<br/>
 * types on a key-value data structure where the key is K.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Dec 15, 2013
 */
public interface PrimitiveSetters<K, R extends PrimitiveFluidSetters<K, R>> {
	/**
	 * Sets a boolean value to key.
	 *
	 * @param key the key to set value for.
	 * @param val the value to set for given key.
	 * @return this.
	 */
	public R setBoolean( K key, boolean val );

	/**
	 * Sets a char value to key.
	 *
	 * @param key the key to set value for.
	 * @param val the value to set for given key.
	 * @return this.
	 */
	public R setChar( K key, char val );

	/**
	 * Sets a short value to key.
	 *
	 * @param key the key to set value for.
	 * @param val the value to set for given key.
	 * @return this.
	 */
	public R setShort( K key, short val );

	/**
	 * Sets a integer value to key.
	 *
	 * @param key the key to set value for.
	 * @param val the value to set for given key.
	 * @return this.
	 */
	public R setInt( K key, int val );

	/**
	 * Sets a long value to key.
	 *
	 * @param key the key to set value for.
	 * @param val the value to set for given key.
	 * @return this.
	 */
	public R setLong( K key, long val );

	/**
	 * Sets a float value to key.
	 *
	 * @param key the key to set value for.
	 * @param val the value to set for given key.
	 * @return this.
	 */
	public R setFloat( K key, float val );

	/**
	 * Sets a double value to key.
	 *
	 * @param key the key to set value for.
	 * @param val the value to set for given key.
	 * @return this.
	 */
	public R setDouble( K key, double val );
}
