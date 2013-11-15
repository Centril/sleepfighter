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
package se.toxbee.sleepfighter.persist.type;

import se.toxbee.sleepfighter.model.AlarmMode;
import se.toxbee.sleepfighter.model.AlarmTime;
import se.toxbee.sleepfighter.utils.model.Codifiable;

import com.j256.ormlite.field.DataPersister;
import com.j256.ormlite.field.DataPersisterManager;

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
		rdt( AlarmTime.class );
		rdt( AlarmMode.class );
	}

	private static void rdt( Class<? extends Codifiable> clazz ) {
		rdt( new CodifiableType( clazz ) );
	}

	private static void rdt( DataPersister p ) {
		DataPersisterManager.registerDataPersisters( p );
	}
}
