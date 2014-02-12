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

package se.toxbee.sleepfighter.persist.prefs;

import com.j256.ormlite.table.DatabaseTable;

import se.toxbee.sleepfighter.utils.prefs.SerializablePreference;

/**
 * {@link PersistPreference} exists for the<br/>
 * sole purpose of defining the table "prefs".
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Dec 14, 2013
 */
@DatabaseTable(tableName = "prefs")
public class PersistPreference extends OrmPreference {
	public PersistPreference() {
		super();
	}
	public PersistPreference( SerializablePreference p ) {
		super( p );
	}
}
