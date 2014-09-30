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
import com.j256.ormlite.field.SqlType;
import com.j256.ormlite.support.DatabaseResults;

import java.sql.SQLException;

/**
 * ToIntegerType is the base type for anything that is stored in database as a {@link SqlType#INTEGER}.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Nov 14, 2013
 */
public abstract class ToIntegerType extends BaseType {
	protected ToIntegerType( Class<?> clazz ) {
		this( new Class<?>[] { clazz } );
	}

	protected ToIntegerType( Class<?>[] clazzes ) {
		super( SqlType.INTEGER, clazzes );
	}

	protected abstract Integer toInt( FieldType fieldType, Object javaObject );
	protected abstract Object fromInt( FieldType fieldType, Integer val );

	@Override
	public Integer javaToSqlArg( FieldType fieldType, Object javaObject ) throws SQLException {
		return toInt( fieldType, javaObject );
	}

	@Override
	public Object sqlArgToJava(FieldType fieldType, Object sqlArg, int columnPos) throws SQLException {
		return fromInt( fieldType, (Integer) sqlArg );
	}

	@Override
	public Object parseDefaultString(FieldType fieldType, String defaultStr) {
		return fromInt( fieldType, Integer.parseInt( defaultStr ) );
	}

	@Override
	public Object resultToSqlArg( FieldType fieldType, DatabaseResults results, int columnPos ) throws SQLException {
		return results.getInt( columnPos );
	}

	@Override
	public boolean isEscapedValue() {
		return false;
	}
}