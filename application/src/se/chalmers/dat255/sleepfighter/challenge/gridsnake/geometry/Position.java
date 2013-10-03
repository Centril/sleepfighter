package se.chalmers.dat255.sleepfighter.challenge.gridsnake.geometry;

import java.awt.Dimension;


/**
 * Immutable class describing integer 2D-points.
 * 
 * @author evensen
 * 
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
		return this.x == other.x && this.y == other.y;
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
    	int diff = this.getX() - pos.getX();
		return diff == 0 ? this.getY() - pos.getY() : diff;
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
		return	this.getX() < 0 || this.getX() >= dim.width ||
				this.getY() < 0 || this.getY() >= dim.height;
	}
}
