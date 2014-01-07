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
 * FactoryDirectSingletonInstantiator acts as a gateway between FactoryInstantiator
 * and a Class object by directly constructing it when {@link #get()} is called.
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