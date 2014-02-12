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

import com.google.common.collect.Maps;

import java.util.Map;

import se.toxbee.sleepfighter.challenge.ChallengePrototypeDefinition.ParameterDefinition;
import se.toxbee.sleepfighter.challenge.ChallengePrototypeDefinition.PrimitiveValueType;
import se.toxbee.sleepfighter.model.challenge.ChallengeConfigSet;
import se.toxbee.sleepfighter.model.challenge.ChallengeType;

/**
 * ChallengeResolvedParams delivers the resolved parameters to a challenge.<br/>
 * It takes default values into account and converts to the correct types.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Oct 8, 2013
 */
public class ChallengeResolvedParams {
	private ChallengeType type;
	private Map<String, Object> values;

	/**
	 * Constructs a ChallengeResolvedParams.<br/>
	 * Call {@link #resolve(ChallengeConfigSet, ChallengePrototypeDefinition)}<br/>
	 * to get started!
	 */
	public ChallengeResolvedParams() {
		this.values = Maps.newHashMap();
	}

	/**
	 * Returns the ChallengeType the params are resolved for.
	 *
	 * @return the type.
	 */
	public ChallengeType getType() {
		return this.type;
	}

	/**
	 * Resolves all the parameters in set with given definition using the type from definition.
	 *
	 * @param set the ChallengeConfigSet.
	 * @param definition the ChallengePrototypeDefinition.
	 */
	public void resolve( ChallengeConfigSet set, ChallengePrototypeDefinition definition ) {
		this.type = definition.getType();

		this.values = Maps.newHashMap();
		ChallengeParamsReadWriter reader = new ChallengeParamsReadWriter( set, definition.getType() );

		for ( ParameterDefinition paramDef : definition.get() ) {
			Object value = null;

			if ( paramDef.getType() instanceof PrimitiveValueType ) {
				switch ( (PrimitiveValueType) paramDef.getType() ) {
				case BOOLEAN:
					value = reader.getBoolean( paramDef.getKey(), (Boolean) paramDef.getDefaultValue() );
					break;

				case DOUBLE:
					value = reader.getDouble( paramDef.getKey(), (Double) paramDef.getDefaultValue() );
					break;

				case FLOAT:
					value = reader.getFloat( paramDef.getKey(), (Float) paramDef.getDefaultValue() );
					break;

				case INTEGER:
					value = reader.getInt( paramDef.getKey(), (Integer) paramDef.getDefaultValue() );
					break;

				case LONG:
					value = reader.getLong( paramDef.getKey(), (Long) paramDef.getDefaultValue() );
					break;

				case STRING:
					value = reader.getString( paramDef.getKey(), (String) paramDef.getDefaultValue() );
					break;

				default:
					break;
				}
			} else {
				value = reader.getString( paramDef.getKey(), (String) paramDef.getDefaultValue() );
			}

			this.values.put( paramDef.getKey(), value );
		}
	}

	/**
	 * Returns the parameter value for key as a String.
	 *
	 * @param key the key.
	 * @return the value as a String.
	 */
	public String getString( String key ) {
		return (String) this.values.get( key );
	}

	/**
	 * Returns the parameter value for key as an integer.
	 *
	 * @param key the key.
	 * @return the value as an integer.
	 */
	public int getInt( String key ) {
		return (Integer) this.values.get( key );
	}

	/**
	 * Returns the parameter value for key as a float.
	 *
	 * @param key the key.
	 * @return the value as a float.
	 */
	public float getFloat( String key ) {
		return (Float) this.values.get( key );
	}

	/**
	 * Returns the parameter value for key as a double.
	 *
	 * @param key the key.
	 * @return the value as a double.
	 */
	public double getDouble( String key ) {
		return (Double) this.values.get( key );
	}

	/**
	 * Returns the parameter value for key as a boolean.
	 *
	 * @param key the key.
	 * @return the value as a boolean.
	 */
	public boolean getBoolean( String key ) {
		return (Boolean) this.values.get( key );
	}

	/**
	 * Returns the parameter value for key as a long.
	 *
	 * @param key the key.
	 * @return the value as a long.
	 */
	public long getLong( String key ) {
		return (Long) this.values.get( key );
	}
}