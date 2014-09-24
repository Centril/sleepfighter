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

import java.util.Objects;

/**
 * Position is a two-dimensional position class.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since May 26, 2013
 */
public abstract class BasePosition implements Position  {
	protected int x, y;

	/**
	 * Constructs position with given x & y coordinates.
	 *
	 * @param x the x coordinate of new position.
	 * @param y the y coordinate of new position.
	 */
	public BasePosition( int x, int y ) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Constructs position copying values from another position.
	 *
	 * @param pos the position to copy from.
	 */
	public BasePosition( Position pos ) {
		this( pos.x(), pos.y() );
	}

	@Override
	public int x() {
		return this.x;
	}

	@Override
	public int y() {
		return this.y;
	}
	
	@Override
	public int getX() {
		return this.x();
	}

	@Override
	public int getY() {
		return this.y();
	}

	@Override
	public Position setX( int x ) {
		return this.x( x );
	}

	@Override
	public Position setY( int y ) {
		return this.y( y );
	}

	@Override
	public Position distance( Position rhs ) {
		return this.sub( rhs );
	}

	@Override
	public Position scale( int factor ) {
		return this.mul( factor );
	}

	@Override
	public Position move( Direction direction ) {
		return this.add( direction.delta() );
	}

	@Override
	public Position move( Direction direction, int scale ) {
		return this.add( direction.delta().mul( scale ) );
	}

	@Override
	public int[] values() {
		return new int[] { this.x, this.y };
	}

	public int compareTo( Position rhs ) {
		int dx = this.x() - rhs.x();
		return dx == 0 ? this.y() - rhs.y() : dx;
	}

    @Override
	public boolean contains( Position pos ) {
    	return pos.containedIn( this );
	}

	@Override
	public boolean containedIn( Position dim ) {
		return this.inRange( this.x(), dim.x() ) && this.inRange( this.y(), dim.y() );
	}

	protected boolean inRange( int val, int max ) {
		return val >= 0 && val < max;
	}

	public boolean isAdjacent( Position rhs ) {
		int n = Math.min( this.n(), rhs.n() );
		for ( int i = 0; i <= n; ++i ) {
			if ( Math.abs( this.size( i ) - rhs.size( i ) ) > 1 ) {
				return false;
			}
		}

		return true;
	}

	public Position clone() {
		return this.cpy();
	}

	public String toString() {
		return "(" + this.x() + "," + this.y() + ")";
	}

	public boolean equals( Object obj ) {
		if ( this == obj ) {
			return true;
		}

		if ( obj instanceof Position ) {
			Position rhs = (Position) obj;
			return this.x() == rhs.x() && this.y() == rhs.y();
		}

		return false;
	}

	public int hashCode() {
		return Objects.hash( this.x(), this.y() );
	}

	@Override
	public int getN() {
		return this.n();
	}

	@Override
	public int getSize( int n ) {
		return this.size( n );
	}

	@Override
	public int width() {
		return this.x();
	}

	public int getWidth() {
		return this.x();
	}

	@Override
	public int height() {
		return this.y();
	}

	public int getHeight() {
		return this.y();
	}

	@Override
	public int size( int n ) {
		switch( n ) {
		case 0:
			return this.x();

		case 1:
			return this.y();

		default:
			throw new Dimension.UndefinedException( n, this.n() );
		}
	}

	@Override
	public int n() {
		return 1;
	}

	public int cross() {
		return this.cross( this.n() );
	}

	public int cross( int n ) {
		int s = 1;

		for ( int i = 0; i <= n; ++i ) {
			s *= this.size( i );
		}

		return s;
	}

	public int lowest() {
		int m = Integer.MAX_VALUE,
			n = this.n();

		for ( int i = 0; i <= n; ++i ) {
			int v = this.size( i );
			if ( v < m ) {
				m = v;
			}
		}

		return m;
	}

	public int highest() {
		int m = Integer.MIN_VALUE,
			n = this.n();

		for ( int i = 0; i <= n; ++i ) {
			int v = this.size( i );
			if ( v > m ) {
				m = v;
			}
		}

		return m;
	}
}