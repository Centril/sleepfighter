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
