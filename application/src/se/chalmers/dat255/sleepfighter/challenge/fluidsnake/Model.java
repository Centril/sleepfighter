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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import se.chalmers.dat255.sleepfighter.challenge.snake.CircleEntity;
import se.chalmers.dat255.sleepfighter.challenge.snake.RectEntity;
import se.chalmers.dat255.sleepfighter.challenge.snake.Segment;

import android.graphics.Color;
import android.graphics.Paint;

/**
 * Model class representing a fluid snake game.
 * 
 * @author Hassel
 * 
 */
public class Model {
	// size of the game board in the model
	public final int boardWidth = 200;
	public final int boardHeight = 400;

	// multiplier for how fast the game should run
	public final float speedFactor = 2;

	// Snake length and width
	public final int maxSegments = 6;
	public final int snakeWidth = 15;

	// spawn point for the snake
	public final int startX = boardWidth / 2;
	public final int startY = 300;

	// how wide obstacles will be
	public final int obstacleWidth = 15;

	// what margin the snake should have when getting close to the wall before
	// losing
	public final int obstacleMargin = 8;

	// radius of the sphere fruits
	public final int sphereFruitWidth = 10;

	// number of fruits
	public final int nbrOfSphereFruits = 3;

	// Lists containing entities
	private List<Segment> snakeSegments;
	private List<RectEntity> obstacles;
	private List<CircleEntity> sphereFruits;

	// the exit entity
	private RectEntity exit;

	private boolean started;
	private boolean lost;
	private boolean won;

	// colors for the entities
	private Paint snakePaint;
	private Paint obstaclePaint;
	private Paint sphereFruitPaint;
	private Paint exitPaint;

	// normalized direction for the snake
	private float dx;
	private float dy;

	// updated each time the update() method is called (used for deciding when
	// to spawn new segments)
	private float modelTime;

	public Model() {
		// set the colors
		snakePaint = new Paint();
		snakePaint.setColor(Color.rgb(255, 255, 0));

		obstaclePaint = new Paint();
		obstaclePaint.setColor(Color.rgb(200, 200, 200));

		sphereFruitPaint = new Paint();
		sphereFruitPaint.setColor(Color.rgb(255, 40, 0));

		exitPaint = new Paint();
		exitPaint.setColor(Color.rgb(30, 255, 40));

		modelTime = 0;

		// initialize the snake and add the head segment
		snakeSegments = new ArrayList<Segment>();
		snakeSegments.add(new Segment(startX, startY, snakeWidth, snakePaint));

		// initialize and add obstacles
		obstacles = new ArrayList<RectEntity>();
		obstacles.add(new RectEntity(0, 0, boardWidth, obstacleWidth,
				obstaclePaint));
		obstacles.add(new RectEntity(0, 0, obstacleWidth, boardHeight,
				obstaclePaint));
		obstacles.add(new RectEntity(boardWidth - obstacleWidth, 0,
				obstacleWidth, boardHeight, obstaclePaint));
		obstacles.add(new RectEntity(0, boardHeight - obstacleWidth,
				boardWidth, obstacleWidth, obstaclePaint));

		// initialize and add randomized fruit entities
		sphereFruits = new ArrayList<CircleEntity>();
		Random rand = new Random();
		for (int i = 0; i < nbrOfSphereFruits; i++) {
			sphereFruits.add(new CircleEntity(rand.nextInt(boardWidth - 2
					* (obstacleWidth + sphereFruitWidth))
					+ obstacleWidth + sphereFruitWidth, rand
					.nextInt(boardHeight - 2
							* (obstacleWidth + sphereFruitWidth))
					+ obstacleWidth + sphereFruitWidth, sphereFruitWidth,
					sphereFruitPaint));
		}

		started = false;
		lost = false;
		won = false;
	}

	/**
	 * @return true if the player has won, false otherwise
	 */
	public boolean won() {
		return won;
	}

	/**
	 * @return true if the player has lost, false otherwise
	 */
	public boolean lost() {
		return lost;
	}

	/**
	 * Updates the direction the snake is heading towards
	 * 
	 * @param touchX
	 *            x coordinate of where the snake should head towards (0 <= x <=
	 *            1)
	 * @param touchY
	 *            y coordinate of where the snake should head towards (0 <= y <=
	 *            1)
	 */
	public void updateDirection(float touchX, float touchY) {
		// current location of the snake head
		float segX = snakeSegments.get(0).getX();
		float segY = snakeSegments.get(0).getY();

		// exact location on the gameboard of where the snake should head
		// towards
		float nextX = touchX * boardWidth;
		float nextY = touchY * boardHeight;

		// difference of the snake location and target location
		float relativeX = nextX - segX;
		float relativeY = nextY - segY;

		// used to normalize the vector so that the snake always moves the same
		// distance
		float hypotenuse = (float) Math.sqrt(relativeX * relativeX + relativeY
				* relativeY);

		if (hypotenuse != 0) {
			// normalize the vector V = [dx, dy]
			dx = relativeX / hypotenuse;
			dy = relativeY / hypotenuse;

			// Game on!
			started = true;
		}
	}

	/**
	 * Updates the model
	 * 
	 * @param delta
	 *            the delay multiplier (targeted update speed divided by actual
	 *            update speed, i.e 1 would be the normal case)
	 */
	public void update(float delta) {
		if (started) {

			// if the delta is too high (in this case 5 times lower update speed
			// than intended)
			if (delta >= 5) {
				delta = 5;
			}
			delta *= speedFactor;

			// update the snake head's position
			snakeSegments.get(0).setX(snakeSegments.get(0).getX() + dx * delta);
			snakeSegments.get(0).setY(snakeSegments.get(0).getY() + dy * delta);

			// add the head position to the x and y-lists
			snakeSegments.get(0).getXList().add(snakeSegments.get(0).getX());
			snakeSegments.get(0).getYList().add(snakeSegments.get(0).getY());

			// for each snake segment, position them at the place the head was
			// at that current iteration
			for (int i = 1; i < snakeSegments.size(); i++) {
				snakeSegments.get(i).setX(
						snakeSegments.get(0).getXList()
								.get(snakeSegments.get(i).getIter()));
				snakeSegments.get(i).setY(
						snakeSegments.get(0).getYList()
								.get(snakeSegments.get(i).getIter()));

				// increment the iteration
				snakeSegments.get(i).iter();
			}

			// increment the iteration for the head as well
			snakeSegments.get(0).iter();

			// do this only if all snake segments have not been spawned
			if (snakeSegments.size() < maxSegments) {
				// update the model time
				modelTime += delta;

				// if the model time is high enough we spawn a new segment and
				// reset the model time
				if (modelTime >= (snakeSegments.get(0).getRadius() * 2)) {
					snakeSegments.add(new Segment(startX, startY, snakeWidth,
							snakePaint));
					modelTime -= snakeSegments.get(0).getRadius() * 2;
				}
			}

			handleCollision();
		}
	}

	private void handleCollision() {
		Segment head = snakeSegments.get(0);

		// if the last segment is outside the top of the map, that means the
		// player won
		if (snakeSegments.get(snakeSegments.size() - 1).getY() + snakeWidth <= 0) {
			won = true;
		}

		// check if the head collides with its body
		for (int i = 1; i < snakeSegments.size(); i++) {
			if (Math.abs((snakeSegments.get(i).getX() - snakeSegments.get(0)
					.getX())) <= head.getRadius() / 2
					&& Math.abs((snakeSegments.get(i).getY() - snakeSegments
							.get(0).getY())) <= head.getRadius() / 2) {
				lost = true;
				break;
			}
		}

		// if an exit doesn't exist, or if there is an exit and the snake head
		// is not on it's position, check
		// for collision wth obstacles
		if (!(exit != null
				&& 0 < head.getX() - head.getRadius() - exit.getX()
				&& 0 < exit.getX() + exit.getWidth() - head.getRadius()
						- head.getX()
				&& 0 < head.getY() + head.getRadius() - exit.getY() && 0 < exit
				.getY() + exit.getHeight() + head.getRadius() - head.getY())) {

			// check for obstacle collision
			for (int i = 0; i < obstacles.size(); i++) {
				RectEntity o = obstacles.get(i);
				if (obstacleMargin < head.getX() + head.getRadius() - o.getX()
						&& obstacleMargin < o.getX() + o.getWidth()
								+ head.getRadius() - head.getX()
						&& obstacleMargin < head.getY() + head.getRadius()
								- o.getY()
						&& obstacleMargin < o.getY() + o.getHeight()
								+ head.getRadius() - head.getY()) {
					lost = true;
					break;
				}
			}
		}

		// check for collision with the healthy fruits
		for (int i = 0; i < sphereFruits.size(); i++) {
			if (Math.abs(sphereFruits.get(i).getX() - head.getX()) <= head
					.getRadius()
					&& Math.abs(sphereFruits.get(i).getY() - head.getY()) <= head
							.getRadius()) {
				sphereFruits.remove(i);
				break;
			}
		}

		// if there are no fruits left, spawn the exit
		if (sphereFruits.isEmpty() && exit == null) {
			exit = new RectEntity(boardWidth / 2, 0, 50, 30, exitPaint);
		}
	}

	/**
	 * @return a list containing all sphere fruits
	 */
	public List<CircleEntity> getSphereFruits() {
		return sphereFruits;
	}

	/**
	 * @return a list containing all obstacles
	 */
	public List<RectEntity> getObstacles() {
		return obstacles;
	}

	/**
	 * @return a list containing all snake segments (the first element is the
	 *         head)
	 */
	public List<Segment> getSnakeSegments() {
		return snakeSegments;
	}

	/**
	 * @return the exit (= the goal)
	 */
	public RectEntity getExit() {
		return exit;
	}
}