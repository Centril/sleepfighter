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

import se.toxbee.sleepfighter.utils.string.StringUtils;

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
	 * Returns the {@link Class} objects of objs.
	 *
	 * @param objs an array of objects.
	 * @return the classes.
	 */
	public static Class<?>[] getClasses( Object... objs ) {
		int len = objs.length;

		Class<?>[] types = new Class<?>[len];
		for ( int i = 0; i < len; ++i ) {
			types[i] = objs[i].getClass();
		}

		return types;
	}

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

	/**
	 * Returns a {@link Class} for fully qualified class with name as a subclass of target.
	 *
	 * @param name the fully qualified name of class.
	 * @param target the target class/interface.
	 * @return the resulting {@link Class}.
	 * @throws ClassNotFoundException when there's no class with name.
	 */
	public static <U> Class<? extends U> classForName( String name, Class<U> target ) throws ClassNotFoundException {
		Class<?> dirty = Class.forName( name );
		Class<? extends U> clazz = dirty.asSubclass( target );
		return clazz;
	}

	/**
	 * {@code Class} to represent a subclass of the given class.
	 * If successful, this {@code Class} is returned; otherwise null is returned.
	 *
	 * @return the {@code Class} or null.
	 */
	@SuppressWarnings( "unchecked" )
	public static <U> Class<? extends U> asSubclass( Class<?> from, Class<U> target ) {
		return target.isAssignableFrom( from ) ? (Class<U>) from : null;
	}

	/**
	 * Returns the fully qualified package name of a package that is descendant of a package with clazz<br/>
	 * If descendant is null, the package name of clazz is returned.
	 *
	 * @param clazz the clazz of the parent package.
	 * @param descendant the descendant package.
	 * @return the package name.
	 */
	public static String packageDecendantName( Class<?> clazz, String descendant ) {
		return StringUtils.QUALIFIER_JOINER.join( clazz.getPackage().getName(), descendant );
	}

	/**
	 * Returns the fully qualified package name of a package that is descendant of a package with clazz<br/>
	 * If descendant is null, the package name of clazz is returned.
	 *
	 * @param o the object to get Class from (the Class of the parent package).
	 * @param descendant the descendant package.
	 * @return the package name.
	 */
	public static String packageDecendantName( Object o, String descendant ) {
		return packageDecendantName( o.getClass(), descendant );
	}
}
