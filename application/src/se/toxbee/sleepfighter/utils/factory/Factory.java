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
public interface Factory<K, V> {
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

	/**
	 * Returns a produced/constructed/instantiated object from a related key.
	 *
	 * @param key The key as given by getKeys()
	 * @return The produced object.
	 */
	public V produce( final K key );
}