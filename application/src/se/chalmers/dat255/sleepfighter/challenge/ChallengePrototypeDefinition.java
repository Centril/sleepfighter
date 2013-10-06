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
package se.chalmers.dat255.sleepfighter.challenge;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import com.google.common.collect.Maps;

import se.chalmers.dat255.sleepfighter.model.challenge.ChallengeType;

/**
 * A ChallengePrototypeDefinition declares the type of challenge and what parameters it has.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Oct 4, 2013
 */
public class ChallengePrototypeDefinition {
	/**
	 * PrimitiveValueType is the standard implementation of ValueType.
	 *
	 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
	 * @version 1.0
	 * @since Oct 4, 2013
	 */
	public enum PrimitiveValueType implements ValueType {
		INTEGER, LONG, FLOAT, DOUBLE, BOOLEAN, STRING 
	}

	/**
	 * ValueType tells us how to read and write a parameter.
	 *
	 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
	 * @version 1.0
	 * @since Oct 5, 2013
	 */
	public interface ValueType {}

	/** 
	 * ParameterDefinition provides information about a parameter:<br/>
	 * what key it has, how to read it and a default value.
	 *
	 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
	 * @version 1.0
	 * @since Oct 4, 2013
	 */
	public static class ParameterDefinition {
		private String key;
		private ValueType type;
		private Object defaultValue;

		protected ParameterDefinition( String key, ValueType type, Object defaultValue ) {
			this.key = key;
			this.type = type;
			this.defaultValue = defaultValue;
		}

		/**
		 * Returns the key of the parameter.
		 *
		 * @return the key of the parameter.
		 */
		public String getKey() {
			return this.key;
		}

		/**
		 * Returns the "how to read it" value type information.
		 *
		 * @return the value type.
		 */
		public ValueType getType() {
			return this.type;
		}

		/**
		 * The default value for key.
		 * 
		 * @return the default value.
		 */
		public Object getDefaultValue() {
			return this.defaultValue;
		}
	}

	private ChallengeType challengeType;

	private Map<String, ParameterDefinition> paramDefinitions;

	/**
	 * Constructs a ChallengePrototypeDefinition.
	 */
	public ChallengePrototypeDefinition() {
		this.paramDefinitions = Maps.newHashMap();
	}

	/**
	 * Sets the challenge type of the definition.
	 *
	 * @param type the challenge type.
	 * @return this.
	 */
	public ChallengePrototypeDefinition setType( ChallengeType type ) {
		this.challengeType = type;
		return this;
	}

	/**
	 * Returns the challenge type of the definition.
	 *
	 * @return the challenge type.
	 */
	public ChallengeType getType() {
		return this.challengeType;
	}

	/**
	 * Adds a parameter definition.
	 *
	 * @param key the key of the parameter.
	 * @param valType the value type of the parameter.
	 * @param defaultValue the default value.
	 * @param defaultEager whether or not the default value is eager to save itself.
	 *  Use this if you want to keep the old default value when you change it between challenge versions.
	 * @return this.
	 */
	public ChallengePrototypeDefinition add( String key, ValueType valType, Object defaultValue ) {
		ParameterDefinition def = new ParameterDefinition( key, valType, defaultValue );
		this.paramDefinitions.put( key, def );
		return this;
	}

	/**
	 * Returns the definitions available as a collection.
	 *
	 * @return the collection of all definitions.
	 */
	public Collection<ParameterDefinition> get() {
		return Collections.unmodifiableCollection( this.paramDefinitions.values() );
	}

	/**
	 * Returns one parameter definition for a given key.
	 *
	 * @param key the key of parameter.
	 * @return the parameter definition.
	 */
	public ParameterDefinition get( String key ) {
		return this.paramDefinitions.get( key );
	}

	/**
	 * Returns whether or not there are parameters for this challenge.
	 *
	 * @return true if it has parameters.
	 */
	public boolean hasParams() {
		return this.paramDefinitions.size() > 0;
	}
}