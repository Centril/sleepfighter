package se.chalmers.dat255.sleepfighter.utils.motion;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorManager;

public class MotionControl {

	private SensorManager mSensorManager;
	private Sensor mSensor;

	public MotionControl(Activity activity) {
		mSensorManager = (SensorManager) activity
				.getSystemService(Activity.SENSOR_SERVICE);
	}

}
