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

package se.toxbee.sleepfighter.model.challenge;

import com.google.common.collect.Maps;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Map;

import se.toxbee.sleepfighter.persist.dao.ChallengeParamDao;
import se.toxbee.sleepfighter.utils.model.IdProvider;
import se.toxbee.sleepfighter.utils.string.StringUtils;

/**
 * ChallengeParam models one parameter for a ChallengeConfig<br/>
 * this is provided for the database mostly and is reworked<br/>
 * on runtime to fit ChallengeConfig as a map.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Oct 3, 2013
 */
@DatabaseTable(tableName = "challenge_params", daoClass = ChallengeParamDao.class)
public class ChallengeParam implements IdProvider {
	public static final String CHALLENGE_ID_COLUMN = "challenge_id";
	public static final String CHALLENGE_VALUE_COLUMN = "value";

	@DatabaseField(generatedId = true)
	private int id;

	@DatabaseField(columnName = CHALLENGE_ID_COLUMN, uniqueCombo = true, index = true)
	private int challengeId;

	@DatabaseField(uniqueCombo = true, index = true)
	private String key;

	@DatabaseField(columnName = CHALLENGE_VALUE_COLUMN)
	private String value;

	/**
	 * Constructs the parameter with no given values.
	 */
	public ChallengeParam() {
	}

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

	@Override
	public String toString() {
		final Map<String, String> prop = Maps.newHashMap();
		prop.put( "id", Integer.toString( this.getId() ) );
		prop.put( "key", this.getKey() );
		prop.put( "value", this.getValue() );

		return "ChallengeParam[" + StringUtils.PROPERTY_MAP_JOINER.join( prop ) + "]";
	}
}