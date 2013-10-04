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

import se.chalmers.dat255.sleepfighter.R;
import se.chalmers.dat255.sleepfighter.activity.ChallengeActivity;
import se.chalmers.dat255.sleepfighter.challenge.Challenge;
import se.chalmers.dat255.sleepfighter.utils.debug.Debug;
import se.chalmers.dat255.sleepfighter.utils.geometry.Direction;
import se.chalmers.dat255.sleepfighter.utils.motion.MotionControl;
import android.content.pm.ActivityInfo;
import android.widget.TextView;

/**
 * A challenge that requires the player to move and rotate the device.
 */
public class MotionSnakeChallenge implements Challenge, PropertyChangeListener {

	private MotionControl motionControl;
	private TextView textView;
	private double angle, margin = 0.2;
	private ChallengeActivity activity;
	private SnakeController snakeController;

	@Override
	public void start(ChallengeActivity activity) {
		this.activity = activity;
		this.activity
				.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		this.activity.setContentView(R.layout.alarm_challenge_motion);

		this.motionControl = new MotionControl(activity);
		this.motionControl.addListener(this);
		this.textView = (TextView) activity.findViewById(R.id.motionText);

		// this.snakeController = new SnakeController();

	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		Object source = event.getSource();
		if (source.equals(this.motionControl)) {
			this.handleRotation();
		} else if (source.equals(snakeController)) {
			this.complete();
		}

	}

	private void handleRotation() {
		this.angle = this.motionControl.getAngle();

		// User controls for the Challenge
		if (withinMargin(Direction.WEST)) {
			this.textView.setText(Direction.WEST.toString()
					+ Double.toString(this.angle));
			// this.snakeController.update(Direction.WEST);
		} else if (withinMargin(Direction.NORTH)) {
			this.textView.setText(Direction.NORTH.toString()
					+ Double.toString(this.angle));
			// this.snakeController.update(Direction.NORTH);
		} else if (withinMargin(Direction.EAST)) {
			this.textView.setText(Direction.EAST.toString()
					+ Double.toString(this.angle));
			// this.snakeController.update(Direction.EAST);
		} else if (withinMargin(Direction.SOUTH)) {
			this.textView.setText(Direction.SOUTH.toString()
					+ Double.toString(this.angle));
			// this.snakeController.update(Direction.SOUTH);
		}
	}

	public boolean withinMargin(Direction dir) {
		boolean within = false;
		Debug.d("withinMargin" + dir.toString());
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
			within = this.angle <= -Math.PI / 2 + this.margin && this.angle >= -Math.PI / 2
					|| this.angle >= -Math.PI / 2 - this.margin && this.angle <= -Math.PI / 2;
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
