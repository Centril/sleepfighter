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

package se.toxbee.ormlite.prefs;

import com.j256.ormlite.field.DatabaseField;

import java.io.Serializable;

import se.toxbee.commons.prefs.SerializablePreference;

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

	/**
	 * Constructs the preference with key and value.
	 *
	 * @param p copy source.
	 */
	public OrmPreference( SerializablePreference p ) {
		this.key = p.key();
		this.value = p.value();
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