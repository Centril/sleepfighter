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
package se.toxbee.sleepfighter.utils.collect;

import se.toxbee.sleepfighter.utils.model.IdProvider;

/**
 * {@link IdObservableList} is an {@link ObservableList} of {@link IdProvider}s.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Dec 14, 2013
 */
public class IdObservableList<E extends IdProvider> extends ObservableList<E> {
	/**
	 * Returns an element with the unique id provided.
	 * 
	 * @param id the unique id of the element.
	 * @return the element, if not found it returns null.
	 */
	public E getById( int id ) {
		for ( E elem : this ) {
			if ( elem.getId() == id ) {
				return elem;
			}
		}

		return null;
	}
}
