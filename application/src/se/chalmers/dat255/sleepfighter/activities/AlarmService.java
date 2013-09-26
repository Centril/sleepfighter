package se.chalmers.dat255.sleepfighter.activities;

import org.joda.time.DateTime;

import se.chalmers.dat255.sleepfighter.IntentUtils;
import se.chalmers.dat255.sleepfighter.SFApplication;
import se.chalmers.dat255.sleepfighter.model.Alarm;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * <p>AlarmService is responsible for registering and canceling alarms to Android OS.<br/>
 * It also handles drawer notifications.</p>
 *
 * Inspired by: http://www.appsrox.com/android/tutorials/remindme/2/#7
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Sep 25, 2013
 */
public class AlarmService extends IntentService {
	private static final String TAG = "se.chalmers.dat255.sleepfighter.activities.AlarmService";

	/**
	 * Registered commands for AlarmService
	 *
	 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
	 * @version 1.0
	 * @since Sep 26, 2013
	 */
	public enum Command {
		CREATE, CANCEL
	}

	/**
	 * Makes a call to the service for given context, command and alarm id.
	 *
	 * @param context android context.
	 * @param command the command to handle.
	 * @param alarm the alarm.
	 */
	public static void call( Context context, Command command, Alarm alarm ) {
		call( context, command, alarm.getId() );
	}

	/**
	 * Makes a call to the service for given context, command and alarm id.
	 *
	 * @param context android context.
	 * @param command the command to handle.
	 * @param alarmId the alarm id.
	 */
	public static void call( Context context, Command command, int alarmId ) {
		Intent service = new Intent( context, AlarmService.class );
		new IntentUtils( service ).setAlarmId( alarmId );
		service.setAction( command.name() );
		context.startService( service );
	}

	private IntentFilter matcher;

	public AlarmService() {
		super( TAG );

		this.matcher = new IntentFilter();
		for ( Command command : Command.values() ) {
			this.matcher.addAction( command.name() );
		}
	}

	@Override
	protected void onHandleIntent( Intent intent ) {
		final String action = intent.getAction();
		final int alarmId = new IntentUtils( intent ).getAlarmId();

		if ( matcher.matchAction( action ) ) {
			this.execute( action, alarmId );
		}
	}

	private void execute( final String action, final int alarmId ) {
		Command command = Command.valueOf( action );

		// Fetch alarm.
		Alarm alarm = SFApplication.get().getPersister().fetchAlarmById( alarmId );
		if ( alarm == null ) {
			throw new IllegalArgumentException( "No alarm was found with given id" );
		}

		// Make pending intent.
		Intent intent = new Intent( this, AlarmReceiver.class);
		new IntentUtils( intent ).setAlarmId( alarm );
		PendingIntent pi = PendingIntent.getBroadcast( this, -1, intent, PendingIntent.FLAG_UPDATE_CURRENT );

		AlarmManager am = (AlarmManager) this.getSystemService( Context.ALARM_SERVICE );

		// Handle commands.
		switch ( command ) {
		case CREATE:
			long scheduleTime = alarm.getNextMillis( new DateTime().getMillis() );
			am.set( AlarmManager.RTC_WAKEUP, scheduleTime, pi );
			break;

		case CANCEL:
			am.cancel( pi );
			break;
		}
	}
}