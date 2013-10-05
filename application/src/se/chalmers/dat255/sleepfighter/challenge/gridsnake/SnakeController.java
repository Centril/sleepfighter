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

package se.chalmers.dat255.sleepfighter.challenge.gridsnake;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Random;

import se.chalmers.dat255.sleepfighter.utils.debug.Debug;
import se.chalmers.dat255.sleepfighter.utils.geometry.Direction;
import android.content.Context;

/**
 * Controller class for Snake. Original author Mazdak, modified by Laszlo for
 * SleepFighter.
 */
public class SnakeController implements PropertyChangeListener {
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

	public SnakeController(MotionSnakeChallenge challenge, Context context) {
		this(context);

		this.pcs.addPropertyChangeListener(challenge);
	}

	public long updateSpeed() {
		return SnakeConstants.getUpdateSpeed();
	}

	protected void init() {
		this.model = new SnakeModel(SnakeConstants.getGameSize(),
				Direction.getRandom(this.rng), this.rng);

		this.view = new SnakeView(this.context, this.model);
		
		thread.start();
	}

	public void update() {
		try {
			SnakeModel model = (SnakeModel) this.model;
			model.tickUpdate();
		} catch (GameOverException e) {
			this.stopGame(e.getScore());
			this.thread.setRunning(false);
		}
	}

	private void stopGame(int score) {
		if (score >= SnakeConstants.getVictoryCondition()) {
			pcs.firePropertyChange(Integer.toString(score), null, null);
		}
	}

	public void update(Direction dir) {
		if (dir != Direction.NONE) {
			((SnakeModel) this.model).updateDirection(dir);
		}
	}

	public SnakeView getView() {
		return view;
	}

	private class SnakeThread extends Thread {
		private boolean isRunning = true;

		public void setRunning(boolean isRunning) {
			this.isRunning = isRunning;
		}

		public boolean isRunning() {
			return isRunning;
		}

		@Override
		public void run() {
			while (isRunning) {
				update();
				view.render();
				try {
					Thread.sleep(updateSpeed());
				} catch (InterruptedException e) {
					Debug.d("GameThread (SnakeChallenge) sleep interrupted!");
				}
			}
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {

	}
}