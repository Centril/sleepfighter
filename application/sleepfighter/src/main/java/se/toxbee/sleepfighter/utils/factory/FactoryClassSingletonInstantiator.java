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
 * FactoryDirectSingletonInstantiator acts as a gateway between FactoryInstantiator
 * and a Class object by directly constructing it when {@link #produce(Object)} is called.
 *
 * @param <K> Key type restriction.
 * @param <V> Value type restriction. All items produced from instantiator must be at least of this super-class.
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @since 2012-12-21
 * @version 1.0
 */
public class FactoryClassSingletonInstantiator<K, V> extends FactorySingletonInstantiator<K, V> {
	/**
	 * Takes a Class object that will be singleton-instantiated with no-parameter constructor.
	 *
	 * @param _class The class object to instantiate.
	 */
	public FactoryClassSingletonInstantiator( Class<? extends V> _class ) {
		super( new FactoryClassInstantiator<K, V>( _class ) );
	}
}