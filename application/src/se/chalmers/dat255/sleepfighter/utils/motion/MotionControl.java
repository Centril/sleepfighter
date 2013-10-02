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
 * code copied from
 * https://developer.android.com/guide/topics/sensors/sensors_motion.html.
 */
public class MotionControl implements SensorEventListener {

	private SensorManager mSensorManager;
	private Sensor mSensor;
	private int defaultType = Sensor.TYPE_GYROSCOPE;
	private float[] orientation = new float[3];
	private PropertyChangeSupport pcs;
	private WindowManager mWindowManager;
	
	private float z = 0f;

	// Create a constant to convert nanoseconds to seconds.
	private static final float NS2S = 1.0f / 1000000000.0f;
	private final float[] deltaRotationVector = new float[4];
	private float timestamp;
	private Display mDisplay;

	// private float deltaFloat = 1f / 1000f;
	// private long deltaTime = 1000000000L, lastTime = 0L;

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
				SensorManager.SENSOR_DELAY_NORMAL);

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
	
	public float getZ(){
		return z;
	}

	public void addListener(PropertyChangeListener pcl) {
		pcs.addPropertyChangeListener(pcl);
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// This timestamp's delta rotation to be multiplied by the current
		// rotation after computing it from the gyro sample data.
		if (timestamp != 0) {
			final float dT = (event.timestamp - timestamp) * NS2S;
			// Axis of the rotation sample, not normalized yet.
			float axisX = event.values[0];
			float axisY = event.values[1];
			float axisZ = event.values[2];
			
			z = axisZ;
			
			// Calculate the angular speed of the sample
			float omegaMagnitude = (float) Math.sqrt(axisX * axisX + axisY
					* axisY + axisZ * axisZ);

			// Normalize the rotation vector if it's big enough to get the axis
			// (that is, EPSILON should represent your maximum allowable margin
			// of error)
			
			float EPSILON = 1f;
			
			if (omegaMagnitude > EPSILON) {
				axisX /= omegaMagnitude;
				axisY /= omegaMagnitude;
				axisZ /= omegaMagnitude;
			}

			// Integrate around this axis with the angular speed by the
			// timestamp in order to get a delta rotation from this sample over
			// the timestamp
			// We will convert this axis-angle representation of the
			// delta rotation into a quaternion before turning it into the
			// rotation matrix.
			float thetaOverTwo = omegaMagnitude * dT / 2.0f;
			float sinThetaOverTwo = (float) Math.sin(thetaOverTwo);
			float cosThetaOverTwo = (float) Math.cos(thetaOverTwo);
			deltaRotationVector[0] = sinThetaOverTwo * axisX;
			deltaRotationVector[1] = sinThetaOverTwo * axisY;
			deltaRotationVector[2] = sinThetaOverTwo * axisZ;
			deltaRotationVector[3] = cosThetaOverTwo;
		}
		timestamp = event.timestamp;
		float[] deltaRotationMatrix = new float[9];
		SensorManager.getRotationMatrixFromVector(deltaRotationMatrix,
				deltaRotationVector);
		// User code should concatenate the delta rotation we computed with the
		// current rotation in order to get the updated rotation.
		// rotationCurrent = rotationCurrent * deltaRotationMatrix;
	}

	/*
	 * @Override public void onSensorChanged(SensorEvent event) { if
	 * (event.sensor.getType() != mSensor.getType()) { return; } // if (lastTime
	 * == 0) { // lastTime = event.timestamp; // } // if ((lastTime + deltaTime)
	 * > event.timestamp) { // return; // } // lastTime = event.timestamp; //
	 * switch (mDisplay.getRotation()) { // case Surface.ROTATION_0: // if
	 * (Math.abs(Math.abs(orientation[0]) - Math.abs(event.values[0])) > //
	 * deltaFloat // && Math.abs(Math.abs(orientation[0]) // -
	 * Math.abs(event.values[0])) > deltaFloat) { // orientation[0] =
	 * event.values[0]; // orientation[1] = event.values[1]; //
	 * pcs.firePropertyChange("event", 0, 1); // } // break; // case
	 * Surface.ROTATION_90: // if (Math.abs(Math.abs(orientation[0]) -
	 * Math.abs(event.values[1])) > // deltaFloat // &&
	 * Math.abs(Math.abs(orientation[1]) // - Math.abs(event.values[0])) >
	 * deltaFloat) { // orientation[0] = -event.values[1]; // orientation[1] =
	 * event.values[0]; // pcs.firePropertyChange("event", 0, 1); // } // break;
	 * // case Surface.ROTATION_180: // if (Math.abs(Math.abs(orientation[0]) -
	 * Math.abs(event.values[0])) > // deltaFloat // &&
	 * Math.abs(Math.abs(orientation[0]) // - Math.abs(event.values[0])) >
	 * deltaFloat) { // orientation[0] = -event.values[0]; // orientation[1] =
	 * -event.values[1]; // pcs.firePropertyChange("event", 0, 1); // } //
	 * break; // case Surface.ROTATION_270: // if
	 * (Math.abs(Math.abs(orientation[0]) - Math.abs(event.values[1])) > //
	 * deltaFloat // && Math.abs(Math.abs(orientation[1]) // -
	 * Math.abs(event.values[0])) > deltaFloat) { // orientation[0] =
	 * event.values[1]; // orientation[1] = -event.values[0]; //
	 * pcs.firePropertyChange("event", 0, 1); // } // break; // } }
	 */
}