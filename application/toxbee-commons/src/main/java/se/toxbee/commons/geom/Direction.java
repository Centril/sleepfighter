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
 * Enumeration representing a direction.
 * Has EAST, SOUTHEAST, WEST, SOUTHWEST, NORTHWEST, NORTH, SOUTH, NORTHEAST and NONE directions.
 */
public enum Direction {
	NONE	( 0, 0 ) {
		public Direction getOpposite() {
			return NONE;
		}
	},
	WEST	(-1, 0) {
		public Direction getOpposite() {
			return EAST;
		}
	},
	NORTH_WEST	(-1, -1) {
		public Direction getOpposite() {
			return SOUTH_EAST;
		}
	},
	NORTH	(0, -1) {
		public Direction getOpposite() {
			return SOUTH;
		}
	},
	NORTH_EAST	(1, -1) {
		public Direction getOpposite() {
			return SOUTH_WEST;
		}
	},
	EAST	(1, 0) {
		public Direction getOpposite() {
			return WEST;
		}
	},
	SOUTH_EAST	(1, 1) {
		public Direction getOpposite() {
			return NORTH_WEST;
		}
	},
	SOUTH	(0, 1) {
		public Direction getOpposite() {
			return NORTH;
		}
	},
	SOUTH_WEST	(-1, 1) {
		public Direction getOpposite() {
			return NORTH_EAST;
		}
	};

	public static final Direction[] DEG_90 = { Direction.WEST, Direction.NORTH, Direction.EAST, Direction.SOUTH };
	public static final Direction[] DEG_45 = { Direction.NORTH_WEST, Direction.NORTH_EAST, Direction.SOUTH_EAST, Direction.SOUTH_WEST };
	public static final Direction[] ALL = { Direction.WEST, NORTH_WEST, Direction.NORTH, NORTH_EAST, Direction.EAST, SOUTH_EAST, Direction.SOUTH, SOUTH_WEST };

	/**
	 * Returns all directions that make up all quadrants circle starting from {@link #WEST}.
	 *
	 * @return the directions.
	 */
	public static Direction[] deg90() {
		return DEG_90;
	}

	/**
	 * Returns all directions that make up all quadrants circle starting from {@link #NORTH_WEST}.
	 *
	 * @return the directions.
	 */
	public static Direction[] deg45() {
		return DEG_45;
	}

	/**
	 * Returns all directions except {@link #NONE}.
	 *
	 * @return the directions.
	 */
	public static Direction[] all() {
		return ALL;
	}

	/**
	 * Tests if this direction is opposite to another one.
	 *
	 * @param rhs right-hand-side direction.
	 * @return true if it is opposite.
	 */
	public boolean isOpposite( Direction rhs ) {
		return this.getOpposite() == rhs;
	}

	/**
	 * Returns the opposite direction of this one.
	 *
	 * @return The opposite direction.
	 */
	public Direction getOpposite() {
		return this;
	}

	/**
	 * Returns the delta that this direction implies.
	 *
	 * @return the offset.
	 */
	public FinalPosition delta() {
		return this.delta;
	}

	/**
	 * Returns the delta-x that this direction implies.
	 *
	 * @return the delta-x.
	 */
	public int dx() {
		return this.delta().x();
	}

	/**
	 * Returns the delta-y that this direction implies.
	 *
	 * @return the delta-y.
	 */
	public int dy() {
		return this.delta().y();
	}

	/**
	 * "Constructor" taking delta in x-axis & y-axis.
	 *
	 * @param dx delta-x.
	 * @param dx delta-y.
	 */
	Direction(final int dx, final int dy ) {
		this.delta = new FinalPosition( dx, dy );
	}

	private final FinalPosition delta;
}