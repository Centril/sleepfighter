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

import se.toxbee.sleepfighter.utils.model.CodifiableLong;
import se.toxbee.sleepfighter.utils.model.CodifiableLong.Factory;
import se.toxbee.sleepfighter.utils.reflect.ReflectionUtil;

/**
 * CodifiableLongType defines how to handle a {@link se.toxbee.sleepfighter.utils.model.CodifiableLong} for OrmLite.<br/>
 * Stored as a long in database.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Nov 16, 2013
 */
public class CodifiableLongType extends ToLongType {
	private final CodifiableLong.Factory factory;

	public CodifiableLongType( Class<? extends CodifiableLong> clazz ) {
		this( clazz, findFactory( clazz ) );
	}

	protected static CodifiableLong.Factory findFactory( Class<? extends CodifiableLong> clazz ) {
		return ReflectionUtil.newInstance( ReflectionUtil.getNested( clazz, Factory.class ) );
	}

	public CodifiableLongType( Class<? extends CodifiableLong> clazz, CodifiableLong.Factory f ) {
		super( clazz );
		this.factory = f;
	}


	@Override
	protected Long toLong( FieldType fieldType, Object javaObject ) {
		return javaObject == null ? null : ((CodifiableLong) javaObject).toCodeLong();
	}

	@Override
	protected Object fromLong( FieldType fieldType, Long val ) {
		return this.factory.produce( val );
	}
}