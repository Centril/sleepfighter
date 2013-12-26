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
