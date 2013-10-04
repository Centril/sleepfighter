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
package se.chalmers.dat255.sleepfighter.utils.android;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;

/**
 * Handles wake-locks for alarm.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Sep 21, 2013
 */
@SuppressLint( "Wakelock" )
public class AlarmWakeLocker {
	public static final String TAG = "se.chalmers.dat255.sleepfighter.activities.AlarmWakeLocker";

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
		wakeLock = pm.newWakeLock( wakeLockLevel, AlarmWakeLocker.TAG );
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
