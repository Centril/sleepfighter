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

import com.google.common.base.Preconditions;

/**
 * {@link TopSerializingPreferenceNode} is the top node for a {@link SerializingPreferenceNode}.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Dec 14, 2013
 */
public abstract class TopSerializingPreferenceNode extends BaseSerializingPreferenceNode {
	/**
	 * Returns the in-memory map of entities.
	 *
	 * @return the map.
	 */
	protected abstract Map<String, Serializable> memory();

	/**
	 * Sets the key-value pair in backend.
	 *
	 * @param key the key.
	 * @param value the value.
	 */
	protected abstract <U extends Serializable> void backendSet( String key, U value );

	/**
	 * Removes the key-value pair in backend.
	 *
	 * @param key the key.
	 */
	protected abstract <U extends Serializable> void backendRemove( String key );

	/**
	 * Loads the given preference into the in-memory map given by {@link #memory()}.
	 *
	 * @param prefs the preferences to load.
	 * @return this.
	 */
	protected TopSerializingPreferenceNode load( Iterable<? extends SerializablePreference> prefs ) {
		for ( SerializablePreference p : prefs ) {
			this.memory().put( p.key(), p.value() );
		}

		return this;
	}

	@Override
	public boolean contains( String key ) {
		return this.memory().containsKey( key );
	}

	@Override
	public Map<String, ?> getAll() {
		return this.memory();
	}

	public <U extends Serializable> U set( String key, U value ) {
		@SuppressWarnings( "unchecked" )
		U old = (U) this.memory().put( Preconditions.checkNotNull( key ), value );

		// Write to persistence if changed.
		if ( old != value ) {
			if ( value == null ) {
				this.backendRemove( key );
			} else {
				this.backendSet( key, value );
			}
		}

		return old;
	}

	public <U extends Serializable> U get( String key, U def ) {
		@SuppressWarnings( "unchecked" )
		U val = (U) this.memory().get( key );
		return val == null ? def : val;
	}

	@Override
	public SerializingPreferenceNode sub( String ns ) {
		return new ChildSerializingPreferenceNode( this, ns );
	}

	@Override
	public SerializingPreferenceNode parent() {
		return this;
	}
}
