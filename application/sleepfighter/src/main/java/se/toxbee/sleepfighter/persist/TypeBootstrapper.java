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

package se.toxbee.sleepfighter.persist;

import com.j256.ormlite.field.DataPersister;
import com.j256.ormlite.field.DataPersisterManager;

import se.toxbee.ormlite.type.BooleanArrayType;
import se.toxbee.ormlite.type.CodifiableLongType;
import se.toxbee.ormlite.type.CodifiableType;
import se.toxbee.sleepfighter.model.time.CountdownTime;
import se.toxbee.sleepfighter.model.time.ExactTime;

/**
 * TypeBootstrapper has the single responsibility of registering all custom data types to OrmLite.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Nov 15, 2013
 */
public class TypeBootstrapper {
	public static void init() {
		rdt( BooleanArrayType.getSingleton() );
		rdt( new CodifiableType( ExactTime.class ) );
		rdt( new CodifiableLongType( CountdownTime.class ) );
	}

	private static void rdt( DataPersister p ) {
		DataPersisterManager.registerDataPersisters( p );
	}
}
