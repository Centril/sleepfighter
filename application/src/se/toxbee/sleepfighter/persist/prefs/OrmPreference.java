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
package se.toxbee.sleepfighter.persist.prefs;

import java.io.Serializable;

import se.toxbee.sleepfighter.utils.prefs.SerializablePreference;

import com.j256.ormlite.field.DatabaseField;

/**
 * OrmPreference is the base {@link SerializablePreference} class for {@link OrmPreferenceManager}.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Dec 15, 2013
 */
public abstract class OrmPreference implements SerializablePreference {
	/**
	 * No-arg constructor.
	 */
	public OrmPreference() {	
	}

	/**
	 * Constructs the preference with key and value.
	 *
	 * @param key the key to use.
	 * @param value the value for key.
	 */
	public OrmPreference( String key, Serializable value ) {
		this.key = key;
		this.value = value;
	}

	@DatabaseField(id = true)
	protected String key;

	@DatabaseField
	protected Serializable value;

	@Override
	public String key() {
		return this.key;
	}

	@Override
	public Serializable value() {
		return this.value;
	}

	protected static final OrmPreference anon( String key, Serializable value ) {
		return new OrmPreference( key, value ) {
		};
	}
}