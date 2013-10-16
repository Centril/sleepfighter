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

package se.chalmers.dat255.sleepfighter.challenge.fluidsnake;

import android.graphics.Color;

/**
 * Class which contains constants that are needed for the fluid snake game
 * 
 * @author Hassel
 */
public class SnakeConstants {

	// How fast the game should try to update and render
	public static final int TARGET_FPS = 60;

	// Size of the model board
	public static final int BOARD_WIDTH = 200;
	public static final int BOARD_HEIGHT = 400;

	// Multiplier for how fast the game will run in the model
	public static final float SPEED_MULT = 2;

	// Number if circles the snake will be made up by
	public static final int SEGMENTS = 6;
	// radius of the snake segments

	public static final int SNAKE_WIDTH = 15;

	// Starting point for the snake
	public static final int START_X = BOARD_WIDTH / 2;
	public static final int START_Y = 300;

	// How wide the obstacles (walls) will be
	public static final int OBSTACLE_WIDTH = 15;
	// How far into the obstacle the snake can go before dying
	public static final int OBSTACLE_MARGIN = 8;

	// Number of fruits to pick before exit is revealed
	public static final int FRUITS = 3;

	// Radius of the sphere fruits
	public static final int FRUIT_WIDTH = 10;

	// Colors of the entities
	public static final int SNAKE_COLOR = Color.rgb(255, 255, 0);
	public static final int OBSTACLE_COLOR = Color.rgb(200, 200, 200);
	public static final int FRUIT_COLOR = Color.rgb(255, 40, 0);
	public static final int EXIT_COLOR = Color.rgb(30, 255, 40);

	// Don't create an instance of this class
	private SnakeConstants() {
	}
}
