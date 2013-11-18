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
package se.toxbee.sleepfighter.receiver;

import se.toxbee.sleepfighter.R;
import se.toxbee.sleepfighter.activity.AlarmActivity;
import se.toxbee.sleepfighter.android.power.WakeLocker;
import se.toxbee.sleepfighter.app.SFApplication;
import se.toxbee.sleepfighter.audio.AudioDriver;
import se.toxbee.sleepfighter.audio.VibrationManager;
import se.toxbee.sleepfighter.gps.GPSFilterRequisitor;
import se.toxbee.sleepfighter.helper.AlarmIntentHelper;
import se.toxbee.sleepfighter.helper.NotificationHelper;
import se.toxbee.sleepfighter.model.Alarm;
import se.toxbee.sleepfighter.service.FadeVolumeService;
import se.toxbee.sleepfighter.speech.SpeechLocalizer;
import se.toxbee.sleepfighter.speech.TextToSpeechUtil;
import se.toxbee.sleepfighter.text.MetaTextUtils;
import se.toxbee.sleepfighter.utils.debug.Debug;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnUtteranceCompletedListener;;

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
// UtteranceProgressListener not available in api < 15
@SuppressWarnings("deprecation")
public class AlarmReceiver extends BroadcastReceiver {
	private Context context;
	private Alarm alarm;

	@Override
	public void onReceive( Context context, Intent intent ) {
		// Acquire wake-lock.
		WakeLocker.acquire( context );

		this.context = context;

		final int alarmId = new AlarmIntentHelper( intent ).getAlarmId();

		if ( !this.isRequirementsMet( alarmId ) ) {
			WakeLocker.release();
			return;
		}

		// Fetching alarm
		this.alarm = SFApplication.get().getPersister().fetchAlarmById(alarmId);

		// Fetch extras.
		Bundle extras = intent.getExtras();

		// Start the alarm, this will wake the screen up!
		this.startAlarm( extras );

		if ( alarm.isSpeech() ) {
			startSpeech();
		}
	}

	/**
	 * Checks whether or not requirements are met for starting alarm.<br/>
	 *
	 * @param alarmId the ID of the alarm.
	 * @return true if requirements are met.
	 */
	private boolean isRequirementsMet( int alarmId ) {
		SFApplication app = SFApplication.get();

		// Perform location filter.
		GPSFilterRequisitor locationReq = new GPSFilterRequisitor( app.getGPSSet(), app.getPrefs() );
		if ( !locationReq.isSatisfied( app ) )  {
			return false;
		}
		app.releaseGPSSet();

		return true;
	}

	/**
	 * Starts AlarmActivity, extras are passed on.
	 * 
	 * @param extras extras to pass on.
	 */
	private void startAlarm( Bundle extras ) {

		// start vibration.
		if (this.alarm.getAudioConfig().getVibrationEnabled()) {
			VibrationManager.getInstance().startVibrate(context.getApplicationContext());
		}

		// Create intent & re-put extras.
		Intent activityIntent;
		activityIntent = new Intent( context, AlarmActivity.class );
		activityIntent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
		activityIntent.putExtras( extras );

		// Start activity!
		context.startActivity( activityIntent );

		showNotification(activityIntent);
		
		this.startAudio();

		SFApplication.get().setRingingAlarm(alarm);
	}

	private void startAudio() {
		SFApplication app = SFApplication.get();
		AudioDriver driver = app.getAudioDriverFactory().produce(app,
				this.alarm.getAudioSource());
		app.setAudioDriver(driver);

		driver.play(this.alarm.getAudioConfig());
	}

	/**
	 * Launches notification showing that the alarm has gone off.
	 * 
	 * Clicking on it takes the user to AlarmActivity, where a challenge can be
	 * started.
	 * 
	 * @param activityIntent
	 *            the intent to be launched when the notification is clicked
	 */
	private void showNotification(Intent activityIntent) {
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
				activityIntent, 0);

		String name = MetaTextUtils.printAlarmName(context, this.alarm);

		// Localized string the name is inserted into
		String formatTitle = context.getString(R.string.notification_ringing_title);

		String title = String.format(formatTitle, name);
		String message = context
				.getString(R.string.notification_ringing_message);

		NotificationHelper.getInstance().showNotification(context.getApplicationContext(), title, message,
				pendingIntent);
	}

	// read out the time and weather.
	public void doSpeech(String weather) {	
		TextToSpeech tts = SFApplication.get().getTts();
		
		// weren't able to obtain any weather.
		String s;
		if (weather == null) {
			s = new SpeechLocalizer(tts, this.context).getSpeech();
		} else {
			s = new SpeechLocalizer(tts, this.context).getSpeech(weather);
		}

		lowerVolume();
		TextToSpeechUtil.speakAlarm(tts, s);
	}

	private void startSpeech() {
		TextToSpeech tts = SFApplication.get().getTts();
		
		doSpeech(SFApplication.get().getPrefs().getWeather());
		
		// Remove the old weather information 
		SFApplication.get().getPrefs().setWeather(null);
		
		// UtteranceProgressListener not available in api < 15
		
		tts.setOnUtteranceCompletedListener(utteranceListener);
	}

	private void lowerVolume() {
		AudioDriver d = SFApplication.get().getAudioDriver();
		int origVolume = this.alarm.getAudioConfig().getVolume();

		d.setVolume(origVolume/5);
	}

	private void restoreVolume() {
		// gradually restore the volume.		
		Intent serviceIntent = new Intent(context,FadeVolumeService.class);
		serviceIntent.putExtra("alarm_volume", AlarmReceiver.this.alarm.getAudioConfig().getVolume());
		this.context.startService(serviceIntent);
 
	}

	private OnUtteranceCompletedListener utteranceListener = new OnUtteranceCompletedListener() {

		@Override
		public void onUtteranceCompleted(String utteranceId) {
			// now start playing the music now that the speech is over.
			Debug.d("utterance completed.");
			restoreVolume();
		}
	};
}
