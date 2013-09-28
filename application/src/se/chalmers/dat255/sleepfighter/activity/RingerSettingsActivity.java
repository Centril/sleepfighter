package se.chalmers.dat255.sleepfighter.activity;

import se.chalmers.dat255.sleepfighter.R;
import se.chalmers.dat255.sleepfighter.SFApplication;
import se.chalmers.dat255.sleepfighter.audio.AudioDriver;
import se.chalmers.dat255.sleepfighter.audio.AudioDriverFactory;
import se.chalmers.dat255.sleepfighter.model.Alarm;
import se.chalmers.dat255.sleepfighter.model.audio.AudioSource;
import se.chalmers.dat255.sleepfighter.model.audio.AudioSourceType;
import se.chalmers.dat255.sleepfighter.preference.InitializableRingtonePreference;
import se.chalmers.dat255.sleepfighter.utils.android.IntentUtils;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.util.Log;
import android.widget.TextView;
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

	private AudioDriver driver;
	private AudioDriverFactory factory;

	private TextView summaryName;
	private TextView summaryType;

	@SuppressWarnings( "deprecation" )
	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );

		this.fetchAlarm();

		// Setup views.
		addPreferencesFromResource(R.xml.perf_alarm_ringer);
		this.setContentView(R.layout.perf_alarm_ringer_layout);

		// Setup factory & make driver from current source.
		this.setupDriver();

		// Setup summary.
		this.setupSummary();

		// Setup pickers, etc.
		this.setupRingtonePicker();
	}

	/**
	 * Sets up the summary in the top.
	 */
	private void setupSummary() {
		this.summaryName = (TextView) findViewById( R.id.alarm_audiosource_summary_name );
		this.summaryType = (TextView) findViewById( R.id.alarm_audiosource_summary_type );

		this.updateSummary();
	}

	/**
	 * Updates the summary in the top.
	 */
	private void updateSummary() {
		this.summaryName.setText( this.driver.printSourceName() );

		// Make and set typeText.
		String typeText;
		AudioSource source = this.driver.getSource();
		if ( source == null ) {
			typeText = this.getString( R.string.alarm_audiosource_summary_type_none );
		} else {
			Resources res = this.getResources();
			typeText = res.getStringArray( R.array.alarm_audiosource_summary_type )[source.getType().ordinal()];
		}

		this.summaryType.setText( typeText );
	}

	/**
	 * Sets up factory & make driver from current source.
	 */
	private void setupDriver() {
		// Setup factory & make driver from current source.
		AudioSource source = this.alarm.getAudioSource();
		this.factory = new AudioDriverFactory();
		this.driver = this.factory.produce( this, source );
	}

	/**
	 * Sets up the ringtone picker.
	 */
	@SuppressWarnings( "deprecation" )
	private void setupRingtonePicker() {
		InitializableRingtonePreference pref = (InitializableRingtonePreference) this.findPreference( RINGTONE_PICKER );

		AudioSource as = this.driver.getSource();
		if ( as != null ) {
			pref.setInitialUri( Uri.parse( as.getUri() ) );
		}

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
	 * @param uri the URI to set.
	 */
	private void setRingtone( String uri ) {
		AudioSource source = null;

		if ( !uri.equals( "" ) ) {
			source = new AudioSource( AudioSourceType.RINGTONE, uri );
		}

		this.driver = this.factory.produce( this, source );

		this.updateSummary();

		this.alarm.setAudioSource( source );
	}

	/**
	 * Fetch the alarm from list or {@link #finish()} if not found.
	 */
	private void fetchAlarm() {
		SFApplication app = SFApplication.get();

		final int id = new IntentUtils( this.getIntent() ).getAlarmId();
		this.alarm = app.getAlarms().getById(id);

		if (this.alarm == null) {
			// TODO: Better handling for final product
			Toast.makeText(this, "Alarm is null (ID: " + id + ")", Toast.LENGTH_SHORT).show();
			this.finish();
		}

		Log.d( "RingerSettingsActivity", "fetchAlarm, " + this.alarm );
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