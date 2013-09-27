package se.chalmers.dat255.sleepfighter.activities;

import se.chalmers.dat255.sleepfighter.IntentUtils;
import se.chalmers.dat255.sleepfighter.R;
import se.chalmers.dat255.sleepfighter.SFApplication;
import se.chalmers.dat255.sleepfighter.model.Alarm;
import se.chalmers.dat255.sleepfighter.model.audio.AudioSource;
import se.chalmers.dat255.sleepfighter.model.audio.AudioSourceType;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.RingtonePreference;
import android.widget.Toast;

/**
 * RingerSettingsActivity handles alarm instance settings related to ringing, i.e AudioSource.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Sep 27, 2013
 */
public class RingerSettingsActivity extends PreferenceActivity {
	private static final CharSequence RINGTONE_PICKER = "pref_ringtone_picker";

	private Alarm alarm;

	@SuppressWarnings( "deprecation" )
	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );

		this.fetchAlarm();

		// Setup views.
		addPreferencesFromResource(R.xml.perf_alarm_ringer);
		this.setContentView(R.layout.perf_alarm_ringer_layout);

		// Setup pickers, etc.
		this.setupRingtonePicker();
	}

	@SuppressWarnings( "deprecation" )
	private void setupRingtonePicker() {
		RingtonePreference pref = (RingtonePreference) this.findPreference( RINGTONE_PICKER );
		pref.setOnPreferenceChangeListener( new OnPreferenceChangeListener() {
			@Override
			public boolean onPreferenceChange( Preference preference, Object newValue ) {
				setRingtone( (String) newValue );
				return true;
			}
		} );
	}

	/**
	 * Sets the AudioSource to a ringtone.
	 *
	 * @param uri 
	 */

	protected void setRingtone( String uri ) {
		AudioSource source = null;

		if ( !uri.equals( "" ) ) {
			source = new AudioSource( AudioSourceType.RINGTONE, uri );
		}

		// TODO save source somewhere.
	}


	/**
	 * Fetch the alarm from list or {@link #finish()} if not found.
	 */
	private void fetchAlarm() {
		SFApplication app = SFApplication.get();

		final int id = new IntentUtils( this.getIntent() ).getAlarmId();
		this.alarm = app.getAlarms().getById(id);

		if (alarm == null) {
			// TODO: Better handling for final product
			Toast.makeText(this, "Alarm is null (ID: " + id + ")", Toast.LENGTH_SHORT).show();
			this.finish();
		}
	}

	/**
	 * Returns the alarm, provided only for components to reach.
	 *
	 * @return the alarm.
	 */
	public Alarm getAlarm() {
		return this.alarm;
	}
}