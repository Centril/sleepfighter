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

package se.toxbee.sleepfighter.challenge.rotosnake;

import android.graphics.Color;

import se.toxbee.commons.geom.Dimension;
import se.toxbee.commons.geom.FinalPosition;

/**
 * Configuration constants for the game Snake. Original author Centril, modified
 * by Laszlo for Sleepfighter.
 */
public enum SnakeConstants {
	; // Safe Singleton pattern, prevent instantiation.

	/*
	 * -------------------------------- Constants.
	 * --------------------------------
	 */
	/** The size of games, that is how many tiles there is in each axis. */
	private static final Dimension SIZE = new FinalPosition(20, 10);

	/** The size of each tile in game. */
	private static final int TILE_SIZE = 1;

	/** The number of milliseconds (ms) between game updates. */
	private static final int UPDATE_INTERVAL = 300;

	/** The initial length of snake. */
	private static final int INITIAL_SNAKE_LENGTH = 3;

	/** The color of food. */
	private static final int FOOD_COLOR = Color.BLUE;

	/** The color of the snake. */
	private static final int SNAKE_COLOR = Color.RED;

	/** The color of the snakes head. */
	private static final int SNAKE_HEAD_COLOR = Color.GREEN;

	/** Points needed to win. */
	private static final int VICTORY_CONDITION = 3;

	/*
	 * -------------------------------- Getters.
	 * --------------------------------
	 */
	/** @return The size of games, that is how many tiles there is in each axis. */
	public static Dimension getGameSize() {
		return SIZE;
	}

	/** @return The size of each tile in game. */
	public static int getTileSize() {
		// Dimension is a mutable class, copy to prevent mutation.
		return TILE_SIZE;
	}

	/** @return The number of milliseconds (ms) between game updates. */
	public static int getUpdateSpeed() {
		return UPDATE_INTERVAL;
	}

	/**
	 * @return The color of food.
	 */
	public static int getFoodColor() {
		return FOOD_COLOR;
	}

	/**
	 * The color of the snake.
	 * 
	 * @return The color of the snake.
	 */
	public static int getSnakeColor() {
		return SNAKE_COLOR;
	}

	/**
	 * The color of the snakes head.
	 * 
	 * @return The color of the snakes head.
	 */
	public static int getSnakeHeadColor() {
		return SNAKE_HEAD_COLOR;
	}

	/**
	 * Returns the initial length of snake.
	 * 
	 * @return The initial length of snake.
	 */
	public static int getInitialSnakeLength() {
		return INITIAL_SNAKE_LENGTH;
	}

	/**
	 * Returns the number of points needed to win.
	 * 
	 * @return The number of points needed to win.
	 */
	public static int getVictoryCondition() {
		return VICTORY_CONDITION;
	}
}