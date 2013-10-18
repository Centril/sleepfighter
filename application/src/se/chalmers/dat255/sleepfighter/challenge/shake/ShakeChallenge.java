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
package se.chalmers.dat255.sleepfighter.challenge.shake;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import se.chalmers.dat255.sleepfighter.R;
import se.chalmers.dat255.sleepfighter.activity.ChallengeActivity;
import se.chalmers.dat255.sleepfighter.challenge.Challenge;
import se.chalmers.dat255.sleepfighter.challenge.ChallengePrototypeDefinition;
import se.chalmers.dat255.sleepfighter.challenge.ChallengeResolvedParams;
import se.chalmers.dat255.sleepfighter.model.challenge.ChallengeType;

/**
 * A challenge where the user have to shake the device to complete it.
 */
public class ShakeChallenge implements Challenge {

	private static final String TAG = "ShakeChallenge";

	public static class PrototypeDefinition extends ChallengePrototypeDefinition {{
		setType(ChallengeType.SHAKE);
	}}

	private static final float GOAL = 5000;
	private static final double MIN_WORK = 100;

	private static final String KEY_PROGRESS_FLOAT = "progress";

	private ChallengeActivity activity;
	private ProgressBar progressBar;
	private TextView progressText;

	private SensorManager sensorManager;
	private Sensor accelerometer;

	private float progress = 0;
	private Vector3D lastVector;
	private long lastTime;

	@Override
	public void start(ChallengeActivity activity, ChallengeResolvedParams params) {
		start(activity, params, null);
	}
	
	@Override
	public void start(ChallengeActivity activity,
			ChallengeResolvedParams params, Bundle state) {
		this.activity = activity;
		this.activity.setContentView(R.layout.challenge_shake);

		// Get view references
		this.progressBar = (ProgressBar) this.activity
				.findViewById(R.id.progressBar);
		this.progressText = (TextView) this.activity
				.findViewById(R.id.progressText);

		// Check if required sensor is available
		boolean hasAccelerometer = activity.getPackageManager()
				.hasSystemFeature(PackageManager.FEATURE_SENSOR_ACCELEROMETER);
		if(!hasAccelerometer) {
			// Complete right away, for now. Checking if device has required
			// hardware could perhaps be done before the challenge is started.
			Log.e(TAG, "Device lacks required sensor for ShakeChallenge");
			activity.complete();
			return;
		}

		// Register to get acceleration events
		this.sensorManager = (SensorManager) activity
				.getSystemService(Context.SENSOR_SERVICE);
		this.accelerometer = sensorManager
				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		
		// Get last progress from bundle, if it exists
		if(state != null) {
			this.progress = state.getFloat(KEY_PROGRESS_FLOAT);
		}
		updateProgress();
	}
	
	private SensorEventListener sensorEventListener = new SensorEventListener() {

		@Override
		public void onSensorChanged(SensorEvent event) {
			long time = System.nanoTime();
			float x = event.values[0];
			float y = event.values[1];
			float z = event.values[2];
			Vector3D vector = new Vector3D(x, y, z);
			onAccelerationChange(vector, time);
		}

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {}
	};

	/**
	 * Handle an acceleration change event.
	 * 
	 * @param vector
	 *            the current acceleration vector
	 * @param time
	 *            the time the acceleration vector was acquired, in nono
	 *            seconds, since some time in the past defined by the system
	 */
	private void onAccelerationChange(Vector3D vector, long time) {
		// Only take vector difference into account, need a last vector
		if (lastVector != null) {
			// The difference of the vectors
			Vector3D diff = vector.subtract(lastVector);

			// Time difference in seconds
			double timeDiff = (double)(time - lastTime) / 1000000000;
						
			// The one dimensional "size" of the difference, there are probably
			// better things to measure
			double work = diff.getNorm();

			double workPerTime = work / timeDiff;

			// Only take effort above a set limit into account
			// Simple way to sort out small movements
			if (workPerTime > MIN_WORK) {
				// Increase the accumulator
				this.progress += work;

				updateProgress();

				checkIfCompleted();
			}
		}
		this.lastVector = vector;
		this.lastTime = time;
	}

	/**
	 * Update the progress shown in the UI.
	 */
	private void updateProgress() {
		int percentage = Math.min(100, Math.round(100 * (progress / GOAL)));
		this.progressBar.setProgress(percentage);
		this.progressText.setText(percentage + "%");
	}

	private void checkIfCompleted() {
		if (progress >= GOAL) {
			// Unregister when done
			this.sensorManager.unregisterListener(sensorEventListener);
			this.activity.complete();
		}
	}

	@Override
	public Bundle savedState() {
		Bundle state = new Bundle();
		state.putFloat(KEY_PROGRESS_FLOAT, this.progress);
		return state;
	}

	@Override
	public void onPause() {
		this.sensorManager.unregisterListener(sensorEventListener);
	}

	@Override
	public void onResume() {
		this.sensorManager.registerListener(this.sensorEventListener,
				accelerometer, SensorManager.SENSOR_DELAY_GAME);
	}

	@Override
	public void onDestroy() {
	}
}
