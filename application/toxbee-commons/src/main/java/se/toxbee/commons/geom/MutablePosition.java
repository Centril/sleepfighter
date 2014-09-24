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
 * Mutable implementation of Position.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since May 27, 2013
 */
public class MutablePosition extends BasePosition {
	/**
	 * Constructs position with given x & y coordinates.
	 *
	 * @param x the x coordinate of new position.
	 * @param y the y coordinate of new position.
	 */
	public MutablePosition( int x, int y ) {
		super( x, y );
	}

	/**
	 * Constructs position copying values from another position.
	 *
	 * @param pos the position to copy from.
	 */
	public MutablePosition( Position pos ) {
		super( pos );
	}

	@Override
	public boolean isMutable() {
		return true;
	}

	@Override
	public Position x( int x ) {
		this.x = x;
		return this;
	}

	@Override
	public Position y( int y ) {
		this.y = y;
		return this;
	}

	@Override
	public Position set( int x, int y ) {
		this.x = x;
		this.y = y;
		return this;
	}

	@Override
	public Position set( Position pos ) {
		this.x = pos.x();
		this.y = pos.y();
		return this;
	}

	@Override
	public Position add( int x, int y ) {
		this.x += x;
		this.y += y;
		return this;
	}

	@Override
	public Position add( Position pos ) {
		this.x += pos.x();
		this.y += pos.y();
		return this;
	}

	@Override
	public Position addX( int x ) {
		this.x += x;
		return this;
	}

	@Override
	public Position addY( int y ) {
		this.y = y;
		return this;
	}

	@Override
	public Position sub( int x, int y ) {
		this.x -= x;
		this.y -= y;
		return this;
	}

	@Override
	public Position sub( Position pos ) {
		this.x -= pos.x();
		this.y -= pos.y();
		return this;
	}

	@Override
	public Position subX( int x ) {
		this.x -= x;
		return this;
	}

	@Override
	public Position subY( int y ) {
		this.y -= y;
		return this;
	}

	@Override
	public Position mul( int factor ) {
		this.x *= factor;
		this.y *= factor;
		return this;
	}

	@Override
	public Position cpy() {
		return new MutablePosition( this );
	}
}