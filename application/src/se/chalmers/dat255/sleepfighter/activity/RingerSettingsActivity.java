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

import se.chalmers.dat255.sleepfighter.R;
import se.chalmers.dat255.sleepfighter.SFApplication;
import se.chalmers.dat255.sleepfighter.android.preference.InitializableRingtonePreference;
import se.chalmers.dat255.sleepfighter.android.utils.ActivityUtils;
import se.chalmers.dat255.sleepfighter.android.utils.DialogUtils;
import se.chalmers.dat255.sleepfighter.audio.AudioDriver;
import se.chalmers.dat255.sleepfighter.audio.AudioDriverFactory;
import se.chalmers.dat255.sleepfighter.model.Alarm;
import se.chalmers.dat255.sleepfighter.model.audio.AudioSource;
import se.chalmers.dat255.sleepfighter.model.audio.AudioSourceType;
import se.chalmers.dat255.sleepfighter.utils.MetaTextUtils;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

/**
 * RingerSettingsActivity handles alarm instance settings related to ringing, i.e AudioSource.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Sep 27, 2013
 */
public class RingerSettingsActivity extends PreferenceActivity {
	private final static String TAG = RingerSettingsActivity.class.getSimpleName();

	public enum ID {
		RINGTONE_PICKER( "pref_ringtone_picker" ),
		MUSIC_PICKER( "pref_local_content_uri_picker" ),
		PLAYLIST_PICKER( "pref_playlist_picker" ),
		REMOTE_PICKER( "pref_remote_content_uri_picker" );

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

		// Setup action bar.
		this.setupActionBar();

		// Setup summary.
		this.setupSummary();

		// Setup pickers, etc.
		this.setupRingtonePicker();
		this.setupMusicPicker();
		this.setupPlaylistPicker();
		this.setupRemotePicker();
	}

	@Override
	public boolean onCreateOptionsMenu( Menu menu ) {
		this.getMenuInflater().inflate( R.menu.ringer_menu, menu );
		return true;
	}

	@Override
	public boolean onOptionsItemSelected( MenuItem item ) {
		switch ( item.getItemId() ) {
		case R.id.ringer_action_cancel:
			// Stop any playback
			if(this.driver != null && this.driver.isPlaying()) {
				this.driver.stop();
			}
			this.setAudioSource( null );
			return true;
		case R.id.ringer_action_test:
			this.testRinger();
			return true;
		case android.R.id.home:
			finish();
			return true;
		default:
			return super.onOptionsItemSelected( item );
		}	
	}

	/**
	 * Tests the ringer via set AudioDevice.
	 */
	private void testRinger() {
		this.driver.toggle( this.alarm.getAudioConfig() );
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= 11) {
			ActionBar actionBar = this.getActionBar();
			actionBar.setTitle(MetaTextUtils.printAlarmName(this, alarm));
			ActivityUtils.setupStandardActionBar(this);
		}
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
		String name = this.driver.printSourceName();
		this.summaryName.setText( name );
		setActionBarSubtitle(name);

		// Make and set typeText.
		String typeText;
		AudioSource source = this.driver.getSource();

		Resources res = this.getResources();
		typeText = source == null
				 ? this.getString( R.string.alarm_audiosource_summary_type_none )
				 : res.getStringArray( R.array.alarm_audiosource_summary_type )[source.getType().ordinal()];

		this.summaryType.setText( typeText );
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setActionBarSubtitle(String subtitle) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			ActionBar actionbar = getActionBar();
			actionbar.setSubtitle(subtitle);
		}
	}

	/**
	 * Sets up factory & make driver from current source.
	 */
	private void setupDriver() {
		// Setup factory & make driver from current source.
		AudioSource source = this.alarm.getAudioSource();
		this.factory = SFApplication.get().getAudioDriverFactory();
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
		this.setAudioSource( AudioSourceType.LOCAL_CONTENT_URI, data );
	}

	/**
	 * Sets up the playlist picker.
	 */
	private void setupPlaylistPicker() {
		this.preferenceBind( ID.PLAYLIST_PICKER, new OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick( Preference preference ) {
				launchPlaylistPicker();
				return false;
			}
		} );
	}

	/**
	 * Launches the playlist picker.
	 */
	private void launchPlaylistPicker() {
		Intent intent = new Intent( this, PlaylistSelectActivity.class );

		AudioSource source = this.alarm.getAudioSource();
		if ( source != null && source.getType() == AudioSourceType.PLAYLIST ) {
			intent.putExtra( "selected_uri", source.getUri() );
		}

		this.startActivityForResult( intent, ID.PLAYLIST_PICKER.ordinal() );
	}

	/**
	 * Sets the AudioSource to a playlist.
	 *
	 * @param data the intent data that contains playlist URI.
	 */
	private void setPlaylist( Intent data ) {
		this.setAudioSource( AudioSourceType.PLAYLIST, data );
	}

	/**
	 * Sets up the remote picker.
	 */
	private void setupRemotePicker() {
		this.preferenceBind( ID.REMOTE_PICKER, new OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick( Preference preference ) {
				launchRemotePicker();
				return false;
			}
		} );
	}

	/**
	 * Launches the remote picker.
	 */
	protected void launchRemotePicker() {
		final EditText input = new EditText( this );

		// Set current value if the current source is of type: INTERNET_STREAM.
		AudioSource source = this.alarm.getAudioSource();
		if ( source != null && source.getType() == AudioSourceType.INTERNET_STREAM ) {
			input.setText( source.getUri() );
		}

		// Make a listener for OK.
		DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
			public void onClick( DialogInterface dialog, int whichButton ) {
				String uri = input.getText().toString().trim();
				if ( !uri.isEmpty() ) {
					setRemote( uri );
				}
			}
		};

		// Setup dialog & show.
		AlertDialog.Builder alert = new AlertDialog.Builder( this );
		alert.setTitle( this.getString( R.string.pref_remote_content_uri_picker ) );
		alert.setMessage( this.getString( R.string.pref_remote_content_uri_picker_summary ) );
		alert.setView( input );
		alert.setNegativeButton( android.R.string.cancel, DialogUtils.getNoopClickListener() );
		alert.setPositiveButton( android.R.string.ok, listener );
		alert.show();
	}

	/**
	 * Sets the AudioSource to a remote source given a URI.
	 *
	 * @param uri the URI.
	 */
	private void setRemote( String uri ) {
		this.setAudioSource( new AudioSource( AudioSourceType.INTERNET_STREAM, uri ) );
	}

	/**
	 * Sets & stores the current audio source using an intent for data.
	 *
	 * @param type the type of AudioSource.
	 * @param data the intent data.
	 */
	private void setAudioSource( AudioSourceType type, Intent data ) {
		String uri = data.getDataString();
		AudioSource source = uri == null ? null : new AudioSource( type, uri );
		this.setAudioSource( source );
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
			Log.d( TAG, data.toString() );
	
			ID[] ids = ID.values();
			if ( requestCode < ids.length ) {
				switch( ids[requestCode] ) {
				case MUSIC_PICKER:
					this.setMusic( data );
					break;

				case PLAYLIST_PICKER:
					this.setPlaylist( data );
					break;

				default:
					throw new AssertionError( "Shouldn't happen!" );
				}
			} else {
				super.onActivityResult( requestCode, resultCode, data );
			}
		} else {
			if(ID.MUSIC_PICKER.ordinal() == requestCode) {
				DialogUtils.showMessageDialog(getResources().getString(R.string.install_google_play_music_message), this);
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
		this.alarm = AlarmIntentHelper.fetchAlarmOrPreset( this );

		if (this.alarm == null) {
			// TODO: Better handling
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
	
	@Override
	protected void onPause() {
		// Stop any playback
		if (this.driver != null && this.driver.isPlaying()) {
			this.driver.stop();
		}
		super.onPause();
	}
}
