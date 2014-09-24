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

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import se.toxbee.sleepfighter.R;
import se.toxbee.sleepfighter.android.motion.NoSensorException;
import se.toxbee.sleepfighter.android.motion.RotationControl;
import se.toxbee.sleepfighter.challenge.BaseChallenge;
import se.toxbee.sleepfighter.challenge.ChallengePrototypeDefinition;
import se.toxbee.sleepfighter.challenge.ChallengeResolvedParams;
import se.toxbee.sleepfighter.model.challenge.ChallengeType;
import se.toxbee.commons.geom.Direction;

/**
 * A challenge that requires the player to rotate the device to control the
 * "snake".
 */
public class RotoSnakeChallenge extends BaseChallenge implements PropertyChangeListener {
	/**
	 * PrototypeDefinition for RotoSnakeChallenge.
	 * 
	 * @version 1.0
	 * @since Oct 8, 2013
	 */
	public static class PrototypeDefinition extends ChallengePrototypeDefinition {{
		setType(ChallengeType.ROTO_SNAKE);
	}}

	private RotationControl motionControl;
	private double angle, margin = 0.2;
	private SnakeController snakeController;

	@Override
	public void start( Activity activity, ChallengeResolvedParams params) {
		super.start( activity, params );

		this.activity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		this.activity().setTitle(R.string.rotoSnakeTitle);

		try {
			this.motionControl = new RotationControl(this.activity());
		} catch (NoSensorException e) {
			// If the Challenge for some reason is selected, despite the device
			// not having the required Sensor, complete the Challenge so as to
			// not trap the user.
			this.complete();
			return;
		}

		this.motionControl.addListener(this);

		this.snakeController = new SnakeController(this, this.activity().getBaseContext());
		this.activity().setContentView(snakeController.getView());
	}

	@Override
	public void start( Activity activity, ChallengeResolvedParams params, Bundle state) {
		this.start(activity, params);
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		Object source = event.getSource();
		if (source.equals(this.motionControl)) {
			this.handleRotation();
		} else if (source.equals(this.snakeController)) {
			this.complete();
		}
	}

	private void handleRotation() {
		// User controls for the Challenge
		if (withinMargin(Direction.WEST)) {
			this.snakeController.update(Direction.WEST);
		} else if (withinMargin(Direction.NORTH)) {
			this.snakeController.update(Direction.NORTH);
		} else if (withinMargin(Direction.EAST)) {
			this.snakeController.update(Direction.EAST);
		} else if (withinMargin(Direction.SOUTH)) {
			this.snakeController.update(Direction.SOUTH);
		}
	}

	private boolean withinMargin(Direction dir) {
		this.angle = this.motionControl.getAzimuth();

		switch (dir) {
		case WEST:
			return Math.abs(Math.abs(this.angle) - this.margin) <= this.margin;
		case NORTH:
			return this.angle >= 0
					&& Math.abs(this.angle - (Math.PI / 2)) <= this.margin;
		case EAST:
			return Math.abs(Math.abs(this.angle) - Math.PI) <= this.margin;
		case SOUTH:
			return this.angle <= 0
					&& Math.abs(this.angle + (Math.PI / 2)) <= this.margin;
		default:
			return false;
		}
	}

	protected void completeChallenge() {
		// Run on UI thread since things can get messy if trying to execute them
		// on other threads
		activity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				pause();
				complete();
			}
		});
	}

	/**
	 * Unregisters the RotationControl instance of the Sensors it is listening
	 * to. Must be called when the Activity pauses, or else the RotationControl
	 * will continue listening to the Sensors, which is very expensive.
	 */
	public void pause() {
		this.snakeController.pause();
		this.motionControl.unregisterSensorListener();
	}

	@Override
	public Bundle savedState() {
		return null;
	}

	@Override
	public void onResume() {
		this.motionControl.resetListener();
		this.snakeController.resume();
	}

	@Override
	public void onPause() {
		pause();
	}
}
