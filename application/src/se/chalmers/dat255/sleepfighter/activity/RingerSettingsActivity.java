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
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.provider.MediaStore;
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
	public enum ID {
		RINGTONE_PICKER( "pref_ringtone_picker" ),
		MUSIC_PICKER( "pref_local_content_uri_picker" ),
		PLAYLIST_PICKER( "pref_playlist_picker" );

		public final String id;

		ID( String id ) {
			this.id = id;
		}
	}

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
		this.setupMusicPicker();
		this.setupPlaylistPicker();
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
		InitializableRingtonePreference pref = (InitializableRingtonePreference) this.findPreference( ID.RINGTONE_PICKER.id );

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
		AudioSource source = uri.equals( "" ) ? null : new AudioSource( AudioSourceType.RINGTONE, uri );
		this.setAudioSource( source );
	}

	/**
	 * Sets up the music picker.
	 */
	private void setupMusicPicker() {
		this.preferenceBind( ID.MUSIC_PICKER, new OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick( Preference preference ) {
				launchMusicPicker();
				return false;
			}
		} );
	}

	/**
	 * Launches the music picker.
	 */
	private void launchMusicPicker() {
		Log.d( this.getClass().getSimpleName(), "launchMusicPicker#1" );
		Intent i = new Intent( Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI );
		this.startActivityForResult( i, ID.MUSIC_PICKER.ordinal() );
	}

	/**
	 * Sets the AudioSource to a music file.
	 *
	 * @param data the intent data that contains music URI.
	 */
	private void setMusic( Intent data ) {
		Log.d( this.getClass().getSimpleName(), data.toString() );

		String uri = data.getDataString();
		AudioSource source = uri == null ? null : new AudioSource( AudioSourceType.LOCAL_CONTENT_URI, uri );
		this.setAudioSource( source );
	}

	private void setupPlaylistPicker() {
		this.preferenceBind( ID.PLAYLIST_PICKER, new OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick( Preference preference ) {
				launchPlaylistPicker();
				return false;
			}
		} );
	}

	protected void launchPlaylistPicker() {
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setType(MediaStore.Audio.Playlists.CONTENT_TYPE); 
		intent.putExtra("oneshot", false);
		this.startActivityForResult( intent, ID.PLAYLIST_PICKER.ordinal() );
	}

	private void setPlaylist( Intent data ) {
		Log.d( this.getClass().getSimpleName(), data.toString() );
	}

	/**
	 * Sets & stores the current audio source.
	 *
	 * @param source the audio source.
	 */
	private void setAudioSource( AudioSource source ) {
		this.driver = this.factory.produce( this, source );
		this.alarm.setAudioSource( source );
		this.updateSummary();
	}

	@Override
	protected void onActivityResult( int requestCode, int resultCode, Intent data ) {
		if ( resultCode == Activity.RESULT_OK ) {
			ID[] ids = ID.values();
			if ( requestCode < ids.length ) {
				switch( ID.values()[requestCode] ) {
				case MUSIC_PICKER:
					this.setMusic( data );
					break;

				case PLAYLIST_PICKER:
					this.setPlaylist( data );

				default:
					throw new AssertionError( "Shouldn't happen!" );
				}
			} else {
				super.onActivityResult( requestCode, resultCode, data );
			}
		}
	}

	@SuppressWarnings( "deprecation" )
	private void preferenceBind( ID id, OnPreferenceClickListener listener ) {
		Preference pref = (Preference) this.findPreference( id.id );
		pref.setOnPreferenceClickListener( listener );
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