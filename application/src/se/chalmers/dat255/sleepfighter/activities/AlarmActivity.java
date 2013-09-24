package se.chalmers.dat255.sleepfighter.activities;

import se.chalmers.dat255.sleepfighter.IntentUtils;
import se.chalmers.dat255.sleepfighter.R;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

/**
 * The activity for when an alarm rings/occurs.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Sep 20, 2013
 */
public class AlarmActivity extends Activity {
	private static final int WINDOW_FLAGS_SCREEN_ON =
			WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
			WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
			WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD;

	private static final int WINDOW_FLAGS_LOCKSCREEN = WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED;

	// TODO move to settings!
	private boolean turnScreenOn = true;
	private boolean bypassLockscreen = true;

	private int alarmId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Turn and/or Keep screen on.
		this.setScreenFlags();

		this.setContentView(R.layout.activity_main);

		// Fetch alarm Id.
		this.alarmId = new IntentUtils( this.getIntent() ).getAlarmId();

		// Do stuff.
		this.work();
	}

	protected void onPause() {
		super.onPause();

		// Release the wake-lock acquired in AlarmReceiver!
		AlarmWakeLocker.release();
	}

	/**
	 * Sets screen related flags, reads from preferences.
	 */
	private void setScreenFlags() {
		int flags = this.computeScreenFlags();

		if ( flags == 0 ) {
			return;
		}

		this.getWindow().addFlags( flags );
	}

	/**
	 * Computes screen flags based on preferences.
	 *
	 * @return screen flags.
	 */
	private int computeScreenFlags() {
		// TODO: actually read from preferences!

		int flags = 0;

		if ( this.turnScreenOn ) {
			flags |= WINDOW_FLAGS_SCREEN_ON;
		}

		if ( this.bypassLockscreen ) {
			flags |= WINDOW_FLAGS_LOCKSCREEN;
		}

		return flags;
	}

	private void work() {
		Log.d( "AlarmActivity", "alarm #id: " + Integer.toString( this.alarmId ) );

		Log.d( "AlarmActivity", "work#1" );
		// TODO: do something useful.
		Toast.makeText(this, "Alarm ringing, get up! Alarm #" + this.alarmId, Toast.LENGTH_LONG).show();
	}
}