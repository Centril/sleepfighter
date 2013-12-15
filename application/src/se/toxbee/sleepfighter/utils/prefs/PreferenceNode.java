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

import se.toxbee.sleepfighter.utils.model.ArbitraryDefaultGetter;
import se.toxbee.sleepfighter.utils.model.ArbitraryOldSetter;
import se.toxbee.sleepfighter.utils.model.PrimitiveDefaultGetters;
import se.toxbee.sleepfighter.utils.model.PrimitiveFluidSetters;
import se.toxbee.sleepfighter.utils.model.StringDefaultGetter;
import se.toxbee.sleepfighter.utils.model.StringFluidSetter;

/**
 * {@link PreferenceNode} provides an interface for preference managers.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Dec 14, 2013
 */
public interface PreferenceNode extends PrimitiveDefaultGetters<String>,
		PrimitiveFluidSetters<String, PreferenceNode>,
		StringDefaultGetter<String>, StringFluidSetter<String, PreferenceNode>,
		ArbitraryDefaultGetter<String, Serializable>, ArbitraryOldSetter<String, Serializable> {
	/**
	 * {@link PreferenceEditCallback}s can be passed to PreferenceNode 
	 *
	 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
	 * @version 1.0
	 * @since Dec 14, 2013
	 */
	public static interface PreferenceEditCallback {
		/**
		 * Performs an edit in a {@link PreferenceNode}.
		 *
		 * @param pref the {@link PreferenceNode}.
		 */
		public void editPreference( PreferenceNode pref );
	}

	/* --------------------------------
	 * Hierarchy logic.
	 * --------------------------------
	 */

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

	/* --------------------------------
	 * Abstract Accessor API.
	 * --------------------------------
	 */

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

	/**
	 * Removes the key.<br/>
	 * Equivalent of calling {@link #set(String, Serializable)} with null as value.
	 *
	 * @param key the key to remove.
	 */
	public PreferenceNode remove( String key );

	/**
	 * Clears all preferences.
	 */
	public PreferenceNode clear();

	/* --------------------------------
	 * Transaction logic.
	 * --------------------------------
	 */

	/**
	 * Begins a transaction and returns success/failure.
	 *
	 * @param cb the callback to run.
	 * @return the result, true if success.
	 */
	public boolean applyForResult( PreferenceEditCallback cb );

	/**
	 * Begins a transaction and ignores the result.<br/>
	 * Might run asynchronously depending on implementation.<br/>
	 * The in-memory changes are immediate.
	 *
	 * @param cb the callback to run.
	 * @return this.
	 */
	public PreferenceNode apply( PreferenceEditCallback cb );

	/**
	 * Returns true if the node is currently applying.
	 *
	 * @return true if applying.
	 */
	public boolean isApplying();
}
