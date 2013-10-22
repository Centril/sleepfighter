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

package se.toxbee.sleepfighter.utils.geometry;

/**
 * Immutable class describing integer 2D-points.
 * Original author Mazdak, modified by Laszlo for SleepFighter.
 */
public class Position implements Comparable<Position>  {
	private final int x;
	private final int y;

	/**
	 * Creates an immutable instance of a 2D integer coordinate.
	 */
	public Position(final int x, final int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * @return The x value of the coordinate.
	 */
	public int getX() {
		return this.x;
	}

	/**
	 * @return The x value of the coordinate.
	 */
	public int getY() {
		return this.y;
	}

	@Override
	public int hashCode() {
		return 23456789 * this.x + 56789123 * this.y;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		Position other = (Position) obj;
		return this.x == other.getX() && this.y == other.getY();
	}

	/** 
     * The method <tt>compareTo</tt> first compares {@link #getX()} of the Position
     * to decide if the given Position is less or greater than this.
     * If they have the same x, {@link #getY()} decides.
     *  
     * @param pos The Position to compare with.
     * @return An integer smaller than 0 if this Position 
     *         is smaller than the Position given as argument,
     *         0 if the sizes are equal, and a positive integer otherwise.
     */
    public int compareTo( Position pos ) {
    	int diff = this.x - pos.getX();
		return diff == 0 ? this.y - pos.getY() : diff;
	}

	public Position moveDirection( Direction d ) {
		return new Position( this.x + d.getXDelta(), this.y + d.getYDelta() );
	}

	/**
	 * Checks whether this position is out of dim:s bounds.
	 *
	 * @param dim Dimension/bounds.
	 * @return true if out of bounds, false otherwise.
	 */
	public boolean isOutOfBounds( Dimension dim ) {
		return	this.getX() < 0 || this.getX() >= dim.getWidth() ||
				this.getY() < 0 || this.getY() >= dim.getHeight();
	}
}
