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

package se.toxbee.sleepfighter.challenge;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;

import se.toxbee.sleepfighter.model.challenge.ChallengeType;

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
		private List<String> dependers;
		
		protected ParameterDefinition( String key, ValueType type, Object defaultValue ) {
			this.key = key;
			this.type = type;
			this.defaultValue = defaultValue;
		}
		
		protected ParameterDefinition( String key, ValueType type, Object defaultValue, List<String> dependers ) {
			
			if(type != PrimitiveValueType.BOOLEAN) {
				throw new IllegalArgumentException("valType is not of type boolean");
			}
			
			this.key = key;
			this.type = type;
			this.defaultValue = defaultValue;
			this.dependers = dependers;
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
		
		/**
		 * Returns the keys of the dependers of this param. If this parameter is false, then all dependers will be disabled.
		 *
		 * @return the dependers of this param.
		 */
		public List<String> getDependers() {
			return this.dependers;
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
	 * Adds a parameter definition.
	 *
	 * @param key the key of the parameter.
	 * @param valType the value type of the parameter.
	 * @param defaultValue the default value.
	 * @param defaultEager whether or not the default value is eager to save itself.
	 *  Use this if you want to keep the old default value when you change it between challenge versions.
	 * @param dependers The keys of all the dependers of this param. If this parameter is false, then all dependers will be disabled.
	 *  Only works if valType == ValueType.Boolean, otherwise an exception is thrown.
	 * @return this.
	 */
	public ChallengePrototypeDefinition add( String key, ValueType valType, Object defaultValue, List<String> dependers) {
		
		
		
		ParameterDefinition def = new ParameterDefinition( key, valType, defaultValue, dependers );
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