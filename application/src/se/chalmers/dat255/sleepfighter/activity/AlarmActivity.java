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
package se.chalmers.dat255.sleepfighter.activity;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import org.joda.time.DateTime;

import se.chalmers.dat255.sleepfighter.R;
import se.chalmers.dat255.sleepfighter.SFApplication;
import se.chalmers.dat255.sleepfighter.audio.VibrationManager;
import se.chalmers.dat255.sleepfighter.helper.NotificationHelper;
import se.chalmers.dat255.sleepfighter.model.Alarm;
import se.chalmers.dat255.sleepfighter.model.AlarmTimestamp;
import se.chalmers.dat255.sleepfighter.preference.GlobalPreferencesReader;
import se.chalmers.dat255.sleepfighter.service.AlarmPlannerService;
import se.chalmers.dat255.sleepfighter.service.AlarmPlannerService.Command;
import se.chalmers.dat255.sleepfighter.speech.TextToSpeechUtil;
import se.chalmers.dat255.sleepfighter.utils.android.AlarmWakeLocker;
import se.chalmers.dat255.sleepfighter.utils.android.IntentUtils;
import se.chalmers.dat255.sleepfighter.utils.debug.Debug;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;

/**
 * The activity for when an alarm rings/occurs.
 * 
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @author Lam(m)<dannylam@gmail.com> / Danny Lam
 * @version 1.0
 * @since Sep 20, 2013
 */
public class AlarmActivity extends Activity implements TextToSpeech.OnInitListener, 
GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener{

	public static final String EXTRA_ALARM_ID = "alarm_id";

	private static final int WINDOW_FLAGS_SCREEN_ON = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
			| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
			| WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD;

	private static final int WINDOW_FLAGS_LOCKSCREEN = WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED;

	public static final int CHALLENGE_REQUEST_CODE = 1;

	private boolean turnScreenOn = true;
	private boolean bypassLockscreen = true;
	private TextView tvName;
	private TextView tvTime;
	private Alarm alarm;
	public Timer timer;
	
    Location currentLocation;
	
	private LocationClient locationClient;

	// To support more languages we use.
	// http://stackoverflow.com/questions/15691031/android-text-to-speech-not-working-w-japanese
	// used to read out. 
	private TextToSpeech tts;
	
	
	class WeatherDataTask extends AsyncTask<Double, Void, WeatherUtil> {

	    protected void onPostExecute(WeatherUtil weather) {
	    	Debug.d("done loading url");
	    	doSpeech(weather);
	    }

		@Override
		protected WeatherUtil doInBackground(Double... params) {
			return new WeatherUtil(params[0], params[1]);
		}
	}
	
	// read out the time and weather.
	public void doSpeech(WeatherUtil weather) {
		// get time.
		String time = TextToSpeechUtil.getCurrentTime();
	
		String s = weather.getSummary();
		Debug.d("Simmary: " + s);
		
		tts.speak("Hello, master, it's time to wake up. The time is: " + time + " and the weather is " + s, TextToSpeech.QUEUE_FLUSH, null);
	}
	
	// called when tts has been fully initialized. 
	@Override
	public void onInit(int status) {
		Debug.d("done initi tts");
		
		// Configure tts 
		TextToSpeechUtil.setBestLanguage(tts, this);
		TextToSpeechUtil.config(tts);
		
		// fetch the json weather data. 
		new WeatherDataTask().execute(currentLocation.getLatitude(), currentLocation.getLongitude());
	}
	
	static Thread thread;
	
	//cb8a0d4b48c35b562d1b427b3f77552d
	
	
	
	/**
	 * Sets up google play services. We need this to get the current location. 
	 */
	private void setupGooglePlay() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());
		if ( status != ConnectionResult.SUCCESS ) {
			// Google Play Services are not available.
			int requestCode = 10;
			GooglePlayServicesUtil.getErrorDialog( status, this, requestCode ).show();
		} else {
			Debug.d("google maps is setup");
			// google map is availabel 
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
				
		
		
		
		// Turn and/or Keep screen on.
		this.setScreenFlags();

		this.setContentView(R.layout.activity_alarm_prechallenge);

		SFApplication app = SFApplication.get();

		// Fetch alarm Id.
		int alarmId = new IntentUtils(this.getIntent()).getAlarmId();
		this.alarm = app.getPersister().fetchAlarmById(alarmId);
		
		if(alarm.isSpeech()) {
			setupGooglePlay();
			locationClient = new LocationClient(this, this, this);
			TextToSpeechUtil.checkTextToSpeech(this);
		}

		// Get the name and time of the current ringing alarm
		tvName = (TextView) findViewById(R.id.tvAlarmName);
		tvName.setText(alarm.getName());

		tvTime = (TextView) findViewById(R.id.tvAlarmTime);

		Button btnChallenge = (Button) findViewById(R.id.btnChallenge);
		btnChallenge.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onStopClick();
			}
		});

		Button btnSnooze = (Button) findViewById(R.id.btnSnooze);
		if(alarm.getSnoozeConfig().isSnoozeEnabled()) {
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

	private void onStopClick() {
		boolean challengeEnabled = this.alarm.getChallengeSet().isEnabled();
		if (challengeEnabled) {
			startChallenge();
		} else {
			stopAlarm();
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
		new IntentUtils( i ).setAlarmId( this.alarm );
		startActivityForResult(i, CHALLENGE_REQUEST_CODE);
	}

	/**
	 * Stops alarm temporarily and sends a snooze command to the server.
	 */
	private void startSnooze() {
		// Should the user complete a challenge before snoozing?

		stopAudio();

		VibrationManager.getInstance().stopVibrate(getApplicationContext());

		// Remove notification saying alarm is ringing
		NotificationHelper.removeNotification(this);

		// Send snooze command to service
		AlarmPlannerService.call(this, Command.SNOOZE, alarm.getId());
		finish();
	}

	protected void onPause() {
		super.onPause();

		// Release the wake-lock acquired in AlarmReceiver!
		AlarmWakeLocker.release();
	}

	private void performRescheduling() {
		SFApplication app = SFApplication.get();

		// Disable alarm if not repeating.
		if (!this.alarm.isRepeating()) {
			if (this.alarm.getMessageBus() == null) {
				this.alarm.setMessageBus(app.getBus());
			}

			this.alarm.setActivated(false);
		} else {
			// Reschedule earliest alarm (if any).
			AlarmTimestamp at = app.getAlarms().getEarliestAlarm(
					new DateTime().getMillis());
			if (at != AlarmTimestamp.INVALID) {
				AlarmPlannerService.call(app, Command.CREATE, at.getAlarm()
						.getId());
			}
		}
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
		GlobalPreferencesReader prefs = SFApplication.get().getPrefs();

		this.turnScreenOn = prefs.turnScreenOn();
		this.bypassLockscreen = prefs.bypassLockscreen();
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
				Toast.makeText(this, "Challenge completed", Toast.LENGTH_LONG)
						.show();
				Debug.d("done with challenge");

				// If completed, stop the alarm
				stopAlarm();
			} else {
				Toast.makeText(this, "Returned from uncompleted challenge",
						Toast.LENGTH_LONG).show();
			}

		} else if(requestCode == TextToSpeechUtil.CHECK_TTS_DATA_REQUEST_CODE) {   
			tts = new TextToSpeech(this, this);
		}else {
			super.onActivityResult(requestCode, resultCode, data);
		}
	}

	/**
	 * Stop the current alarm sound and vibration
	 */
	public void stopAlarm() {
		this.stopAudio();

		VibrationManager.getInstance().stopVibrate(getApplicationContext());

		// Remove notification saying alarm is ringing
		NotificationHelper.removeNotification(this);

		this.performRescheduling();
		finish();
	}

	private void stopAudio() {
		SFApplication.get().setAudioDriver(null);
	}

	@Override
	protected void onStart() {
		super.onStart();
	
		// Connect the client.
        if(alarm.isSpeech())
        	locationClient.connect();

		
		timer = new Timer("SFTimer");
		Calendar calendar = Calendar.getInstance();

		// Get the current time
		final Runnable updateTask = new Runnable() {
			public void run() {
				// Set the current time on the text view
				tvTime.setText(getCurrentTime());
			}
		};

		// Update the user interface
		int msec = 999 - calendar.get(Calendar.MILLISECOND);
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				runOnUiThread(updateTask);
			}
		}, msec, 1000);
		
	}

	/*
	 * Use to stop the timer
	 * (non-Javadoc)
	 * @see android.app.Activity#onStop()
	 */
	@Override
	protected void onStop() {
		super.onStop();
		
		if(alarm.isSpeech())
			locationClient.disconnect();

		timer.cancel();
		timer.purge();
		timer = null;
	}

	// Get the current time with the Calendar
	@SuppressLint("DefaultLocale")
	public String getCurrentTime() {
		Calendar cal = Calendar.getInstance();
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int minute = cal.get(Calendar.MINUTE);
		return String.format("%02d:%02d", hour, minute);
	}
	
	   /*
     * Called by Location Services when the request to connect the
     * client finishes successfully. At this point, you can
     * request the current location or start periodic updates
     */
    @Override
    public void onConnected(Bundle dataBundle) {
        // Display the connection status
        Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();

        currentLocation = locationClient.getLastLocation();
	    Debug.d("current location: " + currentLocation);
    }
    
    /*
     * Called by Location Services if the connection to the
     * location client drops because of an error.
     */
    @Override
    public void onDisconnected() {
        // Display the connection status
        Toast.makeText(this, "Disconnected. Please re-connect.",
                Toast.LENGTH_SHORT).show();
    }
    
    /*
     * Called by Location Services if the attempt to
     * Location Services fails.
     */
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        /*
         * Google Play services can resolve some errors it detects.
         * If the error has a resolution, try sending an Intent to
         * start a Google Play services activity that can resolve
         * error.
         */
        if (connectionResult.hasResolution()) {
    
        	Toast.makeText(this, "Connection failed we are dead.", Toast.LENGTH_SHORT).show();
            	
        } else {
            /*
             * If no resolution is available, display a dialog to the
             * user with the error.
             */
            Toast.makeText(this, "Connection failed with no resolution. Error code: " + connectionResult.getErrorCode(),
                    Toast.LENGTH_SHORT).show();
        }
    }

}
