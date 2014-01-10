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

package se.toxbee.sleepfighter.challenge.rotosnake;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import se.toxbee.sleepfighter.R;
import se.toxbee.sleepfighter.android.motion.NoSensorException;
import se.toxbee.sleepfighter.android.motion.RotationControl;
import se.toxbee.sleepfighter.challenge.BaseChallenge;
import se.toxbee.sleepfighter.challenge.ChallengePrototypeDefinition;
import se.toxbee.sleepfighter.challenge.ChallengeResolvedParams;
import se.toxbee.sleepfighter.model.challenge.ChallengeType;
import se.toxbee.sleepfighter.utils.geom.Direction;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

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