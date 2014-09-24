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

package se.toxbee.ormlite.type;

import com.j256.ormlite.field.FieldType;

import se.toxbee.commons.math.Conversion;

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
