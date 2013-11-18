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

import java.util.Arrays;

import com.j256.ormlite.field.DataPersister;
import com.j256.ormlite.field.SqlType;
import com.j256.ormlite.field.types.BaseDataType;

/**
 * BaseType is the base type of all custom {@link DataPersister}:s.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Nov 16, 2013
 */
public abstract class BaseType extends BaseDataType {
	protected final String[] associatedClassNames;

	protected BaseType( SqlType sqlType, Class<?> clazz ) {
		this( sqlType, new Class<?>[] { clazz } );
	}

	protected BaseType( SqlType sqlType, Class<?>[] clazzes ) {
		super( sqlType, clazzes );

		// Figure out class names.
		int len = clazzes.length;
		this.associatedClassNames = new String[len];
		for ( int i = 0; i < len; ++i ) {
			this.associatedClassNames[i] = clazzes[i].getName();
		}
	}

	@Override
	public String[] getAssociatedClassNames() {
		return associatedHelper( this.associatedClassNames );
	}

	protected static final String[] associatedHelper( String[] associatedClassNames ) {
		return Arrays.copyOf( associatedClassNames, associatedClassNames.length );
	}
}