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

import java.util.ListIterator;

/**
 * GridIterator is the iterator interface for {@link Grid}.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Feb, 16, 2014
 */
public interface GridIterator<T> extends ListIterator<T> {
	// Query Operations

	/**
	 * Returns {@code true} if this grid iterator has more elements when
	 * traversing the grid in the forward direction. (In other words,
	 * returns {@code true} if {@link #next} would return an element rather
	 * than throwing an exception.)
	 *
	 * @return {@code true} if the grid iterator has more elements when
	 *         traversing the grid in the forward direction
	 */
	boolean hasNext();

	/**
	 * Returns the next element in the grid and advances the cursor position.
	 * This method may be called repeatedly to iterate through the grid,
	 * or intermixed with calls to {@link #previous} to go back and forth.
	 * (Note that alternating calls to {@code next} and {@code previous}
	 * will return the same element repeatedly.)
	 *
	 * @return the next element in the grid
	 * @throws java.util.NoSuchElementException if the iteration has no next element
	 */
	T next();

	/**
	 * Returns {@code true} if this grid iterator has more elements when
	 * traversing the grid in the reverse direction.  (In other words,
	 * returns {@code true} if {@link #previous} would return an element
	 * rather than throwing an exception.)
	 *
	 * @return {@code true} if the grid iterator has more elements when
	 *         traversing the grid in the reverse direction
	 */
	boolean hasPrevious();

	/**
	 * Returns the previous element in the grid and moves the cursor
	 * position backwards.  This method may be called repeatedly to
	 * iterate through the grid backwards, or intermixed with calls to
	 * {@link #next} to go back and forth.  (Note that alternating calls
	 * to {@code next} and {@code previous} will return the same
	 * element repeatedly.)
	 *
	 * @return the previous element in the grid
	 * @throws java.util.NoSuchElementException if the iteration has no previous
	 *         element
	 */
	T previous();

	/**
	 * Returns the index of the element that would be returned by a
	 * subsequent call to {@link #next}. (Returns grid size if the grid
	 * iterator is at the end of the grid.)
	 *
	 * @return the index of the element that would be returned by a
	 *         subsequent call to {@code next}, or grid size if the grid
	 *         iterator is at the end of the grid
	 */
	int nextIndex();

	/**
	 * Returns the index of the element that would be returned by a
	 * subsequent call to {@link #previous}. (Returns -1 if the grid
	 * iterator is at the beginning of the grid.)
	 *
	 * @return the index of the element that would be returned by a
	 *         subsequent call to {@code previous}, or -1 if the grid
	 *         iterator is at the beginning of the grid
	 */
	int previousIndex();

	// Modification Operations

	/**
	 * Nullifies the last element that was returned by
	 * {@link #next()} or {@link #remove()} (optional operation).<br/>
	 * This call can be made as many times as wanted per next/previous.
	 *
	 * <p>Calling {@link java.util.Iterator#remove()} will
	 * never resize the backing datastructure.</p>
	 *
	 * @throws UnsupportedOperationException if the {@code remove}
	 *         operation is not supported by this grid iterator
	 */
	@Override
	void remove();

	/**
	 * Replaces the last element returned by {@link #next} or
	 * {@link #previous} with the specified element (optional operation).
	 *
	 * This call can be made as many times as wanted per next/previous.
	 *
	 * @param e the element with which to replace the last element returned by
	 *          {@code next} or {@code previous}
	 * @throws UnsupportedOperationException if the {@code set} operation
	 *         is not supported by this grid iterator
	 * @throws IllegalArgumentException if some aspect of the specified
	 *         element prevents it from being added to this grid
	 */
	void set( T e );

	/**
	 * Alias of {@link #set(Object)}.
	 *
	 * @param e the element with which to replace the last element returned by
	 *          {@code next} or {@code previous}
	 */
	@Override
	void add( T t );
}