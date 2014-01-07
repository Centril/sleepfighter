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