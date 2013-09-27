package se.chalmers.dat255.sleepfighter.reciever;

import se.chalmers.dat255.sleepfighter.activity.AlarmActivity;
import se.chalmers.dat255.sleepfighter.audio.AlarmAudioManager;
import se.chalmers.dat255.sleepfighter.utils.android.AlarmWakeLocker;
import se.chalmers.dat255.sleepfighter.utils.android.IntentUtils;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * <p>AlarmReceiver is responsible for receiving broadcasts<br/>
 * from Android OS and starting {@link AlarmActivity}.</p>
 *
 * <p>Any checking if the alarm activity should be started happens<br/>
 * in {@link AlarmReceiver#onReceive(Context, Intent)}.</p>
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Sep 21, 2013
 */
public class AlarmReceiver extends BroadcastReceiver {
	private Context context;

	@Override
	public void onReceive( Context context, Intent intent ) {
		// Acquire wake-lock.
		AlarmWakeLocker.acquire( context );

		this.context = context;

		// Fetch extras.
		Bundle extras = intent.getExtras();
		final int alarmId = new IntentUtils( intent ).getAlarmId();

		if ( !this.isRequirementsMet( alarmId ) ) {
			return;
		}

		// Start the alarm, this will wake the screen up!
		this.startAlarm( extras );
	}

	/**
	 * Checks whether or not requirements are met for starting alarm.<br/>
	 *
	 * @param alarmId the ID of the alarm.
	 * @return true if requirements are met.
	 */
	private boolean isRequirementsMet( int alarmId ) {
		// TODO: Do something fancy with alarmId, i.e check GPS to see if we should actually start Alarm.
		return true;
	}




	/**
	 * Starts AlarmActivity, extras are passed on.
	 *
	 * @param context android context.
	 * @param extras extras to pass on.
	 */
	private void startAlarm( Bundle extras ) {
		// Create intent & re-put extras.
		Intent activityIntent;
		activityIntent = new Intent( context, AlarmActivity.class );
		activityIntent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
		activityIntent.putExtras( extras );

		// Start activity!
		context.startActivity( activityIntent );
		
		// TODO temporary
		AlarmAudioManager.getInstance().play();
	}
}