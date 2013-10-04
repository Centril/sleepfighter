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
package se.chalmers.dat255.sleepfighter.model.challenge;

import com.j256.ormlite.field.DatabaseField;

import se.chalmers.dat255.sleepfighter.model.IdProvider;

/**
 * ChallengeParam models one parameter for a ChallengeConfig<br/>
 * this is provided for the database mostly and is reworked<br/>
 * on runtime to fit ChallengeConfig as a map.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Oct 3, 2013
 */
public class ChallengeParam implements IdProvider {
	public static final String CHALLENGE_ID_COLUMN = "challenge_id";

	@DatabaseField(id = true, columnName = CHALLENGE_ID_COLUMN)
	private int challengeId;

	@DatabaseField
	private String key;

	@DatabaseField
	private String value;

	/**
	 * Constructs the parameter with given challenge ID, key and value.
	 *
	 * @param id the ID of the challenge config.
	 * @param key the key of the parameter.
	 * @param value the value of the parameter.
	 */
	public ChallengeParam( int id, String key, String value ) {
		this.challengeId = id;
		this.key = key;
		this.value = value;
	}

	@Override
	public int getId() {
		return this.challengeId;
	}

	/**
	 * Returns the key of the param.
	 *
	 * @return the key-
	 */
	public String getKey() {
		return this.key;
	}

	/**
	 * Returns the value of the param.
	 *
	 * @return the value.
	 */
	public String getValue() {
		return this.value;
	}
}