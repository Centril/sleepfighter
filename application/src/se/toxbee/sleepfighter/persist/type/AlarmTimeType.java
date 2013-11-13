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

import se.toxbee.sleepfighter.model.AlarmTime;

import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.field.SqlType;
import com.j256.ormlite.field.types.BaseDataType;
import com.j256.ormlite.support.DatabaseResults;

/**
 * AlarmTimeType defines how to handle an AlarmTime for OrmLite.<br/>
 * Stored as an integer in database.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Nov 13, 2013
 */
public class AlarmTimeType extends BaseDataType {
	private static Class<?> clazz = AlarmTime.class;
	private static final AlarmTimeType singleton = new AlarmTimeType();
	private static final String[] associatedClassNames = new String[] { clazz.getName() };

	private AlarmTimeType() {
		super(SqlType.INTEGER, new Class<?>[] {clazz});
	}

	public static AlarmTimeType getSingleton() {
		return singleton;
	}

	@Override
	public String[] getAssociatedClassNames() {
		return Arrays.copyOf(associatedClassNames, associatedClassNames.length);
	}

	@Override
	public Class<?> getPrimaryClass() {
		return clazz;
	}

	@Override
	public Object javaToSqlArg(FieldType fieldType, Object javaObject) throws SQLException {
		return ((AlarmTime) javaObject).pack();
	}

	@Override
	public Object sqlArgToJava(FieldType fieldType, Object sqlArg, int columnPos) throws SQLException {
		return new AlarmTime( (Integer) sqlArg );
	}

	@Override
	public Object parseDefaultString(FieldType fieldType, String defaultStr) {
		return new AlarmTime( Integer.parseInt( defaultStr ) );
	}

	@Override
	public Object resultToSqlArg(FieldType fieldType, DatabaseResults results, int columnPos) throws SQLException {
		return results.getInt(columnPos);
	}

	@Override
	public boolean isEscapedValue() {
		return false;
	}

	@Override
	public boolean isAppropriateId() {
		return false;
	}
}