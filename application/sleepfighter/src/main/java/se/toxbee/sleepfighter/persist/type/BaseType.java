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

import com.j256.ormlite.field.DataPersister;
import com.j256.ormlite.field.SqlType;
import com.j256.ormlite.field.types.BaseDataType;

import java.util.Arrays;

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