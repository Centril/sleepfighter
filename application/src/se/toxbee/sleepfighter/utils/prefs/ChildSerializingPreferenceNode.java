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

/**
 * {@link ChildSerializingPreferenceNode} is the {@link ChildPreferenceNode} for {@link SerializingPreferenceNode}.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Dec 14, 2013
 */
public class ChildSerializingPreferenceNode extends ChildPreferenceNode implements SerializingPreferenceNode {
	protected ChildSerializingPreferenceNode( SerializingPreferenceNode root, String ns ) {
		super( root, ns );
	}

	@Override
	protected PreferenceNode makeSubNode( PreferenceNode root, String ns ) {
		return new ChildSerializingPreferenceNode( (SerializingPreferenceNode) root, ns );
	}

	public SerializingPreferenceNode sub( String ns ) {
		return (SerializingPreferenceNode)super.sub( ns );
	}

	public SerializingPreferenceNode parent() {
		return (SerializingPreferenceNode)super.parent();
	}

	public SerializingPreferenceNode remove( String key ) {
		return (SerializingPreferenceNode)super.remove( key );
	}

	public SerializingPreferenceNode setBoolean( String key, boolean val ) {
		return (SerializingPreferenceNode)super.setBoolean( key, val );
	}

	public SerializingPreferenceNode setChar( String key, char val ) {
		return (SerializingPreferenceNode)super.setChar( key, val );
	}

	public SerializingPreferenceNode setShort( String key, short val ) {
		return (SerializingPreferenceNode)super.setShort( key, val );
	}

	public SerializingPreferenceNode setInt( String key, int val ) {
		return (SerializingPreferenceNode)super.setInt( key, val );
	}

	public SerializingPreferenceNode setLong( String key, long val ) {
		return (SerializingPreferenceNode)super.setLong( key, val );
	}

	public SerializingPreferenceNode setFloat( String key, float val ) {
		return (SerializingPreferenceNode)super.setFloat( key, val );
	}

	public SerializingPreferenceNode setDouble( String key, double val ) {
		return (SerializingPreferenceNode)super.setDouble( key, val );
	}

	public SerializingPreferenceNode edit() {
		return (SerializingPreferenceNode)super.edit();
	}

	public SerializingPreferenceNode clear() {
		return (SerializingPreferenceNode)super.clear();
	}

	public SerializingPreferenceNode apply() {
		return (SerializingPreferenceNode)super.apply();
	}

	@Override
	public <U extends Serializable> U set( String key, U value ) {
		return ((SerializingPreferenceNode)delegate()).set( key( key ), value );
	}

	@Override
	public <U extends Serializable> U get( String key, U def ) {
		return ((SerializingPreferenceNode)delegate()).set( key( key ), def );
	}
}