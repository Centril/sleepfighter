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
	public V get( K key ) {
		if ( this.instance == null ) {
			this.instance = this.instantiator.get( key );
		}

		return this.instance;
	}
}