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
import java.util.Map;

/**
 * {@link PreferenceNode} provides an interface for preference managers.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Dec 14, 2013
 */
public interface PreferenceNode {
	/**
	 * Returns a sub node one level deeper with the namespace ns.
	 *
	 * @param ns the namespace of the sub node.
	 * @return the sub node.
	 */
	public PreferenceNode sub( String ns );

	/**
	 * Returns the parent node of this node or itself if the current node is root.
	 *
	 * @return the parent node.
	 */
	public PreferenceNode parent();

	/**
	 * Returns true if preference with key exists in manager.
	 *
	 * @param key the key to test for.
	 * @return true if it existed.
	 */
	public boolean contains( String key );

	/**
	 * Returns all key-value pairs as a map.<br/>
	 * Data consistency is not guaranteed when the map is modified.
	 *
	 * @return the map.
	 */
	public Map<String, ?> getAll();

	// All primitive types:

	public boolean getBoolean( String key, boolean def );
	public char getChar( String key, char def );
	public short getShort( String key, short def );
	public int getInt( String key, int def );
	public long getLong( String key, long def );
	public float getFloat( String key, float def );
	public double getDouble( String key, double def );

	public PreferenceNode setBoolean( String key, boolean val );
	public PreferenceNode setChar( String key, char val );
	public PreferenceNode setShort( String key, short val );
	public PreferenceNode setInt( String key, int val );
	public PreferenceNode setLong( String key, long val );
	public PreferenceNode setFloat( String key, float val );
	public PreferenceNode setDouble( String key, double val );

	/**
	 * Removes the key.<br/>
	 * Equivalent of calling {@link #set(String, Serializable)} with null as value.
	 *
	 * @param key the key to remove.
	 */
	public PreferenceNode remove( String key );

	/**
	 * Begins a transaction which may be
	 * completed with {@link #commit()} or {@link #apply()}.
	 */
	public PreferenceNode edit();

	/**
	 * Clears all preferences.
	 */
	public PreferenceNode clear();

	/**
	 * Commits all the changes that were made.
	 *
	 * @return true if the changes were successful.
	 */
	public boolean commit();

	/**
	 * The same as {@link #commit()} but might depending on implementation run asynchronously.
	 * The in-memory changes will be immediate.
	 */
	public PreferenceNode apply();

	/**
	 * Returns whether or not the node is in auto-commit mode.<br/>
	 * When in auto-commit mode, {@link #commit()} will always return true,<br/>
	 * and all non-accessors will be atomic operations.
	 *
	 * @return true if in auto-commit mode.
	 */
	public boolean isAutoCommit();
}
