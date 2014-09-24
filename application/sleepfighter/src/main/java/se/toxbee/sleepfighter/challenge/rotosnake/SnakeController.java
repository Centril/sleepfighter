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

import android.content.Context;
import android.util.Log;

import java.beans.PropertyChangeSupport;
import java.util.Random;

import se.toxbee.commons.geom.Direction;

/**
 * Controller class for Snake. Original author Mazdak, modified by Laszlo for
 * SleepFighter.
 */
public class SnakeController {
	private static final String TAG = SnakeController.class.getSimpleName();

	/** Random Number Generator (RNG) */
	private final Random rng;

	/** Model of Snake game to be run. */
	private SnakeModel model;

	/** View for the game of Snake */
	private SnakeView view;

	private PropertyChangeSupport pcs;

	private SnakeThread thread;

	// Needed to pass forward.
	private Context context;

	/**
	 * Constructs the controller.
	 */
	public SnakeController(Context context) {
		this.rng = new Random();

		this.pcs = new PropertyChangeSupport(this);

		this.thread = new SnakeThread();

		this.context = context;
		this.init();
	}

	public SnakeController(RotoSnakeChallenge challenge, Context context) {
		this(context);

		this.pcs.addPropertyChangeListener(challenge);
	}

	public long updateSpeed() {
		return SnakeConstants.getUpdateSpeed();
	}

	protected void init() {
		this.model = new SnakeModel(SnakeConstants.getGameSize(),
				Direction.NORTH, this.rng);

		this.view = new SnakeView(this.context, this.model);

		thread.start();
	}

	public void update() {
		try {
			SnakeModel model = (SnakeModel) this.model;
			model.tickUpdate();
		} catch (GameOverException e) {
			if (e.getScore() >= SnakeConstants.getVictoryCondition()) {
				this.stopGame(e.getScore());
				this.thread.setRunning(false);
			} else {
				resetGame();
			}
		}
	}

	private void stopGame(int score) {
		pcs.firePropertyChange(null, null, null);
	}

	public void update(Direction dir) {
		if (dir != Direction.NONE) {
			((SnakeModel) this.model).updateDirection(dir);
		}
	}

	public SnakeView getView() {
		return view;
	}

	/**
	 * Call when activity/game pauses. If not called, game will keep running.
	 */
	public void pause() {
		this.thread.setRunning(false);
	}

	/**
	 * Call when activity/game resumes.
	 */
	public void resume() {
		this.thread = new SnakeThread();
	}

	private class SnakeThread extends Thread {
		private boolean isRunning = true;

		public void setRunning(boolean isRunning) {
			this.isRunning = isRunning;
		}

		@Override
		public void run() {
			while (isRunning) {
				if (!model.isGameOver()) {
					update();
					view.render();
				}
				try {
					Thread.sleep(updateSpeed());
				} catch (InterruptedException e) {
					Log.d( TAG, "GameThread (SnakeChallenge) sleep interrupted!" );
				}
			}
		}
	}

	/**
	 * Call if the user gets game over without fulfilling victory conditions.
	 */
	private void resetGame() {
		this.model = new SnakeModel(SnakeConstants.getGameSize(),
				Direction.NORTH, this.rng);
		this.view.setModel(this.model);
	}
}