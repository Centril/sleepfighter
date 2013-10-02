package se.chalmers.dat255.sleepfighter.reciever;

import se.chalmers.dat255.sleepfighter.activity.AlarmActivity;
import se.chalmers.dat255.sleepfighter.audio.AudioDriver;
import se.chalmers.dat255.sleepfighter.audio.VibrationManager;
import se.chalmers.dat255.sleepfighter.utils.android.AlarmWakeLocker;
import se.chalmers.dat255.sleepfighter.utils.android.IntentUtils;
import se.chalmers.dat255.sleepfighter.R;
import se.chalmers.dat255.sleepfighter.SFApplication;
import se.chalmers.dat255.sleepfighter.helper.NotificationHelper;
import se.chalmers.dat255.sleepfighter.model.Alarm;
import se.chalmers.dat255.sleepfighter.utils.MetaTextUtils;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

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

		// Fetching alarm, needed for notification info
		Alarm alarm = SFApplication.get().getPersister().fetchAlarmById(alarmId);
		
		// Start the alarm, this will wake the screen up!
		this.startAlarm(alarm, extras);
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
	 * @param alarm the alarm.
	 * @param context android context.
	 * @param extras extras to pass on.
	 */
	private void startAlarm( Alarm alarm, Bundle extras ) {
		startAudio(alarm);

		// start vibration.
		if (alarm.getVibrationEnabled()) {
			VibrationManager.getInstance().startVibrate(context.getApplicationContext());
		}

		Toast.makeText(context,
				"Alarm #" + alarm.getId() + " is ringing! GET UP!",
				Toast.LENGTH_LONG).show();

		// Create intent & re-put extras.
		Intent activityIntent;
		activityIntent = new Intent( context, AlarmActivity.class );
		activityIntent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
		activityIntent.putExtras( extras );

		// Start activity!
		context.startActivity( activityIntent );

		showNotification(alarm, activityIntent);
	}

	private void startAudio(Alarm alarm) {
		SFApplication app = SFApplication.get();
		AudioDriver driver = app.getAudioDriverFactory().produce(app,
				alarm.getAudioSource());
		app.setAudioDriver(driver);

		driver.play(alarm.getAudioConfig());
	}

	/**
	 * Launches notification showing that the alarm has gone off.
	 * 
	 * Clicking on it takes the user to AlarmActivity, where a challenge can be
	 * started.
	 * 
	 * @param alarm
	 *            the alarm
	 * @param activityIntent
	 *            the intent to be launched when the notification is clicked
	 */
	private void showNotification(Alarm alarm, Intent activityIntent) {
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
				activityIntent, 0);

		String name = MetaTextUtils.printAlarmName(context, alarm);

		// Localized string the name is inserted into
		String formatTitle = context.getString(R.string.notification_ringing_title);

		String title = String.format(formatTitle, name);
		String message = context
				.getString(R.string.notification_ringing_message);

		NotificationHelper.showNotification(context, title, message,
				pendingIntent);
	}
}