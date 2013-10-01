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
import android.view.Surface;
import android.view.WindowManager;

/**
 * A class for handling motion events that can be used all across the app.
 */
public class MotionControl implements SensorEventListener {

	private SensorManager mSensorManager;
	private Sensor mSensor;
	private int defaultType = Sensor.TYPE_ACCELEROMETER;
	private float[] orientation;
	private PropertyChangeSupport pcs;
	private WindowManager mWindowManager;
	private Display mDisplay;

	/**
	 * Sets mSensor to Accelerometer by default, possible to set to other
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
				SensorManager.SENSOR_DELAY_NORMAL);

		// Get an instance of the WindowManager
		mWindowManager = (WindowManager) activity
				.getSystemService(Activity.WINDOW_SERVICE);
		mDisplay = mWindowManager.getDefaultDisplay();

		pcs = new PropertyChangeSupport(this);
	}

	public boolean setSensor(int type) {
		Sensor temp = mSensorManager.getDefaultSensor(type);
		if (temp != null) {
			// Debug.d("Sensor of type " + temp.toString() + " exists");
			// List<Sensor> sensors = mSensorManager.getSensorList(type);
			// for (int i = 0; i < sensors.size(); i++) {
			// if ((sensors.get(i).getVendor().contains("Google Inc."))
			// && (sensors.get(i).getVersion() == 3)) {
			// mSensor = sensors.get(i);
			// }
			// }
			mSensor = temp;
			return true;
		} else {
			Debug.d("Sensor of the request type does not exist");
			return false;
		}
	}

	// For debugging
	public ArrayList<Float> getFloatList() {
		ArrayList<Float> floatList = new ArrayList<Float>(orientation.length);
		for (int i = 0; i < orientation.length; i++) {
			floatList.add(i, orientation[i]);
		}
		return floatList;
	}

	public void addListener(PropertyChangeListener pcl) {
		pcs.addPropertyChangeListener(pcl);
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		if (event.sensor.getType() != mSensor.getType()) {
			return;
		}

		orientation = new float[2];

		switch (mDisplay.getRotation()) {
		case Surface.ROTATION_0:
			orientation[0] = event.values[0];
			orientation[1] = event.values[1];
			pcs.firePropertyChange("event", 0, 1);
			break;
		case Surface.ROTATION_90:
			orientation[0] = -event.values[1];
			orientation[1] = event.values[0];
			pcs.firePropertyChange("event", 0, 1);
			break;
		case Surface.ROTATION_180:
			orientation[0] = -event.values[0];
			orientation[1] = -event.values[1];
			pcs.firePropertyChange("event", 0, 1);
			break;
		case Surface.ROTATION_270:
			orientation[0] = event.values[1];
			orientation[1] = -event.values[0];
			pcs.firePropertyChange("event", 0, 1);
			break;
		}

	}

}
