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
 * FactorySingletonInstantiator acts as a singleton gateway for a given FactoryInstantiator.
 *
 * @param <K> Key type.
 * @param <V> Value type restriction. All items produced from instantiator must be at least of this super-class.
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @since 2012-12-21
 * @version 1.0
 */
public class FactorySingletonInstantiator<K, V> implements FactoryInstantiator<K, V> {
	/** Holds the lazy-loaded singleton instance. */
	protected V instance;

	/** The instantiator used for instantiation. */
	protected final FactoryInstantiator<K, V> instantiator;

	/**
	 * Constructs the FactorySingletonInstantiator with a given FactoryInstantiator<T>
	 *
	 * @param instantiator The FactoryInstantiator<T> to singleton-construct.
	 */
	public FactorySingletonInstantiator( final FactoryInstantiator<K, V> instantiator ) {
		this.instantiator = instantiator;
	}

	/**
	 * Returns the singleton instance.
	 *
	 * @return The singleton instance.
	 */
	public V produce( K key ) {
		if ( this.instance == null ) {
			this.instance = this.instantiator.produce( key );
		}

		return this.instance;
	}
}