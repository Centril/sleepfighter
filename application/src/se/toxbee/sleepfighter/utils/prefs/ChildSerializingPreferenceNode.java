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

import com.google.common.base.Predicate;
import com.google.common.collect.Maps;

/**
 * {@link ChildSerializingPreferenceNode} handles namespace hierarchy for {@link SerializingPreferenceNode}.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Dec 14, 2013
 */
public class ChildSerializingPreferenceNode extends BaseSerializingPreferenceNode {
	private final SerializingPreferenceNode m;
	private final String namespace;

	public ChildSerializingPreferenceNode( SerializingPreferenceNode m, String ns ) {
		this.m = m;
		this.namespace = ns;
	}

	private String ns( String key ) {
		return this.namespace + key;
	}

	@Override
	public SerializingPreferenceNode sub( String ns ) {
		return new ChildSerializingPreferenceNode( m, this.namespace + '.' + ns );
	}

	@Override
	public SerializingPreferenceNode parent() {
		int idx = this.namespace.lastIndexOf( this.namespace );
		String parentNs = this.namespace.substring( 0, idx );
		return parentNs.isEmpty() ? m : new ChildSerializingPreferenceNode( m, parentNs );
	}

	@Override
	public <U extends Serializable> U set( String key, U value ) {
		return m.set( ns( key ), value );
	}

	@Override
	public <U extends Serializable> U get( String key, U def ) {
		return m.get( ns( key ), def );
	}

	@Override
	public boolean contains( String key ) {
		return m.contains( ns( key ) );
	}

	@Override
	public Map<String, ?> getAll() {
		return Maps.newHashMap( this.liveMap() );
	}

	protected Map<String, ?> liveMap() {
		// Live map of all entries with keys starting with namespace.
		return Maps.filterKeys( m.getAll(), new Predicate<String>() {
			@Override
			public boolean apply( String key ) {
				return key.startsWith( namespace );
			}
		} );
	}

	@Override
	public SerializingPreferenceNode clear() {
		for ( String key : this.liveMap().keySet() ) {
			this.remove( key );
		}
		return this;
	}

	@Override
	public SerializingPreferenceNode edit() {
		return m.edit();
	}

	@Override
	public SerializingPreferenceNode apply() {
		return m.apply();
	}

	@Override
	public boolean commit() {
		return m.commit();
	}
}