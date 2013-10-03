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

package se.chalmers.dat255.sleepfighter.challenge;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import se.chalmers.dat255.sleepfighter.R;
import se.chalmers.dat255.sleepfighter.activity.ChallengeActivity;
import se.chalmers.dat255.sleepfighter.utils.motion.MotionControl;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.widget.TextView;

/**
 * A challenge that requires the player to move and rotate the device.
 */
public class MotionChallenge implements Challenge, PropertyChangeListener {

	private MotionControl mc;
	private Activity activity;
	private TextView tv;
	double[] conditions = new double[2];

	@Override
	public void start(ChallengeActivity activity) {
		activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		this.mc = new MotionControl(activity);
		this.mc.addListener(this);
		this.activity = activity;
		activity.setContentView(R.layout.alarm_challenge_motion);
		tv = (TextView) activity.findViewById(R.id.motionText);
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		// If rotation between 0.5 and Math.PI - 0.5, update listeners
		double angle = mc.getAngle();
		// if (angle % (Math.PI / 2) >= 0.5
		// && angle % (Math.PI / 2) <= (Math.PI / 2) - 0.5) {
		tv.setText(Double.toString(angle));
		// }
	}
}
