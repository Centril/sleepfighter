package se.chalmers.dat255.sleepfighter.challenge;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import se.chalmers.dat255.sleepfighter.R;
import se.chalmers.dat255.sleepfighter.activity.ChallengeActivity;

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
	public void start(ChallengeActivity activity) {
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
			// Unregister when done, should also be done on something like
			// onPause
			this.sensorManager.unregisterListener(sensorEventListener);
			this.activity.complete();
		}
	}
}
