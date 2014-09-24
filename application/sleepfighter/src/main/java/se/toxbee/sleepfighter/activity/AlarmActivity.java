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
package se.toxbee.sleepfighter.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.TextView;

import org.joda.time.DateTime;

import java.util.Timer;
import java.util.TimerTask;

import se.toxbee.commons.string.StringUtils;
import se.toxbee.sleepfighter.R;
import se.toxbee.sleepfighter.android.power.WakeLocker;
import se.toxbee.sleepfighter.android.utils.DialogUtils;
import se.toxbee.sleepfighter.app.SFApplication;
import se.toxbee.sleepfighter.audio.VibrationManager;
import se.toxbee.sleepfighter.helper.AlarmIntentHelper;
import se.toxbee.sleepfighter.helper.NotificationHelper;
import se.toxbee.sleepfighter.model.Alarm;
import se.toxbee.sleepfighter.model.challenge.ChallengeConfigSet;
import se.toxbee.sleepfighter.preference.AlarmControlPreferences;
import se.toxbee.sleepfighter.preference.ChallengeGlobalPreferences;
import se.toxbee.sleepfighter.service.AlarmPlannerService;
import se.toxbee.sleepfighter.service.AlarmPlannerService.Command;

/**
 * The activity for when an alarm rings/occurs.
 * 
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @author Lam(m)<dannylam@gmail.com> / Danny Lam
 * @version 1.0
 * @since Sep 20, 2013
 */
public class AlarmActivity extends Activity {
	private static final String TAG = AlarmActivity.class.getSimpleName();

	public static final String EXTRA_ALARM_ID = "alarm_id";

	public static final int CHALLENGE_REQUEST_CODE = 1;

	private static final int WINDOW_FLAGS_LOCKSCREEN = WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED;
	private static final int WINDOW_FLAGS_SCREEN_ON = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
			| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
			| WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD;

	private Parameters p;
	private TextView tvName, tvTime;
	private Button btnStop, btnSnooze;
	private Alarm alarm;
	private Timer timer;
	private Camera camera;
	private boolean turnScreenOn = true;
	private boolean bypassLockscreen = true;

	/**
	 * Enforces that no alarm is currently ringing,<br/>
	 * If one is, AlarmActivity is immediately started<br/>
	 * and the passed activity is finished.
	 *
	 * @param activity the activity to finish if needed.
	 */
	public static void startIfRinging( Activity activity ) {
		SFApplication app = SFApplication.get();

		// Check if an alarm is ringing, if so, send the user to AlarmActivity
		Alarm ringingAlarm = app.getRingingAlarm();
		if ( ringingAlarm != null ) {
			Intent i = new Intent( app, AlarmActivity.class );
			activity.startActivity( new AlarmIntentHelper( i ).setAlarmId( ringingAlarm ).intent() );
			activity.finish();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Turn and/or keep screen on.
		this.setScreenFlags();
		this.setContentView(R.layout.activity_alarm);

		// Fetch alarm.
		this.fetchAlarm();

		// Get the name and time of the current ringing alarm
		tvName = (TextView) findViewById(R.id.tvAlarmName);
		tvName.setText( alarm.printName() );
		tvTime = (TextView) findViewById(R.id.tvAlarmTime);

		setupStopButton();
		setupSnoozeButton();
		setupFooter();
	}

	private void fetchAlarm() {
		// Fetch it.
		int alarmId = new AlarmIntentHelper( this.getIntent() ).getAlarmId();
		this.alarm = SFApplication.get().getPersister().fetchAlarmById( alarmId );

		// Init the planner.
		AlarmPlannerService.register();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.alarm_activity_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_emergency_stop:
			handleEmergencyStop();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void setupStopButton() {
		// Connect the challenge button with XML
		btnStop = (Button) findViewById(R.id.btnStop);
		btnStop.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onStopClick();
			}
		});
	}

	private void setupSnoozeButton() {
		btnSnooze = (Button) findViewById(R.id.btnSnooze);

		if (alarm.getSnoozeConfig().isSnoozeEnabled()) {
			btnSnooze.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					startSnooze();
				}
			});
		} else {
			btnSnooze.setVisibility(View.GONE);
		}
	}

	private ChallengeGlobalPreferences cprefs() {
		return SFApplication.get().getPrefs().challenge;
	}

	private void setupFooter() {
		boolean usingChallenge = useChallenges();
		if (usingChallenge) {
			TextView pointText = (TextView) findViewById( R.id.challenge_points_text );

			int p = cprefs().getChallengePoints();
			pointText.setText( p + " " + this.getString( R.string.challenge_points ) );
		} else {
			findViewById(R.id.footer).setVisibility(View.INVISIBLE);
		}
	}

	/**
	 * Handle what happens when the user presses the emergency stop.
	 */
	private void handleEmergencyStop() {
		boolean skippingChallenges = useChallenges();

		if (skippingChallenges) {
			skipChallengeConfirm();
		} else {
			stopAlarm();
			performRescheduling();
		}
	}

	/**
	 * Handles if the user uses emergency stop so that a challenge would be
	 * skipped by showing confirmation dialog.
	 */
	private void skipChallengeConfirm() {
		int emergencyCost = cprefs().calcEmergencyCost();

		// Show confirmation dialog where the user has to confirm skipping the
		// challenge, and in turn lose a lot of points
		DialogInterface.OnClickListener yesAction = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				cprefs().withdrawEmergencyCost();
				stopAlarm();
				performRescheduling();
			}
		};
		Resources res = getResources();

		// Get the correct string with the correct value inserted.
		DialogUtils.showConfirmationDialog(String.format(res
				.getString(R.string.alarm_emergency_dialog), res
				.getQuantityString( R.plurals.alarm_emergency_cost, emergencyCost, emergencyCost)), this, yesAction);

	}

	private void onStopClick() {
		boolean showChallenge = useChallenges();

		if (showChallenge) {
			startChallenge();
		} else {
			stopAlarm();
			performRescheduling();
		}
	}

	/**
	 * Launch ChallengeActivity to start alarm.
	 */
	private void startChallenge() {
		// The vibration stops whenever you start the challenge
		VibrationManager.getInstance().stopVibrate(getApplicationContext());

		// Send user to ChallengeActivity.
		Intent i = new Intent(this, ChallengeActivity.class);
		new AlarmIntentHelper(i).setAlarmId(this.alarm);
		startActivityForResult(i, CHALLENGE_REQUEST_CODE);
	}

	/**
	 * Stops alarm temporarily and sends a snooze command to the server.
	 */
	private void startSnooze() {
		stopAlarm();

		// Send snooze command to service
		AlarmPlannerService.call(this, Command.SNOOZE, alarm.getId());

		// Remove some challenge points if skipping challenge
		boolean skippingChallenge = useChallenges();
		if ( skippingChallenge ) {
			cprefs().withdrawSnoozeCost();
		}
	}

	protected void onPause() {
		super.onPause();

		// Release the wake-lock acquired in AlarmReceiver!
		WakeLocker.release();
	}

	private void performRescheduling() {
		// Debug mode might have issued an inactive alarm, don't need to re-issue it.
		if ( !this.alarm.isActivated() ) {
			return;
		}

		// Make sure alarm has a bus.
		if ( this.alarm.getMessageBus() == null ) {
			this.alarm.setMessageBus( SFApplication.get().getBus() );
		}

		// Tell the alarm it has been issued, rescheduling if necessary.
		this.alarm.issued();
	}

	/**
	 * Sets screen related flags, reads from preferences.
	 */
	private void setScreenFlags() {
		int flags = this.computeScreenFlags();

		if (flags == 0) {
			return;
		}

		this.getWindow().addFlags(flags);
	}

	private void readPreferences() {
		AlarmControlPreferences acp = SFApplication.get().getPrefs().alarmControl;

		this.turnScreenOn = acp.turnScreenOn();
		this.bypassLockscreen = acp.bypassLockscreen();
	}

	/**
	 * Computes screen flags based on preferences.
	 * 
	 * @return screen flags.
	 */
	private int computeScreenFlags() {
		readPreferences();

		int flags = 0;

		if (this.turnScreenOn) {
			flags |= WINDOW_FLAGS_SCREEN_ON;
		}

		if (this.bypassLockscreen) {
			flags |= WINDOW_FLAGS_LOCKSCREEN;
		}

		return flags;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// Check if result is from a challenge
		if (requestCode == CHALLENGE_REQUEST_CODE) {
			if (resultCode == Activity.RESULT_OK) {
				Log.d( TAG, "done with challenge" );

				// If completed, stop the alarm
				stopAlarm();
				performRescheduling();
				
				// Add points
				cprefs().earnCompleted();
			}
		}  else {
			super.onActivityResult(requestCode, resultCode, data);
		}
	}

	/**
	 * Stop the current alarm sound and vibration
	 */
	public void stopAlarm() {
		stopAudio();
		SFApplication.get().getTts().stop();
		
		VibrationManager.getInstance().stopVibrate(getApplicationContext());

		// Remove notification saying alarm is ringing
		NotificationHelper.getInstance().removeNotification(
				getApplicationContext());

		// Removes the globally set ringing alarm
		SFApplication.get().setRingingAlarm(null);

		finish();
	}

	private void stopAudio() {
		SFApplication.get().setAudioDriver(null);

		TextToSpeech tts = SFApplication.get().getTts();
		if (tts != null) {
			tts.stop();
		}
	}

	/**
	 * Start flash, animation and show the current time on display
	 */
	@Override
	protected void onStart() {
		super.onStart();

		// Start animation and flash
		startAnimate();
		
		if(alarm.isFlashEnabled()){
			startFlash();
		}
		
		// Cancel previously started timers
		cancelTimer();
		
		timer = new Timer("SFTimer");

		final Runnable updateTask = new Runnable() {
			public void run() {
				// Set the current time on the text view
				tvTime.setText(getCurrentTime());
			}
		};

		// Update the user interface
		TimerTask timerTask = new TimerTask() {
			@Override
			public void run() {
				runOnUiThread(updateTask);
			}
		};

		DateTime dt = DateTime.now();
		timer.scheduleAtFixedRate(timerTask,
				dt.getMillisOfSecond(), 1000);
	}

	private void cancelTimer() {
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
	}

	/**
	 * Checks various settings to determine if a challenge should be shown when
	 * trying to stop.
	 * 
	 * @return true if a challenge should be shown
	 */
	private boolean useChallenges() {
		// True if in order: enabled for alarm, globally enabled, any enabled individual challenge for alarm.
		ChallengeConfigSet c = this.alarm.getChallengeSet();
		return c.isEnabled() && cprefs().isActivated() && !c.getEnabledTypes().isEmpty();
	}

	/*
	 * Use to stop the timer (non-Javadoc)
	 * 
	 * @see android.app.Activity#onStop()
	 */
	@Override
	protected void onStop() {
		super.onStop();

		if (camera != null) {
			camera.release();
		}

		cancelTimer();
	}

	// Get the current time with the Calendar
	public String getCurrentTime() {
		DateTime time = new DateTime();
		return StringUtils.joinTime( time.getHourOfDay(), time.getMinuteOfHour() );
	}

	/**
	 * Start the camera's flashlight if found
	 */
	private void startFlash() {

		// Check if there is any camera. If not found, return nothing.
		// If found, flash!
		Context context = this;
		PackageManager pm = context.getPackageManager();

		if (!pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
			Log.e("err", "No flashlight detected!");
			return;
		}else{
			camera = Camera.open();
			Log.i("info", "The flashlight is on.");
			p = camera.getParameters();
			p.setFlashMode(Parameters.FLASH_MODE_TORCH);
			camera.setParameters(p);
		}
	}

	/**
	 * Declaring new animations and sets to components
	 */
	private void startAnimate() {

		// Setting animation

		Animation fadeShort = new AlphaAnimation(1, 0);
		fadeShort.setDuration(200);
		fadeShort.setInterpolator(new LinearInterpolator());
		fadeShort.setRepeatCount(Animation.INFINITE);
		fadeShort.setRepeatMode(Animation.REVERSE);

		// Set the components with animation
		tvTime.startAnimation(fadeShort);
	}
}