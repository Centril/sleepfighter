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

import se.toxbee.sleepfighter.utils.model.Codifiable;
import se.toxbee.sleepfighter.utils.model.Codifiable.Factory;
import se.toxbee.sleepfighter.utils.reflect.ReflectionUtil;

/**
 * CodifiableType defines how to handle a {@link Codifiable} for OrmLite.<br/>
 * Stored as an integer in database.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Nov 15, 2013
 */
public class CodifiableType extends ToIntegerType {
	private final Codifiable.Factory factory;

	public CodifiableType( Class<? extends Codifiable> clazz ) {
		this( clazz, findFactory( clazz ) );
	}

	protected static Codifiable.Factory findFactory( Class<? extends Codifiable> clazz ) {
		return ReflectionUtil.newInstance( ReflectionUtil.getNested( clazz, Factory.class ) );
	}

	public CodifiableType( Class<? extends Codifiable> clazz, Codifiable.Factory f ) {
		super( clazz );
		this.factory = f;
	}

	@Override
	protected Integer toInt( FieldType fieldType, Object javaObject ) {
		return javaObject == null ? null : ((Codifiable) javaObject).toCode();
	}

	@Override
	protected Object fromInt( FieldType fieldType, Integer val ) {
		return this.factory.produce( val );
	}
}