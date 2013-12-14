/*******************************************************************************
 * Copyright (c) 2013 See AUTHORS file. This file is part of SleepFighter.
 * SleepFighter is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version. SleepFighter is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details. You should have received a copy of the GNU General Public License
 * along with SleepFighter. If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package se.toxbee.sleepfighter.utils.prefs;

import java.io.Serializable;

/**
 * {@link BaseSerializingPreferenceNode} implements basic stuff in {@link SerializingPreferenceNode}.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Dec 14, 2013
 */
public abstract class BaseSerializingPreferenceNode implements SerializingPreferenceNode {
	@Override
	public boolean getBoolean( String key, boolean def ) {
		return this.get( key, def );
	}

	@Override
	public char getChar( String key, char def ) {
		return this.get( key, def );
	}

	@Override
	public short getShort( String key, short def ) {
		return this.get( key, def );
	}

	@Override
	public int getInt( String key, int def ) {
		return this.get( key, def );
	}

	@Override
	public long getLong( String key, long def ) {
		return this.get( key, def );
	}

	@Override
	public float getFloat( String key, float def ) {
		return this.get( key, def );
	}

	@Override
	public double getDouble( String key, double def ) {
		return this.get( key, def );
	}

	private <U extends Serializable> SerializingPreferenceNode setc( String key, U value ) {
		this.set( key, value );
		return this;
	}

	@Override
	public SerializingPreferenceNode setBoolean( String key, boolean val ) {
		return this.setc( key, val );
	}

	@Override
	public SerializingPreferenceNode setChar( String key, char val ) {
		return this.setc( key, val );
	}

	@Override
	public SerializingPreferenceNode setShort( String key, short val ) {
		return this.setc( key, val );
	}

	@Override
	public SerializingPreferenceNode setInt( String key, int val ) {
		return this.setc( key, val );
	}

	@Override
	public SerializingPreferenceNode setLong( String key, long val ) {
		return this.setc( key, val );
	}

	@Override
	public SerializingPreferenceNode setFloat( String key, float val ) {
		return this.setc( key, val );
	}

	@Override
	public SerializingPreferenceNode setDouble( String key, double val ) {
		return this.setc( key, val );
	}

	@Override
	public SerializingPreferenceNode remove( String key ) {
		return this.setc( key, null );
	}
}