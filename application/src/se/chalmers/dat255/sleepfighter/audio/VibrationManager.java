package se.chalmers.dat255.sleepfighter.audio;

import se.chalmers.dat255.sleepfighter.utils.debug.Debug;
import android.content.Context;
import android.os.Vibrator;

/**
 * Responsible for making the device vibrate when the alarm goes off. 
 */
public class VibrationManager {

	private static VibrationManager instance = null;

	private boolean startedVibration; 

	private VibrationManager() {
		startedVibration = false;
	}

	/*
	 * The class is a singleton for now. We'll probably fix this later. 
	 */
	public synchronized static VibrationManager getInstance() {
		if (instance == null) {
			instance = new VibrationManager();
		}
		return instance;
	}
	
	public void startVibrate(Context context) {
		if(startedVibration) {
			return;
		}
		
		Vibrator vib = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);	
		
		try {
			long[] pattern = {0, 1000, 1000};

			// 0 means vibrate indefinitely.
			vib.vibrate(pattern, 0);
			this.startedVibration = true;
		} catch (Exception e) {
			Debug.e(e);
		}
	}

	public void stopVibrate(Context context) {
		if (!startedVibration) {
			return;
		}
		Vibrator vib = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);	

		try {
			vib.cancel();
			this.startedVibration = false;
		} catch (Exception e) {
			Debug.e(e);
		}
	}
	
}
