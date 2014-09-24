/*
 * Copyright 2014 toxbee.se
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package se.toxbee.sleepfighter.audio;

import android.content.Context;
import android.os.Vibrator;
import android.util.Log;

/**
 * Responsible for making the device vibrate when the alarm goes off. 
 */
public enum VibrationManager {
	INSTANCE;

	private static final String TAG = VibrationManager.class.getSimpleName();

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
			Log.e( TAG, e.toString() );
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
			Log.e( TAG, e.toString() );
		}
	}

	private Vibrator service( Context ctx ) {
		return (Vibrator) ctx.getSystemService( Context.VIBRATOR_SERVICE );
	}
}
