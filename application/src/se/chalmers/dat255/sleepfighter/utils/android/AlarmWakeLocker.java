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