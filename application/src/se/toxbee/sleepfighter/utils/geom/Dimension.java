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
package se.toxbee.sleepfighter.utils.geom;

/**
 * Dimension represents an object that contains the integer sizes of various dimensions.<br/>
 * The dimension is at least 2-dimensional.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Nov 4, 2013
 */
public interface Dimension {
	public class UndefinedException extends RuntimeException {
		private static final long serialVersionUID = -5639755423022789024L;

		/**
		 * Constructs a new {@code UndefinedException}.
		 * 
		 * @param detailMessage the detail message for this exception.
		 */
		public UndefinedException( int given, int max ) {
			super( "The given dimension: " + given + ", is undefined - the highest defined is: " + max );
		}
	}

	/**
	 * Returns the size of the n:th dimension.
	 *
	 * @param n the dimension n. 0-indexed. e.g: x = 0, y = 1, z = 2...
	 * @return the size.
	 * @throws UndefinedException when n > {@link #n()}.
	 */
	public int size( int n );

	/**
	 * Alias of {@link #size(int)}.
	 */
	public int getSize( int n );

	/**
	 * Returns the index of largest dimension n.
	 *
	 * @return the index.
	 */
	public int n();

	/**
	 * Alias of {@link #n()}.
	 */
	public int getN();

	/**
	 * Returns the 0:th dimension size, AKA x-axis size.<br/>
	 * The same as {@link #size(int)} with 0.
	 *
	 * @return the width.
	 */
	public int width();

	/**
	 * Returns the 1:th dimension size, AKA y-axis size.<br/>
	 * The same as {@link #size(int)} with 1.
	 *
	 * @return the height.
	 */
	public int height();

	/**
	 * Alias of {@link #width()}.
	 */
	public int getWidth();

	/**
	 * Alias of {@link #height()}.
	 */
	public int getHeight();
}
