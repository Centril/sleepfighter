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

import se.toxbee.sleepfighter.utils.math.Conversion;

import com.j256.ormlite.field.FieldType;

/**
 * Defines how to handle a boolean array for OrmLite.<br/>
 * Stored as an integer in database.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Sep 23, 2013
 */
public class BooleanArrayType extends ToIntegerType {
	private static Class<?> clazz = new boolean[0].getClass();
	private static final BooleanArrayType singleton = new BooleanArrayType();

	private BooleanArrayType() {
		super( clazz );
	}

	@Override
	protected Integer toInt( FieldType fieldType, Object javaObject ) {
		return javaObject == null ? null : Conversion.boolArrayToInt( (boolean[]) javaObject );
	}

	@Override
	protected Object fromInt( FieldType fieldType, Integer val ) {
		return Conversion.intToBoolArray( val, fieldType.getWidth() );
	}

	public static BooleanArrayType getSingleton() {
		return singleton;
	}

	@Override
	public boolean isAppropriateId() {
		return false;
	}
}
