/*******************************************************************************
 * Copyright (c) 2013 See AUTHORS file.
 * 
 * This file is part of SleepFighter.
 * 
 * SleepFighter is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * SleepFighter is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with SleepFighter. If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package se.toxbee.sleepfighter.utils.model;

/**
 * {@link PrimitiveFluidSetters} provides setters for all primitive<br/>
 * types on a key-value data structure where the key is K.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Dec 15, 2013
 */
public interface PrimitiveFluidSetters<K, R extends PrimitiveFluidSetters<K, R>> {
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
