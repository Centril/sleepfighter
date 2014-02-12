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

package se.toxbee.sleepfighter.android.power;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;

/**
 * Handles wake-locks for android in a static manner.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Sep 21, 2013
 */
@SuppressLint( "Wakelock" )
public class WakeLocker {
	public static final String TAG = WakeLocker.class.getName();

	private static WakeLock wakeLock;

	private static final int wakeLockLevel = PowerManager.PARTIAL_WAKE_LOCK;

	/**
	 * Acquires wake-lock if not already acquired.
	 *
	 * @param context the context to get PowerManager from.
	 */
	public static void acquire( Context context ) {
		if ( wakeLock != null ) {
			wakeLock.release();
		}

		PowerManager pm = (PowerManager) context.getSystemService( Context.POWER_SERVICE );
		wakeLock = pm.newWakeLock( wakeLockLevel, WakeLocker.TAG );
		wakeLock.acquire();
	}

	/**
	 * Releases wake-lock if there's any to release.
	 */
	public static void release() {
		if ( wakeLock != null ) {
			wakeLock.release();
		}

		wakeLock = null;
	}
}