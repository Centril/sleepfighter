package se.chalmers.dat255.sleepfighter.utils.motion;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;

import se.chalmers.dat255.sleepfighter.utils.debug.Debug;
import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * A class for handling motion events that can be used all across the app.
 */
public class MotionControl implements SensorEventListener {

	private SensorManager mSensorManager;
	private Sensor mSensor;
	private float[] orientation;
	private PropertyChangeSupport pcs;

	/**
	 * Sets mSensor to Accelerometer.
	 * 
	 * @param activity
	 */
	public MotionControl(Activity activity) {
		mSensorManager = (SensorManager) activity
				.getSystemService(Activity.SENSOR_SERVICE);
		setSensor(Sensor.TYPE_ACCELEROMETER);
		Debug.d(new Boolean(mSensorManager.registerListener(this, mSensor,
				SensorManager.SENSOR_DELAY_GAME)).toString());

		pcs = new PropertyChangeSupport(this);
	}

	public boolean setSensor(int type) {
		Sensor temp = mSensorManager.getDefaultSensor(type);
		if (temp != null) {
//			Debug.d("Sensor of type " + temp.toString() + " exists");
//			List<Sensor> sensors = mSensorManager.getSensorList(type);
//			for (int i = 0; i < sensors.size(); i++) {
//				if ((sensors.get(i).getVendor().contains("Google Inc."))
//						&& (sensors.get(i).getVersion() == 3)) {
//					mSensor = sensors.get(i);
//				}
//			}
			mSensor = temp;
			return true;
		} else {
			Debug.d("Sensor of the request type does not exist");
			return false;
		}
	}

	public float[] getFloat() {
		return orientation;
	}

	public void addListener(PropertyChangeListener pcl) {
		pcs.addPropertyChangeListener(pcl);
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onSensorChanged(SensorEvent arg0) {
		orientation = arg0.values;
		pcs.firePropertyChange("event", 0, 1);
	}

}
