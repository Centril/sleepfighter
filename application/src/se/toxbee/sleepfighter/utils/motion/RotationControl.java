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

package se.toxbee.sleepfighter.utils.motion;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * A class for handling rotation events that can be used all across the app. Some
 * code inspired by http://stackoverflow.com/a/12098469
 */
public class RotationControl implements SensorEventListener {

	private SensorManager mSensorManager;
	private Sensor mSensor;
	private static int sensorType = Sensor.TYPE_ACCELEROMETER;
	private PropertyChangeSupport pcs;
	private float[] orientation = new float[3];

	// Sensor vendor recommended by
	// https://developer.android.com/guide/topics/sensors/sensors_motion.html
	private String preferredVendor = "Google Inc.";

	// Sensor version recommended by
	// https://developer.android.com/guide/topics/sensors/sensors_motion.html
	private int preferredVersion = 3;

	/**
	 * Sets mSensor to {@value defaultType} by default, possible to set to other
	 * Sensors with setSensor.
	 * 
	 * @param activity
	 * @throws NoSensorException
	 *             If the default Sensor type is unavailable on the device.
	 */
	public RotationControl(Activity activity) throws NoSensorException {
		// Get an instance of the SensorManager
		this.mSensorManager = (SensorManager) activity
				.getSystemService(Activity.SENSOR_SERVICE);

		this.setSensor();

		this.mSensorManager.registerListener(this, this.mSensor,
				SensorManager.SENSOR_DELAY_NORMAL);

		this.pcs = new PropertyChangeSupport(this);
	}

	/**
	 * Sets the mSensor variable to the default Sensor of the specified type if
	 * it exists, but also checks for the vendor and version specified by the
	 * variables. If it does not exist, a NoSensorException is thrown.
	 * 
	 * @param type
	 *            Requested Sensor type.
	 * @throws NoSensorException
	 *             Thrown if Sensor type unavailable on the device.
	 */
	private void setSensor() throws NoSensorException {
		Sensor temp = this.mSensorManager.getDefaultSensor(sensorType);

		if (temp != null) {

			// If it exists, use the preferred Sensor
			List<Sensor> sensors = mSensorManager
					.getSensorList(sensorType);

			for (int i = 0; i < sensors.size(); i++) {
				if ((sensors.get(i).getVendor().contains(this.preferredVendor))
						&& (sensors.get(i).getVersion() == this.preferredVersion)) {
					temp = sensors.get(i);
				}
			}
			this.mSensor = temp;
		} else {
			throw new NoSensorException();
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
	 * Calculates rotation around the Z axis (azimuth) of the device (and
	 * returns it).
	 */
	public double getAzimuth() {
		return Math.atan2(this.orientation[0], this.orientation[1]);
	}

	/**
	 * Calculates rotation around Y axis (pitch) of the device (and returns it).
	 */
	public double getPitch() {
		return Math.atan2(this.orientation[2], this.orientation[0]);
	}

	/**
	 * Calculates rotation around X axis (roll) of the device (and returns it).
	 */
	public double getRoll() {
		return Math.atan2(this.orientation[1], this.orientation[2]);
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// Not used
	}

	/**
	 * Important: Must be called when you want to stop reading data from
	 * Sensors.
	 */
	public void unregisterSensorListener() {
		this.mSensorManager.unregisterListener(this);
	}

	/**
	 * Used to reset the Listener when activity is resumed.
	 */
	public void resetListener() {
		this.mSensorManager.registerListener(this, this.mSensor,
				SensorManager.SENSOR_DELAY_NORMAL);
	}

}
