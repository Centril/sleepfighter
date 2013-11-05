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

import java.util.Iterator;
import java.util.NoSuchElementException;

import se.toxbee.sleepfighter.utils.geom.Dimension;
import se.toxbee.sleepfighter.utils.geom.FinalPosition;
import se.toxbee.sleepfighter.utils.geom.Position;
import se.toxbee.sleepfighter.utils.reflect.ReflectionUtil;

/**
 * Grid models a generic 2d matrix of elements.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Nov 5, 2013
 */
public class Grid<T> implements Iterable<T> {
	private T[] g;
	private Dimension dim;

	/**
	 * Constructs an Grid of width & height size.<br/>
	 * The cells will be set to grid, make sure that <code>size(grid) = width * height</code>
	 *
	 * @param grid the cells to use, not copied.
	 * @param dim the Dimension containing width & height information.
	 */
	public Grid( T[] grid, Dimension dim ) {
		this.g = grid;
		this.dim = dim;
	}

	/**
	 * Constructs a Grid of width & height size.
	 *
	 * @param clazz the Class object for type T.
	 * @param dim the Dimension containing width & height information.
	 */
	public Grid( Class<T> clazz, Dimension dim ) {
		this( ReflectionUtil.makeArray( clazz, dim.size() ), dim );
	}

	/**
	 * Copy constructor.
	 *
	 * @param rhs the other Grid.
	 */
	public Grid( Grid<T> rhs ) {
		this( rhs.g.clone(), rhs.dim );
	}

	/**
	 * Makes a copy of rhs.
	 *
	 * @param rhs the other grid.
	 * @param cpyArr copy the contents of grid, or just dimensions?
	 * @return the new grid.
	 */
	public static <T> Grid<T> cpy( Grid<T> rhs, boolean cpyArr ) {
		return cpyArr ? new Grid<T>( rhs ) : new Grid<T>( ReflectionUtil.arrayClass( rhs.g ), rhs.dim );
	}

	/**
	 * Returns the dimensions of the grid.
	 *
	 * @return the dimensions.
	 */
	public Dimension dim() {
		return this.dim;
	}

	/**
	 * Converts an index to a <strong>immutable</strong> position object.
	 *
	 * @param index the index.
	 * @return the position.
	 */
	public Position position( int index ) {
		int y = index / dim.width();
		int x = index % dim.width();
		return new FinalPosition( x, y );
	}

	/**
	 * Returns the value at index.
	 *
	 * @param index the index.
	 * @return the value.
	 */
	public T get( int index ) {
		return this.g[index];
	}

	/**
	 * Sets the value at index to val.
	 *
	 * @param index the index.
	 * @param val the value.
	 */
	public void set( int index, T val ) {
		this.g[index] = val;
	}

	/**
	 * Returns the value at (x, y).
	 *
	 * @param x the x component.
	 * @param y the y component.
	 * @return the value.
	 */
	public T get( int x, int y ) {
		return this.get( this.index( x, y ) );
	}

	/**
	 * Sets the value at (x, y) to val.
	 *
	 * @param x the x component.
	 * @param y the y component.
	 * @param val the value.
	 */
	public void set( int x, int y, T val ) {
		this.set( this.index( x, y ), val );
	}

	/**
	 * Returns the index of (x, y) in grid.
	 *
	 * @param x the x component.
	 * @param y the y component.
	 * @return the index of (x, y).
	 */
	public int index( int x, int y ) {
		return y * this.dim.width() + x;
	}

	/**
	 * Returns whether or not (x, y) are in bounds of grid.
	 *
	 * @param x the x component.
	 * @param y the y component.
	 * @return true if in bounds.
	 */
	public boolean inBounds( int x, int y ) {
		return this.inBounds( new FinalPosition( x, y ) );
	}

	/**
	 * Returns the index of (x, y) in grid.
	 *
	 * @param pos the position to use for x, y.
	 * @return the index of (x, y).
	 */
	public int index( Position p ) {
		return this.index( p.x(), p.y() );
	}

	/**
	 * Returns the value at (x, y).
	 *
	 * @param pos the position to use for x, y.
	 * @return the value.
	 */
	public T get( Position p ) {
		return this.get( this.index( p ) );
	}

	/**
	 * Sets the value at (x, y) to val.
	 *
	 * @param pos the position to use for x, y.
	 * @param val the value.
	 */
	public void set( Position p, T val ) {
		this.set( this.index( p ), val );
	}

	/**
	 * Returns whether or not (x, y) are in bounds of grid.
	 *
	 * @param pos the position to use for x, y.
	 * @return true if in bounds.
	 */
	public boolean inBounds( Position pos ) {
		return this.dim.contains( pos );
	}

	/**
	 * Returns whether or not the given index is valid.
	 *
	 * @param index the index to test validity for.
	 * @return true if valid.
	 */
	public boolean isValid( int index ) {
		return 0 <= index && index < this.g.length;
	}

	/**
	 * {@inheritDoc}<br/>
	 * The iterator does not support removal.
	 */
	@Override
	public Iterator<T> iterator() {
		return new GridIterator();
	}

	/**
	 * GridIterator is the iterator implementation for Grid.
	 *
	 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
	 * @version 1.0
	 * @since Nov 5, 2013
	 */
	protected class GridIterator implements Iterator<T> {
		private int pos;

		@Override
		public boolean hasNext() {
            return pos + 1 < g.length;
		}

		@Override
		public T next() {
			try {
				return get( pos++ + 1 );
			} catch ( ArrayIndexOutOfBoundsException e ) {
				throw new NoSuchElementException();
			}
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException( "Can't remove, Grid<T> has fixed size." );
		}
	}
}