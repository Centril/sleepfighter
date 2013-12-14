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
		return delegate().clear();
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
	public PreferenceNode setBoolean( String key, boolean val ) {
		return delegate().setBoolean( key( key ), val );
	}

	@Override
	public PreferenceNode setChar( String key, char val ) {
		return delegate().setChar( key( key ), val );
	}

	@Override
	public PreferenceNode setShort( String key, short val ) {
		return delegate().setShort( key( key ), val );
	}

	@Override
	public PreferenceNode setInt( String key, int val ) {
		return delegate().setInt( key( key ), val );
	}

	@Override
	public PreferenceNode setLong( String key, long val ) {
		return delegate().setLong( key( key ), val );
	}

	@Override
	public PreferenceNode setFloat( String key, float val ) {
		return delegate().setFloat( key( key ), val );
	}

	@Override
	public PreferenceNode setDouble( String key, double val ) {
		return delegate().setDouble( key( key ), val );
	}

	@Override
	public PreferenceNode remove( String key ) {
		return delegate().remove( key( key ) );
	}

	@Override
	public PreferenceNode edit() {
		return delegate().edit();
	}

	@Override
	public boolean commit() {
		return delegate().commit();
	}

	@Override
	public PreferenceNode apply() {
		return delegate().apply();
	}

	@Override
	public boolean isAutoCommit() {
		return delegate().isAutoCommit();
	}
}
