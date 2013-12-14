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
package se.toxbee.sleepfighter.android.settings;

import java.util.Map;

import se.toxbee.sleepfighter.utils.prefs.PreferenceNode;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.google.common.base.Predicate;
import com.google.common.collect.Maps;

/**
 * {@link SharedPreferenceNode} is an adapter for {@link SharedPreferences}.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Dec 14, 2013
 */
public class SharedPreferenceNode implements PreferenceNode {
	private final SharedPreferences prefs;
	private Editor edit;

	/**
	 * Constructs a SharedPreferenceNode.
	 *
	 * @param prefs the {@link SharedPreferences} to adapt to.
	 */
	public SharedPreferenceNode( SharedPreferences prefs ) {
		this( prefs, null );
	}

	protected SharedPreferenceNode( SharedPreferenceNode n ) {
		this( n.prefs, n.edit );
	}

	protected SharedPreferenceNode( SharedPreferences prefs, Editor edit ) {
		this.prefs = prefs;
		this.edit = edit;
	}

	protected String ns( String key ) {
		return key;
	}

	@Override
	public PreferenceNode sub( String ns ) {
		return new Sub( this, this.ns( ns ) );
	}

	@Override
	public PreferenceNode parent() {
		return this;
	}

	@Override
	public boolean contains( String key ) {
		return this.prefs.contains( ns( key ) );
	}

	@Override
	public Map<String, ?> getAll() {
		return this.prefs.getAll();
	}

	@Override
	public boolean getBoolean( String key, boolean def ) {
		return this.prefs.getBoolean( ns( key ), def );
	}

	@Override
	public char getChar( String key, char def ) {
		return (char) this.prefs.getInt( ns( key ), def );
	}

	@Override
	public short getShort( String key, short def ) {
		return (short) this.prefs.getInt( ns( key ), def );
	}

	@Override
	public int getInt( String key, int def ) {
		return this.prefs.getInt( ns( key ), def );
	}

	@Override
	public long getLong( String key, long def ) {
		return this.prefs.getLong( ns( key ), def );
	}

	@Override
	public float getFloat( String key, float def ) {
		return this.prefs.getFloat( ns( key ), def );
	}

	@Override
	public double getDouble( String key, double def ) {
		long defL = Double.doubleToRawLongBits( def );
		return Double.longBitsToDouble( this.prefs.getLong( ns( key ), defL ) );
	}

	@Override
	public PreferenceNode setBoolean( String key, boolean val ) {
		this.edit();
		this.edit.putBoolean( ns( key ), val );
		return this;
	}

	@Override
	public PreferenceNode setChar( String key, char val ) {
		return this.setInt( ns( key ), val );
	}

	@Override
	public PreferenceNode setShort( String key, short val ) {
		return this.setInt( ns( key ), val );
	}

	@Override
	public PreferenceNode setInt( String key, int val ) {
		this.edit();
		this.edit.putInt( ns( key ), val );
		return this;
	}

	@Override
	public PreferenceNode setLong( String key, long val ) {
		this.edit();
		this.edit.putLong( ns( key ), val );
		return this;
	}

	@Override
	public PreferenceNode setFloat( String key, float val ) {
		this.edit();
		this.edit.putFloat( ns( key ), val );
		return this;
	}

	@Override
	public PreferenceNode setDouble( String key, double val ) {
		return this.setLong( ns( key ), Double.doubleToRawLongBits( val ) );
	}

	@Override
	public PreferenceNode remove( String key ) {
		this.edit();
		this.edit.remove( ns( key ) );
		return this;
	}

	@Override
	public PreferenceNode edit() {
		if ( this.edit == null ) {
			this.edit = this.prefs.edit();
		}

		return this;
	}

	@Override
	public PreferenceNode clear() {
		this.edit();
		this.edit.clear();
		return this;
	}

	@Override
	public boolean commit() {
		boolean r = false;

		if ( this.edit != null ) {
			r = this.edit.commit();
			this.edit = null;
		}

		return r;
	}

	@Override
	public PreferenceNode apply() {
		if ( this.edit != null ) {
			this.edit.apply();
			this.edit = null;
		}

		return this;
	}

	protected static class Sub extends SharedPreferenceNode {
		private final String namespace;

		protected Sub( SharedPreferenceNode n, String ns ) {
			super( n );
			this.namespace = ns + '.';
		}

		@Override
		protected String ns( String key ) {
			return this.namespace + key;
		}

		@Override
		public PreferenceNode parent() {
			int idx = this.namespace.lastIndexOf( this.namespace );
			String parentNs = this.namespace.substring( 0, idx );
			return parentNs.isEmpty() ? new SharedPreferenceNode( this ) : new Sub( this, parentNs );
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
	}
}
