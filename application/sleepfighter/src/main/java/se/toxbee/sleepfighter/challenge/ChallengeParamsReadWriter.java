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
package se.toxbee.sleepfighter.challenge;

import se.toxbee.sleepfighter.model.challenge.ChallengeConfig;
import se.toxbee.sleepfighter.model.challenge.ChallengeConfigSet;
import se.toxbee.sleepfighter.model.challenge.ChallengeType;

/**
 * ChallengeParamsReadWriter provides utilities for reading and writing parameters<br/>
 * for a {ChallengeConfigSet, ChallengeType} == ChallengeConfig.<br/>
 * This is the preferred way of dealing with challenge parameters.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Oct 4, 2013
 */
public class ChallengeParamsReadWriter {
	private ChallengeConfigSet challengeSet;
	private ChallengeType challengeType;

	/**
	 * Constructs a ChallengeParamsReadWriter without calling<br/>
	 * {@link #setChallengeSet(ChallengeConfigSet)} and {@link #setChallengeType(ChallengeType)}<br/>
	 * These must be called manually after.
	 */
	public ChallengeParamsReadWriter() {
	}

	/**
	 * Constructs a ChallengeParamsReadWriter with a given ChallengeConfigSet and ChallengeType.
	 *
	 * @param set the ChallengeConfigSet to manage by this instance.
	 * @param type the ChallengeType to manage by this instance.
	 */
	public ChallengeParamsReadWriter( ChallengeConfigSet set, ChallengeType type ) {
		this.setChallengeSet( set );
		this.setChallengeType( type );
	}

	/**
	 * Returns the ChallengeConfigSet that is currently being managed by this instance.
	 *
	 * @return the ChallengeConfigSet that is currently being managed by this instance.
	 */
	public ChallengeConfigSet getChallengeSet() {
		return this.challengeSet;
	}

	/**
	 * Sets the ChallengeConfigSet that is to be managed by this instance.
	 *
	 * @param challengeSet the ChallengeConfigSet that is to be managed by this instance.
	 */
	public void setChallengeSet( ChallengeConfigSet challengeSet ) {
		this.challengeSet = challengeSet;
	}

	/**
	 * Returns the ChallengeType that is currently being managed by this instance.
	 *
	 * @return the ChallengeType that is currently being managed by this instance.
	 */
	public ChallengeType getChallengeType() {
		return this.challengeType;
	}

	/**
	 * Sets the ChallengeType that is to be managed by this instance.
	 *
	 * @param challengeType the ChallengeType that is to be managed by this instance.
	 */
	public void setChallengeType( ChallengeType challengeType ) {
		this.challengeType = challengeType;
	}

	/* --------------------------------
	 * Facades for accessors.
	 * --------------------------------
	 */

	/**
	 * Returns the parameter value for key as a String.
	 *
	 * @param key the key.
	 * @return the value as a String.
	 */
	public String getRaw( String key ) {
		return this.getConfig().getParam( key );
	}

	/**
	 * Returns the parameter value for key as a String.
	 *
	 * @param key the key.
	 * @param defaultValue default value to use instead of null.
	 * @return the value as a String.
	 */
	public String getString( String key, String defaultValue ) {
		String val = this.getRaw( key );
		return val == null ? defaultValue : val;
	}

	/**
	 * Returns the parameter value for key as an integer.
	 *
	 * @param key the key.
	 * @param defaultValue default value to use instead of null.
	 * @return the value as an integer.
	 */
	public int getInt( String key, int defaultValue ) {
		String val = this.getRaw( key );
		return val == null ? defaultValue : Integer.parseInt( val );
	}

	/**
	 * Returns the parameter value for key as a float.
	 *
	 * @param key the key.
	 * @param defaultValue default value to use instead of null.
	 * @return the value as a float.
	 */
	public float getFloat( String key, float defaultValue ) {
		String val = this.getRaw( key );
		return val == null ? defaultValue : Float.parseFloat( val );
	}

	/**
	 * Returns the parameter value for key as a double.
	 *
	 * @param key the key.
	 * @param defaultValue default value to use instead of null.
	 * @return the value as a double.
	 */
	public double getDouble( String key, double defaultValue ) {
		String val = this.getRaw( key );
		return val == null ? defaultValue : Double.parseDouble( val );
	}

	/**
	 * Returns the parameter value for key as a boolean.
	 *
	 * @param key the key.
	 * @param defaultValue default value to use instead of null.
	 * @return the value as a boolean.
	 */
	public boolean getBoolean( String key, boolean defaultValue ) {
		String val = this.getRaw( key );
		return val == null ? defaultValue : Boolean.parseBoolean( val );
	}

	/**
	 * Returns the parameter value for key as a long.
	 *
	 * @param key the key.
	 * @param defaultValue default value to use instead of null.
	 * @return the value as a long.
	 */
	public long getLong( String key, long defaultValue ) {
		String val = this.getRaw( key );
		return val == null ? defaultValue : Long.parseLong( val );
	}

	/* --------------------------------
	 * Facades for mutators.
	 * --------------------------------
	 */

	/**
	 * Sets the parameter value for key from a string value.
	 *
	 * @param key the key.
	 * @param value the value as a string.
	 */
	public void setString( String key, String value ) {
		this.challengeSet.setConfigParam( this.challengeType, key, value );
	}

	/**
	 * Sets the parameter value for key from an integer value.
	 *
	 * @param key the key.
	 * @param value the value as an integer.
	 */
	public void setInt( String key, int value ) {
		this.setString( key, Integer.toString( value ) );
	}

	/**
	 * Sets the parameter value for key from a float value.
	 *
	 * @param key the key.
	 * @param value the value as a float.
	 */
	public void setFloat( String key, float value ) {
		this.setString( key, Float.toString( value ) );
	}

	/**
	 * Sets the parameter value for key from a double value.
	 *
	 * @param key the key.
	 * @param value the value as a double.
	 */
	public void setDouble( String key, double value ) {
		this.setString( key, Double.toString( value ) );
	}

	/**
	 * Sets the parameter value for key from a double value.
	 *
	 * @param key the key.
	 * @param value the value as a boolean.
	 */
	public void setBoolean( String key, boolean value ) {
		this.setString( key, Boolean.toString( value ) );
	}

	/**
	 * Sets the parameter value for key from a long value.
	 *
	 * @param key the key.
	 * @param value the value as a long.
	 */
	public void setLong( String key, long value ) {
		this.setString( key, Long.toString( value ) );
	}

	/**
	 * Returns the resultant ChallengeConfig that is in reality managed.
	 *
	 * @return the ChallengeConfig. 
	 */
	public ChallengeConfig getConfig() {
		return this.challengeSet.getConfig( this.challengeType );
	}
}