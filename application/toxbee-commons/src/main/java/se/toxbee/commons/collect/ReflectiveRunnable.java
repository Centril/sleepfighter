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

package se.toxbee.commons.collect;

import java.lang.reflect.Method;

import se.toxbee.commons.reflect.ReflectionUtil;

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
