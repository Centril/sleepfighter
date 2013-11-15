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
import java.util.Arrays;

import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.field.SqlType;
import com.j256.ormlite.field.types.BaseDataType;
import com.j256.ormlite.support.DatabaseResults;

/**
 * ToIntegerType is the base type for anything that is stored in database as an INTEGER.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Nov 14, 2013
 */
public abstract class ToIntegerType extends BaseDataType {
	protected final String[] associatedClassNames;

	protected ToIntegerType( Class<?> clazz ) {
		this( new Class<?>[] { clazz } );
	}

	protected ToIntegerType( Class<?>[] clazzes ) {
		super( SqlType.INTEGER, clazzes );

		// Figure out class names.
		int len = clazzes.length;
		this.associatedClassNames = new String[len];
		for ( int i = 0; i < len; ++i ) {
			this.associatedClassNames[i] = clazzes[i].getName();
		}
	}

	protected abstract Integer toInt( FieldType fieldType, Object javaObject );
	protected abstract Object fromInt( FieldType fieldType, Integer val );

	@Override
	public String[] getAssociatedClassNames() {
		return associatedHelper( this.associatedClassNames );
	}

	protected static final String[] associatedHelper( String[] associatedClassNames ) {
		return Arrays.copyOf( associatedClassNames, associatedClassNames.length );
	}

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