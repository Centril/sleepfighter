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

/**
 * <p>An abstract factory class wrapping all given FactoryInstantiator or<br/>
 * {@link FactoryClassInstantiator} in case of {@link #add(Object, Class)} in<br/>
 * a {@link FactorySingletonInstantiator} which has the effect that<br/>
 * once produced the same object is returned for the same key.</p>
 *
 * @param <K> Key type.
 * @param <T> Type restriction. All items produced from factory must be at least of this super-class.
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @since 2012-12-21
 * @version 1.0
 */
public abstract class SingletonFactory<K, T> extends AbstractFactory<K, T> {
	/**
	 * Enables run-time addition of a Class object.
	 *
	 * @param key What the FactoryInstantiator is called "publicly".
	 * @param _class Class object of class to instantiate
	 */
	public AbstractFactory<K, T> add( final K key, final Class<? extends T> _class ) {
		return add( key, new FactorySingletonInstantiator<K, T>( new FactoryClassInstantiator<K, T>( _class ) ) );
	}

	/**
	 * Enables run-time addition of a FactoryInstantiator.
	 *
	 * @param name What the FactoryInstantiator is called "publicly".
	 * @param instantiator A FactoryInstantiator used to instantiate an object.
	 */
	public AbstractFactory<K, T> add( final K key, final FactoryInstantiator<K, T> instantiator) {
		this.relations.put( key, new FactorySingletonInstantiator<K, T>( instantiator ) );
		return this;
	}
}
