package se.chalmers.dat255.sleepfighter.activities;

import net.engio.mbassy.listener.Handler;

import org.joda.time.DateTime;

import se.chalmers.dat255.sleepfighter.IntentUtils;
import se.chalmers.dat255.sleepfighter.SFApplication;
import se.chalmers.dat255.sleepfighter.model.Alarm;
import se.chalmers.dat255.sleepfighter.model.Alarm.DateChangeEvent;
import se.chalmers.dat255.sleepfighter.model.AlarmList;
import se.chalmers.dat255.sleepfighter.model.AlarmTimestamp;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

/**
 * <p>AlarmPlannerService is responsible for registering and canceling alarms to Android OS.<br/>
 * It also handles drawer notifications.</p>
 *
 * Inspired by: http://www.appsrox.com/android/tutorials/remindme/2/#7
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Sep 25, 2013
 */
public class AlarmPlannerService extends IntentService {
	private static final String TAG = "se.chalmers.dat255.sleepfighter.activities.AlarmService";

	/**
	 * Handles changes in alarms and alarm list and regarding and plans.
	 *
	 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
	 * @version 1.0
	 * @since Sep 26, 2013
	 */
	public static final class ChangeHandler {
		private Context context;
		private AlarmList list;

		/**
		 * Constructs the ChangeHandler.
		 *
		 * @param context android context.
		 * @param list the AlarmList object.
		 */
		public ChangeHandler( Context context, AlarmList list ) {
			this.context = context;
			this.list = list;
		}

		private void handleChange() {
			AlarmTimestamp at = this.list.getEarliestAlarm( new DateTime().getMillis() );
			Log.d( this.getClass().getName(), "handleChange, " + at );

			if ( at == AlarmTimestamp.INVALID ) {
				call( this.context, Command.CANCEL, Alarm.NOT_COMMITTED_ID );
			} else {
				call( this.context, Command.CREATE, at.getAlarm().getId() );
			}
		}

		/**
		 * Handles a change in time related data in any alarm.
		 *
		 * @param evt the event.
		 */
		@Handler
		public void handleListChange( AlarmList.Event evt ) {
			this.handleChange();
		}

		/**
		 * Handles a change in time related data in any alarm.
		 *
		 * @param evt the event.
		 */
		@Handler
		public void handleDateChange( DateChangeEvent evt ) {
			this.handleChange();
		}
	}

	/**
	 * Registered commands for AlarmPlannerService
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
	 * @param alarmId the alarm id, not used if command == Command.CANCEL.
	 */
	public static void call( Context context, Command command, int alarmId ) {
		Intent service = new Intent( context, AlarmPlannerService.class );
		service.setAction( command.name() );
		new IntentUtils( service ).setAlarmId( alarmId );
		context.startService( service );
	}

	private IntentFilter matcher;

	public AlarmPlannerService() {
		super( TAG );

		this.matcher = new IntentFilter();
		for ( Command command : Command.values() ) {
			this.matcher.addAction( command.name() );
		}
	}

	@Override
	protected void onHandleIntent( Intent intent ) {
		final String action = intent.getAction();

		if ( !matcher.matchAction( action ) ) {
			return;
		}

		// Handle commands.
		switch ( Command.valueOf( action ) ) {
		case CREATE:
			this.create( new IntentUtils( intent ).getAlarmId() );
			break;

		case CANCEL:
			this.cancel();
			break;
		}
	}

	private void create( int alarmId ) {
		// Fetch alarm.
		Alarm alarm = SFApplication.get().getPersister().fetchAlarmById( alarmId );
		if ( alarm == null ) {
			throw new IllegalArgumentException( "No alarm was found with given id" );
		}

		PendingIntent pi = this.makePendingIntent( alarm.getId() );

		long scheduleTime = alarm.getNextMillis( new DateTime().getMillis() );
		this.getAlarmManager().set( AlarmManager.RTC_WAKEUP, scheduleTime, pi );

		Log.d( "AlarmPlannerService", "Setting! " + alarm.toString() );
	}

	private void cancel() {
		this.getAlarmManager().cancel( this.makePendingIntent( Alarm.NOT_COMMITTED_ID ) );
		Log.d( "AlarmPlannerService", "Cancelling!" );
	}

	private AlarmManager getAlarmManager() {
		return (AlarmManager) this.getSystemService( Context.ALARM_SERVICE );
	}

	private PendingIntent makePendingIntent( int alarmId ) {
		Intent intent = new Intent( this, AlarmReceiver.class);
		new IntentUtils( intent ).setAlarmId( alarmId );
		return PendingIntent.getBroadcast( this, -1, intent, PendingIntent.FLAG_UPDATE_CURRENT );
	}
}