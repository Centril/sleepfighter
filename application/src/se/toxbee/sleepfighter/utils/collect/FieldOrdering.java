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

import com.google.common.collect.Ordering;

/**
 * {@link FieldOrdering} simplifies the creation of Orderings by<br/>
 * comparing the result of {@link #fieldToComparable(Object)}<br/>
 * on the left and right-hand-side.
 *
 * @param <T> The type being compared.
 * @param <C> A {@link Comparable} that is the result of {@link #fieldToComparable(Object)}.
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Dec 14, 2013
 */
public abstract class FieldOrdering<T, C extends Comparable<? super C>> extends Ordering<T> {
	@Override
	public int compare( T lhs, T rhs ) {
		// Compute results.
		C l = this.fieldToComparable( lhs );
		C r = this.fieldToComparable( rhs );

		return l.compareTo( r );
	}

	/**
	 * Converts val to a {@link Comparable}.
	 *
	 * @param val the value to convert.
	 * @return the resulting {@link Comparable}.
	 */
	protected abstract C fieldToComparable( T val );
}