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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import se.toxbee.sleepfighter.utils.geom.Dimension;
import se.toxbee.sleepfighter.utils.geom.Direction;
import se.toxbee.sleepfighter.utils.geom.FinalPosition;
import se.toxbee.sleepfighter.utils.geom.Position;

/**
 * Snake game. Original author Mazdak, modified by Laszlo for SleepFighter.
 */
public class SnakeModel {
	/*
	 * -------------------------------- Position/States.
	 * --------------------------------
	 */
	/** Random Number Generator (RNG) */
	private final Random rng;

	/** The position of the current snake-food. */
	private Position currFoodPos;

	/** A list containing all snake positions. */
	private final Deque<Position> snakePos;

	/** A list containing all empty positions. */
	private final List<Position> emptyPos;

	/** The direction of the snake. */
	private Direction direction;

	/** The number of snake-food eaten. */
	private int score;

	/** The size of the board. */
	private Dimension boardSize;

	/** The size of a tile. */
	private int tileSize;

	/** Whether game is over or not. */
	private boolean gameOver;

	/*
	 * -------------------------------- Getters.
	 * --------------------------------
	 */
	/**
	 * Returns the current food position.
	 * 
	 * @return Returns the current food position.
	 */
	public Position getFoodPosition() {
		return this.currFoodPos;
	}

	/**
	 * Checks if food exists on the board.
	 * 
	 * @return True, if there is food.
	 */
	public boolean hasFood() {
		return currFoodPos != null;
	}

	/**
	 * Returns an immutable view of the collection of snakes positions.
	 * 
	 * @return Collection of snakes positions.
	 */
	public Collection<Position> getSnakePositions() {
		return Collections.unmodifiableCollection(this.snakePos);
	}

	/**
	 * Returns an immutable view of the list of empty positions.
	 *
	 * @return List of empty positions.
	 */
	public List<Position> getEmptyPositions() {
		return Collections.unmodifiableList( this.emptyPos );
	}

	/**
	 * Returns the size of the game board as a Dimension object.
	 * 
	 * @return Size of the game board.
	 */
	public Dimension getBoardSize() {
		return boardSize;
	}

	public boolean isGameOver() {
		return gameOver;
	}

	/**
	 * Returns whether or not a position is the head of the snake. Since snake
	 * always has a head this method will never cause NullPointerException.
	 * 
	 * @return true if position is the head of snake, otherwise false.
	 */
	public boolean isPositionHead(Position pos) {
		return this.snakePos.peekFirst().equals(pos);
	}

	/**
	 * Creates a new model of snake-game.
	 * 
	 * @param size
	 *            The size of the board.
	 * @param startDirection
	 *            The direction where snakes head will be pointed at.
	 * @param rng
	 *            Random Number Generator.
	 */
	public SnakeModel(Dimension size, Direction startDirection, Random rng) {
		// Init RNG.
		this.rng = rng;

		// Set start direction.
		this.direction = startDirection;

		// Set board size.
		this.boardSize = size;

		// Set tile size.
		this.tileSize = SnakeConstants.getTileSize();

		// Set game status.
		this.gameOver = false;

		// Blank out the whole gameboard.
		this.emptyPos = new ArrayList<Position>(size.getWidth()
				* size.getHeight() - this.tileSize);
		for (int i = this.tileSize; i < size.getWidth(); i++) {
			for (int j = this.tileSize; j < size.getHeight(); j++) {
				this.emptyPos.add(new FinalPosition(i, j));
			}
		}

		/*
		 * Insert the snake, starting with first one at the middle.
		 * 
		 * In order not to make it too difficult for client we will add
		 * snake-tiles exceeding the first one to opposite of
		 * starting-direction.
		 * 
		 * Then we must add all exceeding first to the end so that the head (
		 * index = 0 ) is first in starting direction. The starting direction
		 * must then be reset to default.
		 */
		this.snakePos = new LinkedList<Position>();
		this.addSnake(new FinalPosition((int) Math.ceil(size.getWidth() / 2.0),
				(int) Math.ceil(size.getHeight() / 2.0)));

		int initLength = SnakeConstants.getInitialSnakeLength();
		if (initLength > 1) {
			Direction oldDirection = this.direction;
			this.direction = this.direction.getOpposite();

			for (int i = 1; i < initLength; i++) {
				this.addSnake(this.getNextSnakePos());
			}

			this.direction = oldDirection;
		}

		// Add starting snake-food.
		this.addFood();
	}

	/*
	 * -------------------------------- Direction logic.
	 * --------------------------------
	 */
	/**
	 * Update the direction of the snake.
	 * 
	 * @param newDirection The new wanted direction.
	 */
	public void updateDirection(Direction newDirection) {
		// Don't change direction if it is opposite to current one or if it is
		// contained by snake.
		// This is one of the features of THIS snake.
		if (!newDirection.isOpposite(this.direction)
				|| snakePos.contains(snakePos.peek()
						.move(newDirection))) {
			this.direction = newDirection;
		}
	}

	/*
	 * -------------------------------- Tick handling.
	 * --------------------------------
	 */
	/**
	 * Updates the model periodically. Central game logic for snake.
	 * 
	 * @throws GameOverException
	 *             When game is over.
	 */
	public void tickUpdate() throws GameOverException {
		// Get the new head-position of snake.
		Position newHeadPos = this.getNextSnakePos();

		// Check if there's food at the snakes head.
		// If yes: Award client with score and add a new snake-food (if not
		// possible or if reached victory condition -> game over).
		// If not: Transfer the previous snake tail position to empty positions
		// and remove head from empty positions.
		if (isCollision(newHeadPos, this.currFoodPos)) {
			this.score++;
			if (this.emptyPos.isEmpty()
					|| score == SnakeConstants.getVictoryCondition()) {
				this.gameOver();
			} else {
				this.addFood();
			}
		} else {
			this.emptyPos.add(this.snakePos.removeLast());
			this.emptyPos.remove(newHeadPos);
		}

		// Game Over if snake is out of bounds.
		if (isOutOfBounds(newHeadPos)) {
			this.gameOver();
		}

		// Add head at new position.
		this.snakePos.addFirst(newHeadPos);
	}

	/**
	 * Issues game-over by throwing GameOverException.
	 * 
	 * @throws GameOverException
	 */
	private void gameOver() throws GameOverException {
		gameOver = true;
		throw new GameOverException(this.score);
	}

	/**
	 * Transfer first random position from empty ones to currFoodPos.
	 */
	private void addFood() {
		// Randomly select an empty position to remove and set current food
		// position to that one.
		this.currFoodPos = this.emptyPos.get(this.rng.nextInt(this.emptyPos
				.size()));
	}

	/**
	 * Helper method for adding new snake tiles to a position.
	 * 
	 * @param pos
	 *            The position in which to add a snake tile.
	 */
	private void addSnake(Position pos) {
		this.emptyPos.remove(pos);
		this.snakePos.add(pos);
	}

	/**
	 * Get next position of the snake.
	 * 
	 * @return a new Position object representing next position.
	 */
	private Position getNextSnakePos() {
		return this.snakePos.getFirst().move( this.direction );
	}

	/**
	 * @param newHeadPos the new position of the head.
	 * @param otherPos the other position to check against head.
	 * @return true if Snake (newHeadPos) collides with another object
	 *         (typically fruit) occupying otherPos
	 */
	private boolean isCollision(Position newHeadPos, Position otherPos) {
		return Math.abs(newHeadPos.getX() - otherPos.getX()) <= this.tileSize
				&& newHeadPos.getY() == otherPos.getY()
				|| newHeadPos.getX() == otherPos.getX()
				&& Math.abs(newHeadPos.getY() - otherPos.getY()) <= this.tileSize;
	}

	/**
	 * Check if Position pos is out of bounds on Android device.
	 */
	private boolean isOutOfBounds(Position pos) {
		return pos.getX() < tileSize || pos.getY() < tileSize
				|| pos.getY() > boardSize.getHeight() - tileSize
				|| pos.getX() > boardSize.getWidth() - tileSize;
	}

}