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

package se.toxbee.sleepfighter.service;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import net.engio.mbassy.listener.Handler;
import net.engio.mbassy.listener.Listener;
import net.engio.mbassy.listener.References;

import org.joda.time.DateTime;
import org.joda.time.MutableDateTime;

import se.toxbee.sleepfighter.R;
import se.toxbee.sleepfighter.activity.AlarmActivity;
import se.toxbee.sleepfighter.activity.MainActivity;
import se.toxbee.sleepfighter.app.SFApplication;
import se.toxbee.sleepfighter.helper.AlarmIntentHelper;
import se.toxbee.sleepfighter.helper.NotificationHelper;
import se.toxbee.sleepfighter.model.Alarm;
import se.toxbee.sleepfighter.model.Alarm.ScheduleChangeEvent;
import se.toxbee.sleepfighter.model.AlarmList;
import se.toxbee.sleepfighter.model.AlarmTimestamp;
import se.toxbee.sleepfighter.receiver.AlarmReceiver;
import se.toxbee.sleepfighter.receiver.GPSFilterRefreshReceiver;
import se.toxbee.sleepfighter.receiver.LocationReceiver;

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
	private static final String TAG = AlarmPlannerService.class.getSimpleName();

	private static boolean isRegistered = false;

	/**
	 * Registers the {@link ChangeHandler}.
	 */
	public static void register() {
		if ( !isRegistered ) {
			isRegistered = false;

			SFApplication app = SFApplication.get();
			ChangeHandler handler = new ChangeHandler( app, app.getAlarms() );

			app.getBus().subscribe( handler );
		}
	}

	/**
	 * Handles changes in alarms and alarm list and regarding and plans.
	 *
	 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
	 * @version 1.0
	 * @since Sep 26, 2013
	 */
	@Listener(references = References.Strong)
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
		public void handleDateChange( ScheduleChangeEvent evt ) {
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
		/**
		 * Used with an {@link Alarm} ID to schedule the alarm at its next
		 * occurrence. This will override any previous scheduled Alarm.<br/>
		 * 
		 * At the alarm's time, {@code onRecieve} in {@link AlarmReceiver} will
		 * be called.
		 */
		CREATE,
		/**
		 * Used to cancel the scheduled {@link Alarm}.
		 */
		CANCEL,
		/**
		 * Used with an {@link Alarm} ID to tell the service to reschedule an alarm that
		 * has gone of, in the amount of minutes from now specified in {@link Alarm}.
		 */
		SNOOZE
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
		new AlarmIntentHelper( service ).setAlarmId( alarmId );
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
		String action = intent.getAction();

		if ( !matcher.matchAction( action ) ) {
			return;
		}

		// Handle commands.
		switch ( Command.valueOf( action ) ) {
		case CREATE:
			this.create( new AlarmIntentHelper( intent ).getAlarmId() );
			break;
		case SNOOZE:
			this.snooze( new AlarmIntentHelper( intent ).getAlarmId() );
			break;
		case CANCEL:
			this.cancel();
			break;
		}
	}

	/**
	 * Schedules an alarm at the next time it should go off.
	 * 
	 * @param alarmId
	 *            the alarm's ID
	 */
	private void create( int alarmId ) {
		SFApplication app = SFApplication.get();

		// Fetch alarm.
		Alarm alarm = app.getPersister().fetchAlarmById( alarmId );
		if ( alarm == null ) {
			throw new IllegalArgumentException( "No alarm was found with given id" );
		}

		// Get alarm RTC time, could be null cause of threading, so check!
		Long scheduleTime = alarm.getNextMillis( new DateTime().getMillis() );
		if ( scheduleTime == null ) {
			return;
		}

		GPSFilterRefreshReceiver.scheduleFix( app, scheduleTime );

		if (alarm.isSpeech()) {
			LocationReceiver.scheduleFix(app, scheduleTime);
		}
		schedule(scheduleTime, alarm);

		showPendingNotification(alarm);
	}

	/**
	 * Schedule an alarm to go off at a certain time.
	 * 
	 * @param scheduleTime
	 *            the time, in milliseconds (UTC), when the Alarm should go off
	 * @param alarm
	 *            the alarm
	 */
	private void schedule(long scheduleTime, Alarm alarm) {
		PendingIntent pi = this.makePendingIntent( alarm.getId() );

		this.getAlarmManager().set( AlarmManager.RTC_WAKEUP, scheduleTime, pi );

		Log.d(getClass().getSimpleName(), "Scheduled alarm [" + alarm.toString()
				+ "] at " + scheduleTime);
	}

	/**
	 * Shows a notification for a pending alarm.
	 * 
	 * The user can click on it to get to MainActivity, where it can be turned
	 * off easily.
	 * 
	 * @param alarm
	 *            the alarm
	 */
	private void showPendingNotification(Alarm alarm) {
		Intent mainActIntent = new Intent(getApplicationContext(),
				MainActivity.class);
		PendingIntent mainActPI = PendingIntent.getActivity(this, 0,
				mainActIntent, 0);

		String name = alarm.printName();
		String time = alarm.getTime().getTimeString();

		// Localized strings which we inserts current time and name into
		String titleFormat = getString(R.string.notification_pending_title);
		String messageFormat = getString(R.string.notification_pending_message);

		String title = String.format(titleFormat, name);
		String message = String.format(messageFormat, time);

		NotificationHelper.getInstance().showNotification(this, title, message,
				mainActPI);
	}

	/**
	 * Shows a notification for an alarm when snoozing has been done.
	 * 
	 * The user can click on it to get to {@link AlarmActivity}, where it can be
	 * disabled.
	 * 
	 * @param alarm
	 *            the alarm
	 * @param time
	 *            the new time the alarm will go off, as a string
	 */
	private void showSnoozingNotification(Alarm alarm, String time) {
		Intent alarmIntent = new Intent(getApplicationContext(),
				AlarmActivity.class);
		new AlarmIntentHelper(alarmIntent).setAlarmId(alarm);
		PendingIntent alarmPI = PendingIntent.getActivity(this, 0,
				alarmIntent, 0);

		String name = alarm.printName();

		// Localized strings which we inserts current time and name into
		String titleFormat = getString(R.string.notification_snooze_title);
		String messageFormat = getString(R.string.notification_snooze_message);

		String title = String.format(titleFormat, name);
		String message = String.format(messageFormat, time);

		NotificationHelper.getInstance().showNotification(
				getApplicationContext(), title, message, alarmPI);
	}

	/**
	 * Cancels any scheduled alarm.
	 */
	private void cancel() {
		this.getAlarmManager().cancel( this.makePendingIntent( Alarm.NOT_COMMITTED_ID ) );
		Log.d( "AlarmPlannerService", "Cancelling!" );

		// Remove app's sticky notification
		NotificationHelper.getInstance().removeNotification(
				getApplicationContext());

		// Unschedule any location fixes.
		GPSFilterRefreshReceiver.unscheduleFix( SFApplication.get() ); 
		LocationReceiver.unscheduleFix( SFApplication.get() );
	}

	/**
	 * Reschedule an {@link Alarm}, that has gone of, to some minutes from now,
	 * defined by the alarm itself.
	 * 
	 * @param alarmId
	 *            the alarm's id
	 */
	private void snooze(int alarmId) {
		Alarm alarm = SFApplication.get().getPersister().fetchAlarmById( alarmId );
		if ( alarm == null ) {
			throw new IllegalArgumentException( "No alarm was found with given id" );
		}

		
		// Determine which time to schedule at by adding offset minutes from alarm to current time
		MutableDateTime dateTime = MutableDateTime.now();
		
		int mins = alarm.getSnoozeConfig().getSnoozeTime();

		dateTime.addMinutes(mins);

		long scheduleTime = dateTime.getMillis();
		schedule(scheduleTime, alarm);
		showSnoozingNotification(alarm, dateTime.toString("HH:mm"));
	}

	private AlarmManager getAlarmManager() {
		return (AlarmManager) this.getSystemService( Context.ALARM_SERVICE );
	}

	private PendingIntent makePendingIntent( int alarmId ) {
		Intent intent = new Intent( this, AlarmReceiver.class);
		new AlarmIntentHelper( intent ).setAlarmId( alarmId );
		return PendingIntent.getBroadcast( this, -1, intent, PendingIntent.FLAG_UPDATE_CURRENT );
	}
}