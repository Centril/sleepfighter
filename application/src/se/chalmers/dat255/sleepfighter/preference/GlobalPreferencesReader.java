package se.chalmers.dat255.sleepfighter.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * GlobalPreferencesReader provides a layer of abstraction above SharedPreferences.<br/>
 * It is more direct and knows about the global options in the application.<br/>
 * This also allows for one central location to edit strings, default values, etc.
 * 
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Sep 29, 2013
 */
public class GlobalPreferencesReader {
	private SharedPreferences prefs;

	/**
	 * Constructs a GlobalPreferencesReader given the application context.
	 * 
	 * @param context application android context.
	 */
	public GlobalPreferencesReader( Context context ) {
		this.prefs = PreferenceManager.getDefaultSharedPreferences( context );
	}

	/**
	 * Returns whether or not to turn the screen on when an Alarm rings.<br/>
	 * Default is true.
	 *
	 * @return true if the screen should be turned on.
	 */
	public boolean turnScreenOn() {
		return this.prefs.getBoolean( "pref_alarm_turn_screen_on", true );
	}

	/**
	 * Returns whether or not to bypass the lockscreen when an Alarm rings.<br/>
	 * Default is true.
	 *
	 * @return true if the lockscreen should be bypassed.
	 */
	public boolean bypassLockscreen() {
		return this.prefs.getBoolean( "pref_alarm_bypass_lock_screen", true );
	}

	/**
	 * Returns whether or not to show the earliest Alarm as a period or as exact time.<br/>
	 * Default is false.
	 *
	 * @return true if a period should be displayed instead of exact time.
	 */
	public boolean displayPeriodOrTime() {
		return this.prefs.getBoolean( "pref_global_when_or_in_how_much", false );
	}
}