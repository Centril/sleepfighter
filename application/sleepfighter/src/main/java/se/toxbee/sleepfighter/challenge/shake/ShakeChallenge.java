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

package se.toxbee.sleepfighter.challenge.shake;

import android.app.Activity;
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

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import se.toxbee.sleepfighter.R;
import se.toxbee.sleepfighter.challenge.BaseChallenge;
import se.toxbee.sleepfighter.challenge.ChallengePrototypeDefinition;
import se.toxbee.sleepfighter.challenge.ChallengeResolvedParams;
import se.toxbee.sleepfighter.model.challenge.ChallengeType;

/**
 * A challenge where the user have to shake the device to complete it.
 */
public class ShakeChallenge extends BaseChallenge {

	private static final String TAG = "ShakeChallenge";

	public static class PrototypeDefinition extends ChallengePrototypeDefinition {{
		setType(ChallengeType.SHAKE);
	}}

	private static final float GOAL = 5000;
	private static final double MIN_WORK = 100;

	private static final String KEY_PROGRESS_FLOAT = "progress";
	private ProgressBar progressBar;
	private TextView progressText;

	private SensorManager sensorManager;
	private Sensor accelerometer;

	private float progress = 0;
	private Vector3D lastVector;
	private long lastTime;

	@Override
	public void start( Activity activity, ChallengeResolvedParams params) {
		start(activity, params, null);
	}
	
	@Override
	public void start( Activity activity, ChallengeResolvedParams params, Bundle state) {
		super.start( activity, params );

		this.activity().setContentView(R.layout.challenge_shake);

		// Get view references
		this.progressBar = (ProgressBar) this.activity()
				.findViewById(R.id.progressBar);
		this.progressText = (TextView) this.activity()
				.findViewById(R.id.progressText);

		// Check if required sensor is available
		boolean hasAccelerometer = activity.getPackageManager()
				.hasSystemFeature(PackageManager.FEATURE_SENSOR_ACCELEROMETER);
		if(!hasAccelerometer) {
			// Complete right away, for now. Checking if device has required
			// hardware could perhaps be done before the challenge is started.
			Log.e(TAG, "Device lacks required sensor for ShakeChallenge");
			this.complete();
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
			this.complete();
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
}
