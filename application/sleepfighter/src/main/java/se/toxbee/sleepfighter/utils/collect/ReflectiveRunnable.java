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
package se.toxbee.sleepfighter.utils.collect;

import java.lang.reflect.Method;

import se.toxbee.sleepfighter.utils.reflect.ReflectionUtil;

/**
 * {@link ReflectiveRunnable} is a {@link Runnable} that calls methods via reflection.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Dec 22, 2013
 */
public class ReflectiveRunnable implements Runnable {
	private final Method m;
	private final Object r;
	private final Object[] a;

	/**
	 * Constructs the runnable given a {@link Class}, a name of a static method, and arguments.
	 *
	 * @param clazz the class that has static method with name.
	 * @param name the name of the static method in clazz.
	 * @param a the arguments (varargs).
	 */
	public ReflectiveRunnable( Class<?> clazz, String name, Object... a) {
		this( ReflectionUtil.declaredMethod( clazz, name, a ), a );
	}

	/**
	 * Constructs the runnable given a {@link Class}, an object receiver, a name of a non-static method, and arguments.
	 *
	 * @param clazz the class that has non-static method with name.
	 * @param r the receiver.
	 * @param name the name of the static method in clazz.
	 * @param a the arguments (varargs).
	 */
	public ReflectiveRunnable( Object r, String name, Object... a) {
		this( ReflectionUtil.declaredMethod( r.getClass(), name, a ), r, a );
	}

	/**
	 * Constructs the runnable given a static method, and arguments.
	 *
	 * @param m the static method.
	 * @param a the arguments (varargs).
	 */
	public ReflectiveRunnable( Method m, Object... a ) {
		this( m, (Object) null, a );
	}

	/**
	 * Constructs the runnable given a method, its object receiver, and arguments.
	 *
	 * @param m the method.
	 * @param r the receiver.
	 * @param a the arguments (varargs).
	 */
	public ReflectiveRunnable( Method m, Object r, Object... a ) {
		this.m = m;
		this.r = r;
		this.a = a;
	}

	@Override
	public void run() {
		ReflectionUtil.invoke( this.m, this.r, this.a );
	}
}
