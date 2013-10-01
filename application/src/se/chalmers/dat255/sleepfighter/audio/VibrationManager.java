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
		startedVibration = false;
	}
	
	boolean startedVibration; 

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
		if(startedVibration) {
			return;
		}
		
		try {
			long[] pattern = {0, 1000, 1000};

			// 0 means vibrate indefinitely.
			vib.vibrate(pattern, 0);
			this.startedVibration = true;
		} catch (Exception e) {
			Debug.e(e);
		}
	}

	public void stopVibrate() {
		if(!startedVibration) 
			return;
		
		try {
			vib.cancel();
			this.startedVibration = false;
		} catch (Exception e) {
			Debug.e(e);
		}
	}
	
}
