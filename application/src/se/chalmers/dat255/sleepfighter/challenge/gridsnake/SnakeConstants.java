package Snake;

import java.awt.Color;

import Geometry.Dimension;


/**
 * Configuration constants for the game Snake.
 */
public enum SnakeConstants {
	; // Safe Singleton pattern, prevent instantiation.

	/* --------------------------------
	 * Constants.
	 * --------------------------------
	 */
	/** The size of games, that is how many tiles there is in each axis. */
	private static final Dimension SIZE = new Dimension(10, 10);

	/** The size of each tile in game. */
	private static final int TILE_SIZE = 20;

	/** The number of milliseconds (ms) between game updates. */
	private static final int UPDATE_INTERVAL = 150;

	/** The initial length of snake. */
	private static final int INITIAL_SNAKE_LENGTH = 2;

	/** The color of food. */
	private static final Color FOOD_COLOR = new Color( 238, 110, 2 );

	/** The color of the snake. */
	private static final Color SNAKE_COLOR = new Color( 9, 205, 218 );

	/** The color of the snakes head. */
	private static final Color SNAKE_HEAD_COLOR = Color.BLACK;

	/* --------------------------------
	 * Getters.
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
	public static Color getFoodColor() {
		return FOOD_COLOR;
	}

	/**
	 * The color of the snake.
	 *
	 * @return The color of the snake.
	 */
	public static Color getSnakeColor() {
		return SNAKE_COLOR;
	}

	/**
	 * The color of the snakes head.
	 *
	 * @return The color of the snakes head.
	 */
	public static Color getSnakeHeadColor() {
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
}