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
import java.util.List;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * A class for handling motion events that can be used all across the app. Some
 * code copied from http://stackoverflow.com/a/12098469
 */
public class MotionControl implements SensorEventListener {

	private SensorManager mSensorManager;
	private Sensor mSensor;
	private int defaultSensorType = Sensor.TYPE_ACCELEROMETER;
	private PropertyChangeSupport pcs;
	private double azimuthRotation = 0d;
	private float[] orientation = new float[3];

	/**
	 * Sets mSensor to {@value defaultType} by default, possible to set to other
	 * Sensors with setSensor.
	 * 
	 * @param activity
	 * @throws MotionControlException
	 *             If the default Sensor type is unavailable on the device.
	 */
	public MotionControl(Activity activity) throws MotionControlException {
		// Get an instance of the SensorManager
		this.mSensorManager = (SensorManager) activity
				.getSystemService(Activity.SENSOR_SERVICE);

		setSensor(defaultSensorType);

		this.mSensorManager.registerListener(this, mSensor,
				SensorManager.SENSOR_DELAY_FASTEST);

		this.pcs = new PropertyChangeSupport(this);
	}

	/**
	 * Sets the mSensors variable to the default Sensor of the specified type if
	 * it exists, but also checks for the version of the Sensor recommended on
	 * the Android Developer website. If it does not exist, a
	 * MotionControlException is thrown.
	 * 
	 * @param type
	 *            Requested Sensor type.
	 * @throws MotionControlException
	 *             Thrown if Sensor type unavailable on the device.
	 */
	public void setSensor(int type) throws MotionControlException {
		Sensor temp = this.mSensorManager.getDefaultSensor(type);

		if (temp != null) {
			// If it exists, use the software-based sensor provided by Android
			// Open Source Project, recommended by
			// https://developer.android.com/guide/topics/sensors/sensors_motion.html
			List<Sensor> sensors = mSensorManager.getSensorList(type);
			for (int i = 0; i < sensors.size(); i++) {
				if ((sensors.get(i).getVendor().contains("Google Inc."))
						&& (sensors.get(i).getVersion() == 3)) {
					// Use the version 3 sensor.
					temp = sensors.get(i);
				}
			}
			this.mSensor = temp;
		} else {
			throw new MotionControlException();
		}
	}

	public void addListener(PropertyChangeListener pcl) {
		this.pcs.addPropertyChangeListener(pcl);
	}
	
	/**
	 * Partially copied from http://stackoverflow.com/a/12098469
	 */
	@Override
	public void onSensorChanged(SensorEvent event) {
		if (event.sensor != this.mSensor) {
			return;
		}

		// Movement along X-axis
		this.orientation[0] = event.values[0];
		// Movement along Y-axis
		this.orientation[1] = event.values[1];
		// Movement along Z-axis
		this.orientation[2] = event.values[2];

		this.pcs.firePropertyChange(null, 0, 1);
	}

	/**
	 * Calculates rotation around the Z axis (azimuth) of the device.
	 */
	public double getAzimuthRotation() {
		return this.azimuthRotation = Math.atan2(this.orientation[0],
				this.orientation[1]);
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// Not used
	}

	public void unregisterSensorListener() {
		this.mSensorManager.unregisterListener(this);
	}
}