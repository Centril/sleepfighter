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

package se.toxbee.commons.geom;

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
		 * @param given the given dimension.
		 * @param max the max dimension.
		 */
		public UndefinedException( int given, int max ) {
			super( "The given dimension: " + given + ", is undefined - the highest defined is: " + max );
		}
	}

	/**
	 * Returns cross product of the sizes of all dimension.
	 * Equivalent of {@link Dimension#cross(int)} with {@link Dimension#n()}.
	 *
	 * @return the product sum of all dimensions.
	 */
	public int cross();

	/**
	 * Returns cross product of the sizes of up until (inclusive) the n:th dimension.
	 *
	 * @param n the highest n:index (0-indexed) to sum dimension of.
	 * @return the product sum of 0, ..., n:th dimension.
	 */
	public int cross( int n );

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

	/**
	 * Returns whether or not the given position is contained in dimension.
	 *
	 * @param pos the position object.
	 * @return true if this position is contained in dim.
	 */
	public boolean contains( Position pos );

	/**
	 * Returns the lowest value among the axes.
	 *
	 * @return lowest value.
	 */
	public int lowest();

	/**
	 * Returns the highest value among the axes.
	 *
	 * @return highest value.
	 */
	public int highest();
}
