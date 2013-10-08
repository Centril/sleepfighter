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

package se.chalmers.dat255.sleepfighter.challenge.motionsnake;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import se.chalmers.dat255.sleepfighter.activity.ChallengeActivity;
import se.chalmers.dat255.sleepfighter.challenge.Challenge;
import se.chalmers.dat255.sleepfighter.challenge.ChallengePrototypeDefinition;
import se.chalmers.dat255.sleepfighter.model.challenge.ChallengeConfigSet;
import se.chalmers.dat255.sleepfighter.model.challenge.ChallengeType;
import se.chalmers.dat255.sleepfighter.utils.geometry.Direction;
import se.chalmers.dat255.sleepfighter.utils.motion.MotionControl;
import se.chalmers.dat255.sleepfighter.utils.motion.NoSensorException;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.Toast;

/**
 * A challenge that requires the player to move and rotate the device.
 */
public class MotionSnakeChallenge implements Challenge, PropertyChangeListener {

	public static class PrototypeDefinition extends
			ChallengePrototypeDefinition {
		{
			setType(ChallengeType.MOTION_SNAKE);
		}
	}

	private MotionControl motionControl;
	private double angle, margin = 0.2;
	private ChallengeActivity activity;
	private SnakeController snakeController;
	private NoSensorException exception = null;

	private ChallengeConfigSet config;

	@Override
	public void start(ChallengeActivity activity, ChallengeConfigSet config) {
		this.activity = activity;
		this.activity
				.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		try {
			this.motionControl = new MotionControl(this.activity);
		} catch (NoSensorException e) {
			// If the Challenge for some reason is selected, despite the device
			// not having the required Sensor, complete the Challenge so as to
			// not trap the user.
			this.activity.complete();
			this.exception = e;
		}

		// If there was no NoSensorException, keep running.
		if (this.exception == null) {
			this.motionControl.addListener(this);

			this.snakeController = new SnakeController(this,
					this.activity.getBaseContext());
			this.activity.setContentView(snakeController.getView());
		}
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
		// Debug.d("Azimuth: " + motionControl.getAzimuth());
		// Debug.d("Pitch: " + motionControl.getPitch());
		// Debug.d("Roll: " + motionControl.getRoll());

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

		boolean within = false;

		switch (dir) {
		case WEST:
			within = this.angle <= this.margin && this.angle >= 0
					|| this.angle >= -this.margin && this.angle <= 0;
			break;
		case NORTH:
			within = this.angle <= Math.PI / 2 + this.margin && this.angle >= 0
					|| this.angle >= Math.PI / 2 - this.margin
					&& this.angle <= Math.PI / 2;
			break;
		case EAST:
			within = this.angle >= Math.PI - this.margin
					|| this.angle <= -Math.PI + this.margin
					&& this.angle >= -Math.PI / 2 + this.margin;
			break;
		case SOUTH:
			within = this.angle <= -Math.PI / 2 + this.margin
					&& this.angle >= -Math.PI / 2
					|| this.angle >= -Math.PI / 2 - this.margin
					&& this.angle <= -Math.PI / 2;
			break;
		default:
			break;
		}
		return within;
	}

	private void complete() {
		// Run on UI thread since things can get messy if trying to execute them
		// on other threads
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				motionControl.unregisterSensorListener();
				activity.complete();
			}
		});
	}

	/**
	 * Unregisters the MotionControl instance of the Sensors it is listening to.
	 * Must be called when the Activity pauses, or else the MotionControl will
	 * continue listening to the Sensors, which is very expensive.
	 */
	public void pause() {
		motionControl.unregisterSensorListener();
	}

	@Override
	public void start(ChallengeActivity activity, Bundle state) {
		Toast.makeText(activity, "TODO: IMPLEMENT START FROM SAVED STATE",
				Toast.LENGTH_LONG).show();
		this.start(activity, config);
	}

	@Override
	public Bundle savedState() {
		// To be implemented
		return null;
	}

	@Override
	public void onResume() {
		this.motionControl.resetListener();
	}

	@Override
	public void onPause() {
		pause();
	}

	@Override
	public void onDestroy() {
	}
}
