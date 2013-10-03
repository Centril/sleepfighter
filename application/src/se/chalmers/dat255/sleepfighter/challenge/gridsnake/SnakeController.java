package se.chalmers.dat255.sleepfighter.challenge.gridsnake;

import java.beans.PropertyChangeSupport;
import java.util.Random;

import android.graphics.Canvas;

import se.chalmers.dat255.sleepfighter.challenge.gridsnake.geometry.Direction;
import se.chalmers.dat255.sleepfighter.utils.debug.Debug;

/**
 * Controller class for Snake. Original author Mazdak, modified by Laszlo for
 * SleepFighter.
 */
public class SnakeController {
	/** Random Number Generator (RNG) */
	private final Random rng;

	/** Model of Snake game to be run. */
	private SnakeModel model;

	/** View for the game of Snake */
	private SnakeView view;

	private PropertyChangeSupport pcs;

	private SnakeThread thread;

	/**
	 * Constructs the controller.
	 */
	public SnakeController() {
		this.init();

		this.rng = new Random();

		this.view = new SnakeView();

		this.thread = new SnakeThread();
	}

	public SnakeController(MotionChallenge challenge) {
		this();

		this.pcs.addPropertyChangeListener(challenge);
	}

	public long updateSpeed() {
		return SnakeConstants.getUpdateSpeed();
	}

	protected void init() {
		this.model = new SnakeModel(SnakeConstants.getGameSize(),
				Direction.getRandom(this.rng), this.rng);
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
			pcs.firePropertyChange(null, null, null);
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

				try {
					Thread.sleep(updateSpeed());
				} catch (InterruptedException ex) {
					Debug.d("GameThread (SnakeChallenge) sleep interrupted!");
				}
			}
		}
	}
}