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

import se.chalmers.dat255.sleepfighter.activity.ChallengeActivity;
import se.chalmers.dat255.sleepfighter.challenge.Challenge;
import se.chalmers.dat255.sleepfighter.utils.debug.Debug;
import se.chalmers.dat255.sleepfighter.utils.geometry.Direction;
import se.chalmers.dat255.sleepfighter.utils.motion.MotionControl;
import se.chalmers.dat255.sleepfighter.utils.motion.MotionControlException;
import android.content.pm.ActivityInfo;
import android.widget.TextView;

/**
 * A challenge that requires the player to move and rotate the device.
 */
public class MotionSnakeChallenge implements Challenge, PropertyChangeListener {

	private MotionControl motionControl;
	private double angle, margin = 0.2;
	private ChallengeActivity activity;
	private SnakeController snakeController;
	private TextView tv;
	private MotionControlException exception = null;

	@Override
	public void start(ChallengeActivity activity) {
		Debug.d("In MotionSnakeActivity");
		this.activity = activity;
		this.activity
				.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

		try {
			this.motionControl = new MotionControl(activity);
		} catch (MotionControlException e) {
			// If the Challenge for some reason is selected, despite the device
			// not having the required Sensor, complete the Challenge so as to
			// not trap the user.
			this.activity.complete();
			this.exception = e;
		}

		// If there was no MotionControlException, keep running.
		if (this.exception != null) {
			this.motionControl.addListener(this);

			this.snakeController = new SnakeController(this.activity.getBaseContext());
			this.activity.setContentView(snakeController.getView());
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		Object source = event.getSource();
		if (source.equals(this.motionControl)) {
			this.handleRotation();
		} else if (source.equals(snakeController)) {
			tv.setText(event.getPropertyName());
			this.complete();
		}

	}

	private void handleRotation() {
		this.angle = this.motionControl.getAzimuth();

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

	public boolean withinMargin(Direction dir) {
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

	public void complete() {
		this.motionControl.unregisterSensorListener();
		this.activity.complete();
	}
}
