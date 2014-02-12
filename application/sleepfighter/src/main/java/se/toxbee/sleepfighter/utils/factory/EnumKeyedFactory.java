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

import java.util.EnumMap;
import java.util.Map;

/**
 * Changes the data structure of relations in a AbstractFactory to EnumMap.
 * Extending classes must implement {@link #keyClass()}.
 *
 * @param <K> Key type restriction.
 * @param <V> Value type restriction. All items produced from factory must be at least of this super-class.
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @since 2012-12-21
 * @version 1.0
 */
public abstract class EnumKeyedFactory<K extends Enum<K>, V> extends AbstractFactory<K, V> {
	/**
	 * Provides EnumMap as data structure for factory relations.
	 * Extending classes must implement {@link #keyClass()}
	 * which returns the Class object of the enum used for key.
	 *
	 * @return The EnumMap.
	 */
	protected final Map<K, FactoryInstantiator<K, V>> createMap() {
		return new EnumMap<K, FactoryInstantiator<K, V>>( this.keyClass() );
	}

	/**
	 * Returns the Class object of the enum class used for keys.
	 * Must be implemented by sub-classes.
	 *
	 * @return The Class object of the enum-key class.
	 */
	protected abstract Class<K> keyClass();
}