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

package se.toxbee.commons.collect;

import java.util.Collection;

import se.toxbee.commons.geom.Dimension;
import se.toxbee.commons.geom.Position;

/**
 * Grid models a generic two-dimensional collection of elements.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Feb, 16, 2014
 */
public interface Grid<T> extends Collection<T> {
	// Grid Logic Operations

	/**
	 * Returns the dimensions of the grid.
	 *
	 * @return the dimensions.
	 */
	Dimension dim();

	/**
	 * Returns the index of (x, y) in grid.
	 *
	 * @param p the position to use for x, y.
	 * @return the index of (x, y).
	 */
	int index( Position p );

	/**
	 * Returns whether or not (x, y) are in bounds of grid.
	 *
	 * @param pos the position to use for (x, y).
	 * @return true if in bounds.
	 */
	boolean inBounds( Position pos );

	/**
	 * Converts an index to a <strong>immutable</strong> position object.
	 *
	 * @param index the index.
	 * @return the position.
	 */
	Position position( int index );

	/**
	 * Returns whether or not (x, y) are in bounds of grid.
	 *
	 * @param x the x component.
	 * @param y the y component.
	 * @return true if in bounds.
	 */
	boolean inBounds( int x, int y );

	/**
	 * Returns the index of (x, y) in grid.
	 *
	 * @param x the x component.
	 * @param y the y component.
	 * @return the index of (x, y).
	 */
	int index( int x, int y );

	// Positional Access Operations

	/**
	 * Returns the element at (x, y).
	 *
	 * @param x the x component.
	 * @param y the y component.
	 * @return the value.
	 */
	T get( int x, int y );

	/**
	 * Sets the element at (x, y) to val.
	 *
	 * @param x the x component.
	 * @param y the y component.
	 * @param val the value.
	 */
	T set( int x, int y, T val );

	/**
	 * Returns the element at (x, y).
	 *
	 * @param p the position to use for (x, y).
	 * @return the value.
	 */
	T get( Position p );

	/**
	 * Sets the value at (x, y) to val.
	 *
	 * @param p the position to use for (x, y).
	 * @param val the value.
	 */
	T set( Position p, T val );

	/**
	 * Returns the element at index.
	 *
	 * @param index the index.
	 * @return the element.
	 */
	T get( int index );

	/**
	 * Replaces the element at the specified index in this
	 * grid with the specified element. If val == null,
	 * {@link #remove(int)} will be called.
	 *
	 * @param index the index.
	 * @param val the value.
	 */
	T set( int index, T val );

	/**
	 * Removes the element at the specified position in this grid.
	 * Unlike the List interface, this doesn't shift any element.
	 * It simply nullifies the value at index.
	 *
	 * @param index the index to "remove".
	 * @return the previous value at index.
	 */
	T remove( int index );

	// Query operations

	/**
	 * Returns true if every element in the grid is null.
	 *
	 * @return true if empty.
	 */
	@Override
	boolean isEmpty();

	/**
	 * Returns true if every element in the grid is non-null.
	 *
	 * @return true if full.
	 */
	boolean isFull();

	/**
	 * Only adds to the first empty slot,
	 * assuming that {@link #isEmpty()} yields false.
	 *
	 * @param t the element to be set to the first empty slot.
	 * @return true if the grid changed.
	 * @throws IllegalArgumentException When t == null.
	 * @throws IllegalStateException When {@link #isFull()} yields true.
	 */
	@Override
	boolean add( T t );

	// Bulk Modification Operations

	/**
	 * See {@link #add(Object)}
	 *
	 * @param c the elements to be set to the first empty slots.
	 * @return true if the grid changed.
	 * @throws IllegalArgumentException When any element in c is null.
	 * @throws IllegalStateException When at any point during insertion {@link #isFull()} yields true.
	 */
	@Override
	boolean addAll( Collection<? extends T> c );

	/**
	 * Sets all the indexes starting from index and
	 * until the end of the given collection or until {@link #size()}.
	 *
	 * @param index the index from where to start.
	 * @param c the collection to set to the grid.
	 * @return always true.
	 */
	boolean setAll( int index, Collection<? extends T> c );

	// Grid Iterators

	/**
	 * {@inheritDoc}<br/>
	 * Calling {@link java.util.Iterator#remove()} will
	 * never resize the backing datastructure.
	 */
	@Override
	GridIterator<T> iterator();

	/**
	 * Calling {@link java.util.Iterator#remove()} will
	 * never resize the backing datastructure.
	 *
	 * @see #iterator()
	 * @param start the starting index to iterate from.
	 */
	GridIterator<T> iterator( int start );

	// Comparison and hashing

	/**
	 * Compares the specified object with this grid for equality.
	 * Returns <tt>true</tt> if and only if the specified object
	 * is also a grid, both grids have the same size,
	 * and all corresponding pairs of elements in the two grids
	 * are <i>equal</i>. (Two elements <tt>e1</tt> and
	 * <tt>e2</tt> are <i>equal</i> if <tt>(e1==null ? e2==null :
	 * e1.equals(e2))</tt>.)  In other words, two grids are
	 * defined to be equal if they contain the same elements in the same order.
	 * This definition ensures that the equals method works properly across
	 * different implementations of the <tt>Grid</tt> interface.
	 *
	 * @param o the object to be compared for equality with this grids
	 * @return <tt>true</tt> if the specified object is equal to this grids
	 */
	boolean equals( Object o );

	/**
	 * Returns the hash code value for the grid.
	 *
	 * @return the hash code.
	 */
	@Override
	int hashCode();

	// Search Operations

	/**
	 * Returns the index of the first occurrence of the specified element
	 * in this grid, or -1 if this list does not contain the element.
	 * More formally, returns the lowest index <tt>i</tt> such that
	 * <tt>(o==null&nbsp;?&nbsp;get(i)==null&nbsp;:&nbsp;o.equals(get(i)))</tt>,
	 * or -1 if there is no such index.
	 *
	 * @param o element to search for
	 * @return the index of the first occurrence of the specified element in
	 *         this grid, or -1 if this grid does not contain the element
	 * @throws ClassCastException if the type of the specified element
	 *         is incompatible with this grid
	 *         (<a href="Collection.html#optional-restrictions">optional</a>)
	 */
	int indexOf( Object o );

	/**
	 * Returns the index of the last occurrence of the specified element
	 * in this grid, or -1 if this grid does not contain the element.
	 * More formally, returns the highest index <tt>i</tt> such that
	 * <tt>(o==null&nbsp;?&nbsp;get(i)==null&nbsp;:&nbsp;o.equals(get(i)))</tt>,
	 * or -1 if there is no such index.
	 *
	 * @param o element to search for
	 * @return the index of the last occurrence of the specified element in
	 *         this grid, or -1 if this grid does not contain the element
	 * @throws ClassCastException if the type of the specified element
	 *         is incompatible with this grid
	 *         (<a href="Collection.html#optional-restrictions">optional</a>)
	 */
	int lastIndexOf( Object o );
}
