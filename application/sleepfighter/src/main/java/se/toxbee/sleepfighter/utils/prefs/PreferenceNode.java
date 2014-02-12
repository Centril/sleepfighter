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

package se.toxbee.sleepfighter.utils.prefs;

import java.io.Serializable;
import java.util.Map;

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
		StringDefaultGetter<String>, StringFluidSetter<String, PreferenceNode> {
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
	 * Returns a sub node one level deeper with the namespace ns.<br/>
	 * If ns == null, this node will be returned.
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
