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
package se.chalmers.dat255.sleepfighter.utils.math;

/**
 * Grid models a two-dimensional matrix of integers with width & height.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Oct 14, 2013
 */
public class IntGrid {
	private int[] g;
	private int w, h;

	/**
	 * Constructs an IntGrid of width & height size.<br/>
	 * The cells will be set to grid, make sure that <code>size(grid) = width * height</code>
	 *
	 * @param grid the cells to use, not copied.
	 * @param width the width.
	 * @param height the height.
	 */
	public IntGrid( int[] grid, int width, int height ) {
		this.g = grid;
		this.w = width;
		this.h = height;
	}

	/**
	 * Constructs a IntGrid of width & height size.<br/>
	 * All cells are initialized to 0.
	 *
	 * @param width the width.
	 * @param height the height.
	 */
	public IntGrid( int width, int height ) {
		this( new int[width * height], width, height );
	}

	/**
	 * Copy constructor.
	 *
	 * @param rhs the other IntGrid.
	 */
	public IntGrid( IntGrid rhs ) {
		this( rhs.g.clone(), rhs.w, rhs.h );
	}

	/**
	 * Makes a copy of rhs.
	 *
	 * @param rhs the other grid.
	 * @param cpyArr copy the contents of grid, or just dimensions?
	 * @return the new grid.
	 */
	public static IntGrid cpy( IntGrid rhs, boolean cpyArr ) {
		return cpyArr ? new IntGrid( rhs ) : new IntGrid( rhs.w, rhs.h );
	}

	/**
	 * Returns the width of the grid.
	 *
	 * @return the width.
	 */
	public int width() {
		return w;
	}

	/**
	 * Returns the size of the grid.
	 *
	 * @return the size.
	 */
	public int size() {
		return this.g.length;
	}

	/**
	 * Returns the height of the grid.
	 *
	 * @return the height.
	 */
	public int height() {
		return h;
	}

	/**
	 * Returns the value at index.
	 *
	 * @param index the index.
	 * @return the value.
	 */
	public int get( int index ) {
		return this.g[index];
	}

	/**
	 * Sets the value at index to val.
	 *
	 * @param index the index.
	 * @param val the value.
	 */
	public void set( int index, int val ) {
		this.g[index] = val;
	}

	/**
	 * Returns the value at (x, y).
	 *
	 * @param x the x component.
	 * @param y the y component.
	 * @return the value.
	 */
	public int get( int x, int y ) {
		return this.get( this.index( x, y ) );
	}

	/**
	 * Sets the value at (x, y) to val.
	 *
	 * @param x the x component.
	 * @param y the y component.
	 * @param val the value.
	 */
	public void set( int x, int y, int val ) {
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
		return y * this.w + x;
	}

	/**
	 * Returns whether or not (x, y) are in bounds of grid.
	 *
	 * @param x the x component.
	 * @param y the y component.
	 * @return true if in bounds.
	 */
	public boolean inBounds( int x, int y ) {
		return this.inCoordinate( x, this.w ) && this.inCoordinate( y, this.h );
	}

	/**
	 * Enforces the bounds of grid throwing an exception if (x, y) are out of bounds.
	 *
	 * @param x the x component.
	 * @param y the y component.
	 */
	public void enforceBounds( int x, int y ) {
		if ( this.inBounds( x, y ) ) {
			throw new IllegalArgumentException( "Coordinates are out of bounds" );
		}
	}

	/**
	 * Returns whether or not: c >= 0 && c < dim
	 *
	 * @param c the coordinate component.
	 * @param dim the dimension component.
	 * @return true if in range.
	 */
	public boolean inCoordinate( int c, int dim ) {
		return c >= 0 && c < dim;
	}
}