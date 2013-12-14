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
package se.toxbee.sleepfighter.utils.prefs;

import java.io.Serializable;

/**
 * {@link SerializingPreferenceNode} is a {@link PreferenceNode}<br/>
 * that also allows for storing any type of {@link Serializable}.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Dec 14, 2013
 */
public interface SerializingPreferenceNode extends PreferenceNode {
	/**
	 * Sets a value to key.<br/>
	 * Passing null as value deletes the key.
	 *
	 * @param key the key to store value under.
	 * @param value the value to set to key.
	 * @return the old value or null if not previously set.
	 */
	public <U extends Serializable> U set( String key, U value );

	/**
	 * Returns a value for key or def if not set.
	 *
	 * @param key the key to get value for.
	 * @param def default value.
	 * @return the value.
	 */
	public <U extends Serializable> U get( String key, U def );

	public SerializingPreferenceNode sub( String ns );
	public SerializingPreferenceNode parent();
	public SerializingPreferenceNode remove( String key );
	public SerializingPreferenceNode setBoolean( String key, boolean val );
	public SerializingPreferenceNode setChar( String key, char val );
	public SerializingPreferenceNode setShort( String key, short val );
	public SerializingPreferenceNode setInt( String key, int val );
	public SerializingPreferenceNode setLong( String key, long val );
	public SerializingPreferenceNode setFloat( String key, float val );
	public SerializingPreferenceNode setDouble( String key, double val );
	public SerializingPreferenceNode edit();
	public SerializingPreferenceNode clear();
	public SerializingPreferenceNode apply();
}
