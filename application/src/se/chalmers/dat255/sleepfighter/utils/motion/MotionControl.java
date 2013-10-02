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
	private float[] orientation = new float[3];
	private PropertyChangeSupport pcs;
	private WindowManager mWindowManager;

	private double angle = 0d;

	// Create a constant to convert nanoseconds to seconds.
	private static final float NS2S = 1.0f / 1000000000.0f;
	private float timestamp;
	private Display mDisplay;

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
				SensorManager.SENSOR_DELAY_GAME);

		// Get an instance of the WindowManager
		mWindowManager = (WindowManager) activity
				.getSystemService(Activity.WINDOW_SERVICE);
		mDisplay = mWindowManager.getDefaultDisplay();

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

	public ArrayList<Float> getFloatList() {
		ArrayList<Float> floatList = new ArrayList<Float>(orientation.length);
		for (int i = 0; i < orientation.length; i++) {
			floatList.add(i, orientation[i]);
		}
		return floatList;
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
		if (event.sensor != mSensor)
			return;

		float aX = event.values[0];
		float aY = event.values[1];
		// float aZ = event.values[2];

		// Calculate rotation on Z axis.
		angle = Math.atan2(aX, aY);

		// If rotation between 0.5 and Math.PI - 0.5, update listeners
		if (angle % (Math.PI / 2) >= 0.5
				&& angle % (Math.PI / 2) <= (Math.PI / 2) - 0.5) {
			pcs.firePropertyChange(null, 0, 1);
		}
	}
}