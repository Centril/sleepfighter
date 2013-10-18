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

import se.chalmers.dat255.sleepfighter.android.power.WakeLocker;
import se.chalmers.dat255.sleepfighter.service.LocationService;
import se.chalmers.dat255.sleepfighter.utils.debug.Debug;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

/**
 * 10 seconds before an alarm goes off onReceiver is called for this class.
 * In onReceive() LocationService is started, which fetches the current location and the weather of that location. 
 */
public class LocationReceiver extends BroadcastReceiver {

	private static final int SECOND_TO_MS_FACTOR = 1000;
	
	private Context context;

	/**
	 * Sets up google play services. We need this to get the current location.
	 */
	private void setupGooglePlay() {
		int status = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(context);
		if (status != ConnectionResult.SUCCESS) {
			// Google Play Services are not available.
			int requestCode = 10;
			Debug.d("google play not available: " + requestCode);
			/*GooglePlayServicesUtil.getErrorDialog(status, this, requestCode)
					.show();*/
		} else {
			Debug.d("google maps is setup");
			// google map is availabel
		}
	}
	

	@Override
	public void onReceive( Context context, Intent intent ) {
		
		WakeLocker.acquire( context );
		
		this.context = context;
		
		setupGooglePlay();
				
		Intent serviceIntent = new Intent(context,LocationService.class);
		this.context.startService(serviceIntent);

		Debug.d("receive location receiver");
	}


	/**
	 * Schedules for a fix.
	 *
	 * @param context android context.
	 * @param alarmTime the time of earliest alarm in Unix time.
	 */
	public static void scheduleFix( Context context, long alarmTime ) {
		
		Debug.d("schedule fix location receiver");

		long unixTime = alarmTime - 10 * SECOND_TO_MS_FACTOR;

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