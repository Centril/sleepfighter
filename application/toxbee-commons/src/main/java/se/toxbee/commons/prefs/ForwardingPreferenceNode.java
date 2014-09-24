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

package se.toxbee.commons.prefs;

import java.io.Serializable;
import java.util.Map;

/**
 * {@link ForwardingPreferenceNode} is a guava-style forwarding {@link PreferenceNode}.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Dec 14, 2013
 */
public abstract class ForwardingPreferenceNode implements PreferenceNode {
	/**
	 * Returns the {@link PreferenceNode} delegated to.
	 *
	 * @return the node.
	 */
	protected abstract PreferenceNode delegate();

	/**
	 * Returns the processed key, this is an opportunity to add namespace, etc.
	 *
	 * @param key the key.
	 * @return the processed key.
	 */
	protected String key( String key ) {
		return key;
	}

	@Override
	public PreferenceNode parent() {
		return delegate().parent();
	}

	@Override
	public Map<String, ?> getAll() {
		return delegate().getAll();
	}

	@Override
	public PreferenceNode clear() {
		delegate().clear();
		return this;
	}

	@Override
	public PreferenceNode sub( String ns ) {
		return delegate().sub( ns );
	}

	@Override
	public boolean contains( String key ) {
		return delegate().contains( key( key ) );
	}

	@Override
	public PreferenceNode remove( String key ) {
		delegate().remove( key( key ) );
		return this;
	}

	@Override
	public boolean applyForResult( PreferenceEditCallback cb ) {
		return delegate().applyForResult( cb );
	}

	@Override
	public PreferenceNode apply( PreferenceEditCallback cb ) {
		delegate().apply( cb );
		return this;
	}

	@Override
	public boolean isApplying() {
		return delegate().isApplying();
	}

	@Override
	public boolean getBoolean( String key, boolean def ) {
		return delegate().getBoolean( key( key ), def );
	}

	@Override
	public char getChar( String key, char def ) {
		return delegate().getChar( key( key ), def );
	}

	@Override
	public short getShort( String key, short def ) {
		return delegate().getShort( key( key ), def );
	}

	@Override
	public int getInt( String key, int def ) {
		return delegate().getInt( key( key ), def );
	}

	@Override
	public long getLong( String key, long def ) {
		return delegate().getLong( key( key ), def );
	}

	@Override
	public float getFloat( String key, float def ) {
		return delegate().getFloat( key( key ), def );
	}

	@Override
	public double getDouble( String key, double def ) {
		return delegate().getDouble( key( key ), def );
	}

	@Override
	public String getString( String key, String def ) {
		return delegate().getString( key( key ), def );
	}

	@Override
	public <U extends Serializable> U get( String key, U def ) {
		return delegate().get( key( key ), def );
	}

	@Override
	public PreferenceNode setBoolean( String key, boolean val ) {
		delegate().setBoolean( key( key ), val );
		return this;
	}

	@Override
	public PreferenceNode setChar( String key, char val ) {
		delegate().setChar( key( key ), val );
		return this;
	}

	@Override
	public PreferenceNode setShort( String key, short val ) {
		delegate().setShort( key( key ), val );
		return this;
	}

	@Override
	public PreferenceNode setInt( String key, int val ) {
		delegate().setInt( key( key ), val );
		return this;
	}

	@Override
	public PreferenceNode setLong( String key, long val ) {
		delegate().setLong( key( key ), val );
		return this;
	}

	@Override
	public PreferenceNode setFloat( String key, float val ) {
		delegate().setFloat( key( key ), val );
		return this;
	}

	@Override
	public PreferenceNode setDouble( String key, double val ) {
		delegate().setDouble( key( key ), val );
		return this;
	}

	@Override
	public PreferenceNode setString( String key, String val ) {
		delegate().setString( key( key ), val );
		return this;
	}


	@Override
	public <U extends Serializable> U set( String key, U val ) {
		return delegate().set( key( key ), val );
	}
}
