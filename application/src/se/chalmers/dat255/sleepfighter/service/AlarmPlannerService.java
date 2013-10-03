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
package se.chalmers.dat255.sleepfighter.service;

import net.engio.mbassy.listener.Handler;

import org.joda.time.DateTime;
import org.joda.time.MutableDateTime;

import se.chalmers.dat255.sleepfighter.R;
import se.chalmers.dat255.sleepfighter.SFApplication;
import se.chalmers.dat255.sleepfighter.activity.MainActivity;
import se.chalmers.dat255.sleepfighter.helper.NotificationHelper;
import se.chalmers.dat255.sleepfighter.model.Alarm;
import se.chalmers.dat255.sleepfighter.model.Alarm.ScheduleChangeEvent;
import se.chalmers.dat255.sleepfighter.model.AlarmList;
import se.chalmers.dat255.sleepfighter.model.AlarmTimestamp;
import se.chalmers.dat255.sleepfighter.reciever.AlarmReceiver;
import se.chalmers.dat255.sleepfighter.utils.MetaTextUtils;
import se.chalmers.dat255.sleepfighter.utils.android.IntentUtils;
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
	private static final String TAG = AlarmPlannerService.class.getSimpleName();

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
		CREATE, CANCEL, SNOOZE
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
		case SNOOZE:
			this.snooze( new IntentUtils( intent ).getAlarmId() );
			break;
		case CANCEL:
			this.cancel();
			break;
		}
	}

	private void snooze(int alarmId) {
		Alarm alarm = SFApplication.get().getPersister().fetchAlarmById( alarmId );
		if ( alarm == null ) {
			throw new IllegalArgumentException( "No alarm was found with given id" );
		}
		MutableDateTime dateTime = new MutableDateTime();
		
		// TODO get property from alarm
		int mins = 5;
		
		dateTime.addMinutes(mins);
		schedule(dateTime.getMillis(), alarm);
		showNotification(alarm, dateTime.toString("HH:mm"));
	}

	private void create( int alarmId ) {
		// Fetch alarm.
		Alarm alarm = SFApplication.get().getPersister().fetchAlarmById( alarmId );
		if ( alarm == null ) {
			throw new IllegalArgumentException( "No alarm was found with given id" );
		}

		// Get alarm RTC time, could be null cause of threading, so check!
		Long scheduleTime = alarm.getNextMillis( new DateTime().getMillis() );
		if ( scheduleTime == null ) {
			return;
		}
		
		schedule(scheduleTime, alarm);

		showNotification(alarm, alarm.getTimeString());
	}

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
	private void showNotification(Alarm alarm, String time) {
		Intent mainActIntent = new Intent(getApplicationContext(),
				MainActivity.class);
		PendingIntent mainActPI = PendingIntent.getActivity(this, 0,
				mainActIntent, 0);

		String name = MetaTextUtils.printAlarmName(this, alarm);

		// Localized strings which we inserts current time and name into
		String titleFormat = getString(R.string.notification_pending_title);
		String messageFormat = getString(R.string.notification_pending_message);

		String title = String.format(titleFormat, name);
		String message = String.format(messageFormat, time);

		NotificationHelper.showNotification(this, title, message, mainActPI);
	}

	private void cancel() {
		this.getAlarmManager().cancel( this.makePendingIntent( Alarm.NOT_COMMITTED_ID ) );
		Log.d( "AlarmPlannerService", "Cancelling!" );

		// Remove app's sticky notification
		NotificationHelper.removeNotification(this);
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
