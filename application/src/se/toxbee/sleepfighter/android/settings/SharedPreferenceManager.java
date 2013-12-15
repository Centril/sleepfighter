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

import java.io.Serializable;
import java.util.Map;

import se.toxbee.sleepfighter.utils.prefs.BasePreferenceManager;
import se.toxbee.sleepfighter.utils.prefs.PreferenceManager;
import se.toxbee.sleepfighter.utils.prefs.PreferenceNode;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * {@link SharedPreferenceNode} is an adapter for {@link SharedPreferences}.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Dec 14, 2013
 */
public class SharedPreferenceManager extends BasePreferenceManager {
	private final SharedPreferences prefs;
	private Editor edit;

	/**
	 * Constructs a SharedPreferenceNode.
	 *
	 * @param prefs the {@link SharedPreferences} to adapt to.
	 */
	public SharedPreferenceManager( SharedPreferences prefs ) {
		this.prefs = prefs;
		this.edit = null;
	}

	@Override
	public boolean contains( String key ) {
		return this.prefs.contains( key );
	}

	@Override
	public Map<String, ?> getAll() {
		return this.prefs.getAll();
	}

	@Override
	public boolean getBoolean( String key, boolean def ) {
		return this.prefs.getBoolean( key, def );
	}

	@Override
	public char getChar( String key, char def ) {
		return (char) this.prefs.getInt( key, def );
	}

	@Override
	public short getShort( String key, short def ) {
		return (short) this.prefs.getInt( key, def );
	}

	@Override
	public int getInt( String key, int def ) {
		return this.prefs.getInt( key, def );
	}

	@Override
	public long getLong( String key, long def ) {
		return this.prefs.getLong( key, def );
	}

	@Override
	public float getFloat( String key, float def ) {
		return this.prefs.getFloat( key, def );
	}

	@Override
	public double getDouble( String key, double def ) {
		long defL = Double.doubleToRawLongBits( def );
		return Double.longBitsToDouble( this.prefs.getLong( key, defL ) );
	}

	@Override
	public PreferenceNode setBoolean( String key, boolean val ) {
		return tryac( edit(), edit.putBoolean( key, val ) );
	}

	@Override
	public PreferenceNode setChar( String key, char val ) {
		return this.setInt( key, val );
	}

	@Override
	public PreferenceNode setShort( String key, short val ) {
		return this.setInt( key, val );
	}

	@Override
	public PreferenceNode setInt( String key, int val ) {
		return tryac( edit(), edit.putInt( key, val ) );
	}

	@Override
	public PreferenceNode setLong( String key, long val ) {
		return tryac( edit(), edit.putLong( key, val ) );
	}

	@Override
	public PreferenceNode setFloat( String key, float val ) {
		return tryac( edit(), edit.putFloat( key, val ) );
	}

	@Override
	public PreferenceNode setDouble( String key, double val ) {
		return this.setLong( key, Double.doubleToRawLongBits( val ) );
	}

	@Override
	public PreferenceNode remove( String key ) {
		return tryac( edit(), edit.remove( key ) );
	}

	@Override
	public PreferenceNode clear() {
		return tryac( edit(), edit.clear() );
	}

	@Override
	public boolean isApplying() {
		return this.edit != null;
	}

	private boolean edit() {
		boolean isNotApplying = this.edit == null;

		if ( isNotApplying ) {
			this.edit = this.prefs.edit();
		}

		return isNotApplying;
	}

	private PreferenceNode tryac( boolean autoCommit, Editor editor ) {
		if ( autoCommit && this.isApplying() ) {
			this.edit.apply();
		}

		return this;
	}

	@Override
	public PreferenceManager _apply( PreferenceNode node, PreferenceEditCallback cb ) {
		this.edit();

		cb.editPreference( node );

		this.edit.apply();
		return this;
	}

	@Override
	public boolean _applyForResult( PreferenceNode node, PreferenceEditCallback cb ) {
		this.edit();

		cb.editPreference( node );

		return this.edit.commit();
	}

	@Override
	public <U extends Serializable> U set( String key, U value ) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <U extends Serializable> U get( String key, U def ) {
		// TODO Auto-generated method stub
		return null;
	}
}
