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
package se.chalmers.dat255.sleepfighter.receiver;

import se.chalmers.dat255.sleepfighter.SFApplication;
import se.chalmers.dat255.sleepfighter.preference.GlobalPreferencesManager;
import se.chalmers.dat255.sleepfighter.utils.android.AlarmWakeLocker;
import se.chalmers.dat255.sleepfighter.utils.debug.Debug;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;

public class LocationReceiver extends BroadcastReceiver {

	private static final int SECOND_TO_MS_FACTOR = 1000;

	@Override
	public void onReceive( Context context, Intent intent ) {
		
		AlarmWakeLocker.acquire( context );
		
		Debug.d("receive location receiver");
	}


	/**
	 * Schedules for a fix.
	 *
	 * @param context android context.
	 * @param alarmTime the time of earliest alarm in Unix time.
	 */
	public static void scheduleFix( Context context, long alarmTime ) {
		SFApplication app = SFApplication.get();
		GlobalPreferencesManager manager = app.getPrefs();
		
		Debug.d("schedule fix location receiver");

		long unixTime = alarmTime - 20 * SECOND_TO_MS_FACTOR;

		// Make pending intent.
		Bundle bundle = new Bundle();
		PendingIntent pi = makePi( context, bundle );

		// Schedule.
		getAm( context ).set( AlarmManager.RTC_WAKEUP, unixTime, pi );
	}

	/**
	 * Unschedules fixes if any.
	 *
	 * @param context android context.
	 */
	public static void unscheduleFix( Context context ) {
		getAm( context ).cancel( makePi( context, null ) );
		unscheduleUpdates( context );
	}


	private static void unscheduleUpdates( Context context ) {
		getLM( context ).removeUpdates( makePi( context, null ) );
	}

	private static PendingIntent makePi( Context context, Bundle extras ) {
		Intent intent = new Intent( context, LocationReceiver.class );

		if ( extras != null ) {
			intent.putExtras( extras );
		}

		PendingIntent pi = PendingIntent.getBroadcast( context, -1, intent, PendingIntent.FLAG_UPDATE_CURRENT );
		return pi;
	}

	private static AlarmManager getAm( Context context ) {
		return (AlarmManager) context.getSystemService( Context.ALARM_SERVICE );
	}

	private static LocationManager getLM( Context context ) {
		return (LocationManager) context.getSystemService( Context.LOCATION_SERVICE );
	}
}