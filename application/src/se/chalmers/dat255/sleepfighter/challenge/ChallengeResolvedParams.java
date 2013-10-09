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

import java.util.Map;

import se.chalmers.dat255.sleepfighter.challenge.ChallengePrototypeDefinition.ParameterDefinition;
import se.chalmers.dat255.sleepfighter.challenge.ChallengePrototypeDefinition.PrimitiveValueType;
import se.chalmers.dat255.sleepfighter.model.challenge.ChallengeConfigSet;
import se.chalmers.dat255.sleepfighter.model.challenge.ChallengeType;

import com.google.common.collect.Maps;

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