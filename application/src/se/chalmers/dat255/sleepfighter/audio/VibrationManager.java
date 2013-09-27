package se.chalmers.dat255.sleepfighter.audio;

import se.chalmers.dat255.sleepfighter.utils.debug.Debug;
import android.content.Context;
import android.os.Vibrator;

/**
 * Responsible for making the device vibrate when the alarm goes off. 
 */
public class VibrationManager {

	private static VibrationManager instance = null;

	protected VibrationManager() {
	}

	/*
	 * The class is a singleton for now. We'll probably fix this later. 
	 */
	public static VibrationManager getInstance() {
		if (instance == null) {
			instance = new VibrationManager();
		}
		return instance;
	}
	
	Vibrator vib;
	
	public void setup(Context context) {
		
		
		try {
			vib = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);	
		} catch (Exception e) {
			Debug.e(e);
		}
	}
	
	public void startVibrate() {
		try {	
			// Start without a delay
			// Vibrate for 100 milliseconds
			// Sleep for 1000 milliseconds
			long[] pattern = {0, 100, 1000};

			// 0 means vibrate indefinitely.
			vib.vibrate(pattern, 0);
		} catch (Exception e) {
			Debug.e(e);
		}
	}

	public void stopVibrate() {
		try {
			vib.cancel();
		} catch (Exception e) {
			Debug.e(e);
		}
	}
	
}
