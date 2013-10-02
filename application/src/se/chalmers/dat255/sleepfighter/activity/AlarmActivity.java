package se.chalmers.dat255.sleepfighter.activity;

import org.joda.time.DateTime;

import se.chalmers.dat255.sleepfighter.R;
import se.chalmers.dat255.sleepfighter.SFApplication;
import se.chalmers.dat255.sleepfighter.audio.VibrationManager;
import se.chalmers.dat255.sleepfighter.challenge.ChallengeType;
import se.chalmers.dat255.sleepfighter.helper.NotificationHelper;
import se.chalmers.dat255.sleepfighter.model.Alarm;
import se.chalmers.dat255.sleepfighter.model.AlarmTimestamp;
import se.chalmers.dat255.sleepfighter.preference.GlobalPreferencesReader;
import se.chalmers.dat255.sleepfighter.service.AlarmPlannerService;
import se.chalmers.dat255.sleepfighter.service.AlarmPlannerService.Command;
import se.chalmers.dat255.sleepfighter.utils.android.AlarmWakeLocker;
import se.chalmers.dat255.sleepfighter.utils.android.IntentUtils;
import se.chalmers.dat255.sleepfighter.utils.debug.Debug;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

/**
 * The activity for when an alarm rings/occurs.
 * 
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Sep 20, 2013
 */
public class AlarmActivity extends Activity {

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

		tvName = (TextView) findViewById(R.id.tvAlarmName);
        tvName.setText(alarm.getName());
        
        tvTime = (TextView) findViewById(R.id.tvAlarmTime);
        tvTime.setText(alarm.getTimeString());
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

	public void alarmString(final AlarmActivity activity) {

		final TextView alarmName = (TextView) activity
				.findViewById(R.id.tvAlarmName);

		alarmName.setText(alarm.getName());

		final TextView alarmTime = (TextView) activity
				.findViewById(R.id.tvAlarmTime);

		alarmTime.setText(alarm.getTimeString());
	}

	public void button(View view) {
		// The vibration stops whenever you start the challenge
		VibrationManager.getInstance().stopVibrate(getApplicationContext());

		Intent i = new Intent(this, ChallengeActivity.class);

		// TODO use property from Alarm
		i.putExtra(ChallengeActivity.BUNDLE_CHALLENGE_TYPE, ChallengeType.TEST);
		
		startActivityForResult(i, CHALLENGE_REQUEST_CODE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// Check if result is from a challenge
		if (requestCode == CHALLENGE_REQUEST_CODE) {
			if (resultCode == Activity.RESULT_OK) {
				Toast.makeText(this, "Challenge completed", Toast.LENGTH_LONG)
						.show();
				Debug.d("done with challenge");

				stopAlarm();
				finish();
			} else {
				Toast.makeText(this, "Returned from uncompleted challenge",
						Toast.LENGTH_LONG).show();
			}

		} else {
			super.onActivityResult(requestCode, resultCode, data);
		}
	}

	public void stopAlarm() {

		this.stopAudio();

		VibrationManager.getInstance().stopVibrate(getApplicationContext());

		// Remove notification saying alarm is ringing
		NotificationHelper.removeNotification(this);

		this.performRescheduling();
	}

	private void stopAudio() {
		SFApplication.get().setAudioDriver(null);
	}
}