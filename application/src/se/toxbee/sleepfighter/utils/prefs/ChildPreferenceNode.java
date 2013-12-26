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

import java.util.Map;

import com.google.common.base.Predicate;
import com.google.common.collect.Maps;

/**
 * {@link ChildPreferenceNode} is the base class for all child/sub {@link PreferenceNode}s.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Dec 14, 2013
 */
public class ChildPreferenceNode extends ForwardingPreferenceNode {
	protected final String namespace;
	protected final PreferenceManager root;

	/**
	 * Constructs the ChildPreferenceNode given the root and the child's namespace.
	 *
	 * @param root the root node.
	 * @param ns the namespace of child.
	 */
	public ChildPreferenceNode( PreferenceManager root, String ns ) {
		this.root = root;
		this.namespace = this.makeNSString( ns );
	}

	protected PreferenceNode makeSubNode( String ns ) {
		return new ChildPreferenceNode( this.root, ns );
	}

	/**
	 * Creates the NS string at construction.<br/>
	 * E.g: transforms "myapp" to "myapp."
	 *
	 * @param ns the namespace.
	 * @return the namespace-string for use.
	 */
	protected String makeNSString( String ns ) {
		return ns + ".";
	}

	/**
	 * Reduces the namespace to its parent.
	 *
	 * @return the namespace.
	 */
	protected String parentNSString() {
		int idx = this.namespace.lastIndexOf( this.namespace );
		return this.namespace.substring( 0, idx );
	}

	@Override
	protected String key( String key ) {
		return this.namespace + key;
	}

	@Override
	protected PreferenceManager delegate() {
		return this.root;
	}

	@Override
	public PreferenceNode sub( String ns ) {
		return ns == null ? this : this.makeSubNode( key( ns ) );
	}

	@Override
	public PreferenceNode parent() {
		String parentNs = this.parentNSString();
		return parentNs.isEmpty() ? this.root : this.makeSubNode( parentNs );
	}

	@Override
	public Map<String, ?> getAll() {
		return Maps.newHashMap( this.liveMap() );
	}

	protected Map<String, ?> liveMap() {
		// Live map of all entries with keys starting with namespace.
		return Maps.filterKeys( super.getAll(), new Predicate<String>() {
			@Override
			public boolean apply( String key ) {
				return key.startsWith( namespace );
			}
		} );
	}

	@Override
	public PreferenceNode clear() {
		for ( String key : this.liveMap().keySet() ) {
			this.remove( key );
		}
		return this;
	}

	@Override
	public boolean applyForResult( PreferenceEditCallback cb ) {
		return delegate()._applyForResult( this, cb );
	}

	@Override
	public PreferenceNode apply( PreferenceEditCallback cb ) {
		delegate()._apply( this, cb );
		return this;
	}
}
