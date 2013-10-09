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

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;
import se.chalmers.dat255.sleepfighter.R;
import se.chalmers.dat255.sleepfighter.activity.ChallengeActivity;
import se.chalmers.dat255.sleepfighter.model.challenge.ChallengeConfigSet;

/**
 * A challenge where the user have to shake the device to complete it.
 */
public class ShakeChallenge implements Challenge {

	private static final float GOAL = 5000;

	private ChallengeActivity activity;
	private ProgressBar progressBar;
	private TextView progressText;

	private SensorManager sensorManager;
	private Sensor accelerometer;

	private float accumulator = 0;
	private Vector3D lastVector;

	@Override
	public void start(ChallengeActivity activity, ChallengeResolvedParams params) {
		start(activity, params, null);
	}
	
	@Override
	public void start(ChallengeActivity activity,
			ChallengeResolvedParams params, Bundle state) {
		this.activity = activity;
		this.activity.setContentView(R.layout.challenge_shake);

		// Lock to portrait orientation for now
		// TODO remove if progress can be stored
		this.activity.setRequestedOrientation(
				ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		// Get view references
		this.progressBar = (ProgressBar) this.activity
				.findViewById(R.id.progressBar);
		this.progressText = (TextView) this.activity
				.findViewById(R.id.progressText);

		// Register to get acceleration events
		this.sensorManager = (SensorManager) activity
				.getSystemService(Context.SENSOR_SERVICE);
		this.accelerometer = sensorManager
				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		this.sensorManager.registerListener(this.sensorEventListener,
				accelerometer, SensorManager.SENSOR_DELAY_GAME);
	}
	
	private SensorEventListener sensorEventListener = new SensorEventListener() {

		@Override
		public void onSensorChanged(SensorEvent event) {
			float x = event.values[0];
			float y = event.values[1];
			float z = event.values[2];
			Vector3D vector = new Vector3D(x, y, z);
			onAccelerationChange(vector);
		}

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {}
	};

	/**
	 * Handle an acceleration change event.
	 * 
	 * @param vector
	 *            the current acceleration vector
	 */
	private void onAccelerationChange(Vector3D vector) {
		// Only take vector difference into account, need a last vector
		if (lastVector != null) {
			// The difference of the vectors
			Vector3D diff = vector.subtract(lastVector);

			// The one dimensional "size" of the difference, there are probably
			// better things to measure
			double work = diff.getNorm();

			// Increase the accumulator
			this.accumulator += work;

			updateProgress();

			checkIfCompleted();
		}
		this.lastVector = vector;
	}

	private void updateProgress() {
		// Change the progress bar and text to show how near completion
		int percentage = Math.min(100, Math.round(100 * (accumulator / GOAL)));
		this.progressBar.setProgress(percentage);
		this.progressText.setText(percentage + "%");
	}

	private void checkIfCompleted() {
		if (accumulator >= GOAL) {
			// Unregister when done
			this.sensorManager.unregisterListener(sensorEventListener);
			this.activity.complete();
		}
	}

	@Override
	public Bundle savedState() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onPause() {
		this.sensorManager.unregisterListener(sensorEventListener);
	}

	@Override
	public void onResume() {
	}

	@Override
	public void onDestroy() {
	}
}
