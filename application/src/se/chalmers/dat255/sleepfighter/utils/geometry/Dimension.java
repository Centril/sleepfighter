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

package se.chalmers.dat255.sleepfighter.utils.geometry;

/**
 * Implementation of an immutable dimension class.
 * Original author Mazdak, modified by Laszlo for SleepFighter
 */
public class Dimension {
	private static final long serialVersionUID = -1845270057681504248L;

	private int width, height;
	
	/**
	 * Creates an instance of <code>Dimension</code>
	 * with a width of zero and a height of zero.
	 */
	public Dimension() {
		this( 0, 0 );
	}

	/**
	 * Creates an instance of <code>Dimension</code> whose width
	 * and height are the same as for the specified dimension.
	 *
	 * @param d The specified dimension for the <code>width</code> and <code>height</code> values
	 */
	public Dimension( Dimension d ) {
		this( d.getWidth(), d.getHeight() );
	}

	/**
	 * Constructs a <code>Dimension</code> and initializes
	 * it to the specified width and specified height.
	 *
	 * @param width the specified width
	 * @param height the specified height
	 */
	public Dimension( int width, int height ) {
		this.width = width;
		this.height = height;
	}

	public int getHeight(){
		return height;
	}
	
	public int getWidth(){
		return width;
	}
}
