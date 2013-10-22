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

import java.util.Random;

/**
 * Enum representing a direction. Has EAST, SOUTHEAST, WEST, SOUTHWEST,
 * NORTHWEST, NORTH, SOUTH, NORTHEAST and NONE directions.
 */
public enum Direction {
	/*
	 * IMPORTANT: CHANGING THE ORDER/ORDINALITY OF THESE WILL BREAK
	 * IMPLEMENTATION OF getOpposite().
	 */
	EAST(1, 0), SOUTHEAST(1, 1), SOUTH(0, 1), SOUTHWEST(-1, 1), WEST(-1, 0), NORTHWEST(
			-1, -1), NORTH(0, -1), NORTHEAST(1, -1), NONE(0, 0);

	/**
	 * The amount of steps to move in x-axis.
	 */
	private final int xDelta;

	/**
	 * The amount of steps to move in y-axis.
	 */
	private final int yDelta;

	/**
	 * "Constructor" taking delta in x-axis & y-axis.
	 * 
	 * @param xDelta
	 *            Integer value representing how much to move in x-axis.
	 * @param yDelta
	 *            Integer value representing how much to move in y-axis.
	 */
	Direction(final int xDelta, final int yDelta) {
		this.xDelta = xDelta;
		this.yDelta = yDelta;
	}

	/**
	 * Getter: for xDelta.
	 * 
	 * @return value of xDelta property.
	 */
	public int getXDelta() {
		return this.xDelta;
	}

	/**
	 * Getter: for yDelta.
	 * 
	 * @return value of yDelta property.
	 */
	public int getYDelta() {
		return this.yDelta;
	}

	/**
	 * Tests if this direction is opposite to another one.
	 * 
	 * @param direction
	 *            "Another one"
	 * @return True if it is opposite.
	 */
	public boolean isOpposite(Direction direction) {
		return direction.getXDelta() + getXDelta() == 0
				&& direction.getYDelta() + getYDelta() == 0;
	}

	/**
	 * Returns the opposite direction of this one.
	 * 
	 * @return The opposite direction.
	 */
	public Direction getOpposite() {
		/*
		 * Since Direction is an enum it's not possible to construct anything.
		 * The way the elements are ordered now enables us to use +4 & modulo-8
		 * logic. There's one exception: the opposite of NONE is NONE.
		 * 
		 * +4 & modulo-8 logic goes as follows: The opposite of each Direction
		 * is always +4 ordinal steps "this". However, when > the 4th one (3th
		 * index) we have to go backwards. Due to NONE we can't use % operator.
		 */
		return this == NONE ? NONE : values()[this.ordinal()
				+ (this.ordinal() < 4 ? 4 : -4)];
	}

	/**
	 * Returns random non-NONE Direction with the specified Random object.
	 * 
	 * @param random
	 *            A Random object to generate the Direction.
	 * @return A random non-NONE Direction.
	 */
	public static Direction getRandom(Random random) {
		int dir = random.nextInt(3);
		switch (dir) {
		case 0:
			return EAST;
		case 1:
			return SOUTH;
		case 2:
			return WEST;
		case 3:
			return NORTH;
		default:
			return EAST;
		}
	}
}