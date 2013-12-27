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
package se.toxbee.sleepfighter.audio;

import se.toxbee.sleepfighter.utils.debug.Debug;
import android.content.Context;
import android.os.Vibrator;

/**
 * Responsible for making the device vibrate when the alarm goes off. 
 */
public enum VibrationManager {
	INSTANCE;

	private static final long[] PATTERN = { 0, 1000, 1000 };
	private boolean startedVibration = false; 
	
	public static VibrationManager getInstance() {
		return INSTANCE;
	}
	
	public void startVibrate(Context context) {
		if ( startedVibration ) {
			return;
		}

		Vibrator vib = service( context );

		try {
			// 0 means vibrate indefinitely.
			vib.vibrate( PATTERN, 0 );
			this.startedVibration = true;
		} catch ( Exception e ) {
			Debug.e( e );
		}
	}

	public void stopVibrate( Context context ) {
		if ( !startedVibration ) {
			return;
		}

		Vibrator vib = service( context );

		try {
			vib.cancel();
			this.startedVibration = false;
		} catch ( Exception e ) {
			Debug.e( e );
		}
	}

	private Vibrator service( Context ctx ) {
		return (Vibrator) ctx.getSystemService( Context.VIBRATOR_SERVICE );
	}
}
