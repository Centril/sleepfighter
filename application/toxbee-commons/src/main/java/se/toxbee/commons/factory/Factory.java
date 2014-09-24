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

package se.toxbee.commons.factory;

import java.util.Set;

/**
 * A factory interface that given a key can produce a well-defined object.
 *
 * @param <K> Key type restriction.
 * @param <V> Value type restriction. All items produced from factory must be at least of this super-class.
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @since 2012-12-14
 * @version 1.1
 */
public interface Factory<K, V> extends IFactory<K, V> {
	/**
	 * Returns set with public keys relating to what this factory can produce.
	 *
	 * @return List of keys relating to what factory can produce.
	 */
	public Set<K> getKeys();

	/**
	 * Alias for {@link #produce(K)}
	 */
	public V get( final K key );
}