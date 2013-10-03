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

package se.chalmers.dat255.sleepfighter.utils.motion;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

import se.chalmers.dat255.sleepfighter.utils.debug.Debug;
import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.Display;
import android.view.WindowManager;

/**
 * A class for handling motion events that can be used all across the app. Some
 * code copied from http://stackoverflow.com/a/12098469
 */
public class MotionControl implements SensorEventListener {

	private SensorManager mSensorManager;
	private Sensor mSensor;
	private int defaultType = Sensor.TYPE_ACCELEROMETER;
	private PropertyChangeSupport pcs;

	private double angle = 0d;

	// Create a constant to convert nanoseconds to seconds.
	private static final float NS2S = 1.0f / 1000000000.0f;
	private float timestamp;

	/**
	 * Sets mSensor to {@value defaultType} by default, possible to set to other
	 * Sensors with setSensor.
	 * 
	 * @param activity
	 */
	public MotionControl(Activity activity) {
		// Get an instance of the SensorManager
		mSensorManager = (SensorManager) activity
				.getSystemService(Activity.SENSOR_SERVICE);
		setSensor(defaultType);
		mSensorManager.registerListener(this, mSensor,
				SensorManager.SENSOR_DELAY_FASTEST);

		pcs = new PropertyChangeSupport(this);
	}

	public boolean setSensor(int type) {
		Sensor temp = mSensorManager.getDefaultSensor(type);

		// Check if Sensor of designated type exists on device
		if (temp != null) {
			mSensor = temp;
			return true;
		} else {
			Debug.d("Sensor of the request type does not exist");
			return false;
		}
	}

	public double getAngle() {
		return angle;
	}

	public void addListener(PropertyChangeListener pcl) {
		pcs.addPropertyChangeListener(pcl);
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

	/**
	 * Partially copied from http://stackoverflow.com/a/12098469
	 */
	@Override
	public void onSensorChanged(SensorEvent event) {
		if (event.sensor != mSensor) {
			return;
		}

		calcAzimuth(event);
	}

	/**
	 * Calculates rotation around the Z axis (azimuth) of the device. Partially
	 * copied from http://stackoverflow.com/a/12098469
	 */
	private void calcAzimuth(SensorEvent event) {
		float aX = event.values[0];
		float aY = event.values[1];

		// Calculate rotation around Z axis.
		angle = Math.atan2(aX, aY);

		pcs.firePropertyChange(null, 0, 1);
	}
}