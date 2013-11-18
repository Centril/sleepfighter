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

import se.toxbee.sleepfighter.utils.model.CodifiableLong.Factory;
import se.toxbee.sleepfighter.utils.model.CodifiableLong;
import se.toxbee.sleepfighter.utils.reflect.ReflectionUtil;

import com.j256.ormlite.field.FieldType;

/**
 * CodifiableType defines how to handle a {@link Codifiable} for OrmLite.<br/>
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