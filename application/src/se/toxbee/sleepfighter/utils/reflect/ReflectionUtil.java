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
package se.toxbee.sleepfighter.utils.reflect;

import com.google.common.collect.ObjectArrays;

/**
 * ReflectionUtil provides very basic reflection utilities.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Nov 5, 2013
 */
public class ReflectionUtil {
	/**
	 * Creates an array of type T of size.
	 *
	 * @param clazz the Class object of type T.
	 * @param size the size of array.
	 * @return the array.
	 */
	public static <T> T[] makeArray( Class<T> clazz, int size ) {
		return ObjectArrays.newArray( clazz, size );
	}

	/**
	 * Returns the Class object for an element of a generic array.
	 *
	 * @param arr the array.
	 * @return the Class object.
	 */
	@SuppressWarnings( "unchecked" )
	public static <T> Class<T> arrayClass( T[] arr ) {
		return (Class<T>) arr.getClass().getComponentType();
	}
}
