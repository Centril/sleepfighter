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

package se.toxbee.sleepfighter.utils.factory;

import java.lang.reflect.Constructor;

import se.toxbee.sleepfighter.utils.reflect.ROJava6Exception;
import se.toxbee.sleepfighter.utils.reflect.ReflectionUtil;

/**
 * FactoryDirectInstantiator acts as a gateway between FactoryInstantiator
 * and a {@link Class} object by directly constructing it when {@link #get()} is called.
 *
 * @param <K> Key type restriction.
 * @param <V> Value type restriction. All items produced from instantiator must be at least of this super-class.
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @since 2012-12-11
 * @version 1.0
 */
public class FactoryClassInstantiator<K, V> implements FactoryInstantiator<K, V> {
	/** Stores the constructor rather than {@link Class}, for optimization. */
	protected final Constructor<? extends V> ctor;
	protected final Object[] params;

	/**
	 * Constructs an instantiator that constructs objects of with the {@link Class} _class
	 * using constructor with paramTypes. 
	 *
	 * @param _class The class object to instantiate.
	 * @param paramTypes
	 */
	public FactoryClassInstantiator( Class<? extends V> clazz, Object... params ) {
		this.params = params;
		this.ctor = ReflectionUtil.getCtor( clazz, ReflectionUtil.getClasses( params ) );
	}

	/**
	 * {@inheritDoc}
	 * The object is constructed from a Class object.
	 * The key parameter is ignored, it is simply to
	 * costly to find the class of generic type K every time.
	 * 
	 * @param key The key parameter is ignored.
	 * @throws ROJava6Exception when an error occurs during construction.
	 */
	@Override
	public V produce( K key ) {
		return ReflectionUtil.newInstance( this.ctor, this.params );
	}
}