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

package se.toxbee.sleepfighter.persist.type;

import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.field.SqlType;
import com.j256.ormlite.support.DatabaseResults;

import java.sql.SQLException;

/**
 * ToLongType is the base type for anything that is stored in database as a {@link SqlType#LONG}.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Nov 16, 2013
 */
public abstract class ToLongType extends BaseType {
	protected ToLongType( Class<?> clazz ) {
		this( new Class<?>[] { clazz } );
	}

	protected ToLongType( Class<?>[] clazzes ) {
		super( SqlType.LONG, clazzes );
	}

	protected abstract Long toLong( FieldType fieldType, Object javaObject );
	protected abstract Object fromLong( FieldType fieldType, Long val );

	@Override
	public Long javaToSqlArg( FieldType fieldType, Object javaObject ) throws SQLException {
		return toLong( fieldType, javaObject );
	}

	@Override
	public Object sqlArgToJava(FieldType fieldType, Object sqlArg, int columnPos) throws SQLException {
		return fromLong( fieldType, (Long) sqlArg );
	}

	@Override
	public Object parseDefaultString(FieldType fieldType, String defaultStr) {
		return fromLong( fieldType, Long.parseLong( defaultStr ) );
	}

	@Override
	public Object resultToSqlArg( FieldType fieldType, DatabaseResults results, int columnPos ) throws SQLException {
		return results.getLong( columnPos );
	}

	@Override
	public boolean isEscapedValue() {
		return false;
	}
}