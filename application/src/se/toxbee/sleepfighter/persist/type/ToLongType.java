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

import java.sql.SQLException;

import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.field.SqlType;
import com.j256.ormlite.support.DatabaseResults;

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