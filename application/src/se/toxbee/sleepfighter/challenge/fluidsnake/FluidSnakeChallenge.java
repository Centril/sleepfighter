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

package se.toxbee.sleepfighter.challenge.fluidsnake;

import se.toxbee.sleepfighter.R;
import se.toxbee.sleepfighter.challenge.BaseChallenge;
import se.toxbee.sleepfighter.challenge.ChallengePrototypeDefinition;
import se.toxbee.sleepfighter.challenge.ChallengeResolvedParams;
import se.toxbee.sleepfighter.model.challenge.ChallengeType;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Toast;

/**
 * A fluid snake challenge.
 * 
 * Touch on the screen to choose the direction to where the snake is moving. Eat
 * all red fruits to open an exit which you will need to pass through in order
 * to win. If you hit a wall or one of the snake segments you will lose and the
 * game will restart.
 * 
 * @author Hassel
 * 
 */
public class FluidSnakeChallenge extends BaseChallenge implements OnTouchListener {
	/**
	 * PrototypeDefinition for FluidSnakeChallenge.
	 * 
	 * @version 1.0
	 * @since Oct 5, 2013
	 */
	public static class PrototypeDefinition extends ChallengePrototypeDefinition {{
		setType(ChallengeType.FLUID_SNAKE);
	}}

	private Model model;
	private Thread thread;
	private GameView view;

	// where the user touched on the screen
	private float touchX;
	private float touchY;

	// true if we should call the updateDirection method
	private boolean updateDir;

	@Override
	public void start( Activity activity, ChallengeResolvedParams params) {
		super.start( activity, params );

		activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		activity.setTitle(R.string.fluidSnakeTitle);

		model = new Model();
		view = new GameView(activity, model);

		view.setOnTouchListener(this);
		activity.setContentView(view);

		thread = new Thread() {
			@Override
			public void run() {
				long lastTime = 0;

				// run as long as you havent won
				while (!model.won()) {

					// if the player lost, restart the game
					if (model.lost()) {
						restart();
					}

					// calculate delta in order to make the game run smoothly
					// even if
					// the target FPS isn't reached (or if it is actually higher
					// than supposed)
					long now = System.currentTimeMillis();
					float delta = (lastTime > 0 ? now - lastTime
							: 1000 / SnakeConstants.TARGET_FPS)
							/ (1000f / SnakeConstants.TARGET_FPS);
					lastTime = now;

					// update the direction of the snake only when needed
					if (updateDir) {
						model.updateDirection(touchX / view.getWidth(), touchY
								/ view.getHeight());
						updateDir = false;
					}

					// update the snake model, with the provided delta (actually
					// a multiplier and not really a delta)
					model.update(delta);

					// render the view
					view.render();

					try {
						Thread.sleep(1000 / SnakeConstants.TARGET_FPS);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

				// if we got out of the loop it means the player won, complete
				// the challenge
				exitChallenge();
			}
		};
		thread.start();
	}

	private void exitChallenge() {
		// Run on UI thread since things can get messy if trying to execute them
		// on other threads
		activity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				complete();
			}
		});
	}

	/**
	 * Provides the view with a new model
	 */
	public void restart() {
		model = new Model();
		view.setModel(model);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		touchX = event.getX();
		touchY = event.getY();

		// update the direction in the next game loop
		updateDir = true;

		return true;
	}

	@Override
	public void start( Activity activity, ChallengeResolvedParams params, Bundle state) {
		Toast.makeText(activity, "TODO: IMPLEMENT START FROM SAVED STATE",
				Toast.LENGTH_LONG).show();
		this.start(activity, params);
	}

	@Override
	public Bundle savedState() {
		return null;
	}
}