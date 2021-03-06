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

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.speech.tts.TextToSpeech;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import net.engio.mbassy.listener.Handler;

import java.util.Locale;

import se.toxbee.sleepfighter.R;
import se.toxbee.sleepfighter.android.component.secondpicker.SecondTimePicker;
import se.toxbee.sleepfighter.android.component.secondpicker.SecondTimePickerDialog;
import se.toxbee.sleepfighter.android.preference.EnablePlusSettingsPreference;
import se.toxbee.sleepfighter.android.preference.MultiSelectListPreference;
import se.toxbee.sleepfighter.android.preference.NumberPickerDialogPreference;
import se.toxbee.sleepfighter.android.preference.TimepickerPreference;
import se.toxbee.sleepfighter.android.preference.VolumePreference;
import se.toxbee.sleepfighter.android.utils.DialogUtils;
import se.toxbee.sleepfighter.app.SFApplication;
import se.toxbee.sleepfighter.audio.AudioDriver;
import se.toxbee.sleepfighter.audio.factory.AudioDriverFactory;
import se.toxbee.sleepfighter.helper.AlarmIntentHelper;
import se.toxbee.sleepfighter.helper.AlarmTimeRefresher;
import se.toxbee.sleepfighter.helper.AlarmTimeRefresher.RefreshedEvent;
import se.toxbee.sleepfighter.model.Alarm;
import se.toxbee.sleepfighter.model.Alarm.AudioChangeEvent;
import se.toxbee.sleepfighter.model.Alarm.Field;
import se.toxbee.sleepfighter.model.Alarm.MetaChangeEvent;
import se.toxbee.sleepfighter.model.AlarmList;
import se.toxbee.sleepfighter.model.time.AlarmTime;
import se.toxbee.sleepfighter.model.time.CountdownTime;
import se.toxbee.sleepfighter.model.time.ExactTime;
import se.toxbee.sleepfighter.speech.SpeechLocalizer;
import se.toxbee.sleepfighter.speech.TextToSpeechUtil;
import se.toxbee.sleepfighter.text.DateTextUtils;
import se.toxbee.sleepfighter.utils.debug.Debug;

/**
 * Contains preferences for specific alarms.
 * 
 * @author Hassel
 */
public class AlarmSettingsActivity extends PreferenceActivity {
	public static final String EXTRA_ALARM_IS_NEW = "alarm_is_new";

	private static final String NAME = "pref_alarm_name";
	private static final String TIME = "pref_alarm_time";
	private static final String DAYS = "pref_enabled_days";
	private static final String REPEAT = "pref_alarm_repeat";
	private static final String DELETE = "pref_delete_alarm";
	private static final String VIBRATION = "pref_alarm_vibration";	
	private static final String RINGER_SUBSCREEN = "perf_alarm_ringtone";
	private static final String CHALLENGE = "pref_challenge";
	private static final String VOLUME = "pref_volume";
	private static final String ENABLE_SNOOZE = "pref_alarm_snooze_enabled";
	private static final String SNOOZE_TIME = "pref_alarm_snooze_time";
	
	private static final String SPEECH = "pref_alarm_speech";
	private static final String SPEECH_SAMPLE = "pref_speech_sample";
	
	private static final String FLASH = "pref_alarm_flash_enabled";
	
	private Preference ringerPreference;

	private Alarm alarm;
	private AlarmList alarmList;

	private SFApplication app() {
		return SFApplication.get();
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= 11) {
			ActionBar actionBar = getActionBar();
		    // add the custom view to the action bar
		    actionBar.setCustomView(R.layout.alarm_settings_actionbar);

		    View customView = actionBar.getCustomView();

		    // Setup name field.
		    getActionBar().getCustomView().findViewById(R.id.global_alarm_hidden_title).setVisibility(View.INVISIBLE);
		    EditText edit_title_field = (EditText) customView.findViewById(R.id.alarm_edit_title_field);
		    edit_title_field.setText( alarm.printName() );
		    edit_title_field.setOnEditorActionListener(new OnEditorActionListener() {
				@Override
				public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
					alarm.setName(v.getText().toString());
					InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
					return false;
				}
		    });
		    edit_title_field.clearFocus();
			actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_CUSTOM);

			// Remove the Name preference... no need for duplicate, just looks ugly.
			this.removeEditName();

			
			// Setup activated switch.
			CompoundButton activatedSwitch = (CompoundButton) customView.findViewById( R.id.alarm_actionbar_toggle );
			activatedSwitch.setChecked( this.alarm.isActivated() );
			activatedSwitch.setOnCheckedChangeListener( new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged( CompoundButton buttonView, boolean isChecked ) {
					alarm.setActivated( isChecked );
				}
			} );
		}
	}

	@Override
	protected void onResume() {
		super.onResume();

		AlarmActivity.startIfRinging( this );

		this.initRefresher();
	}

	@Override
	protected void onPause() {
		super.onPause();

		this.clearRefresher();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		this.clearRefresher();
		SFApplication.get().getTts().stop();
	}

	private AlarmTimeRefresher refresher;
	private void initRefresher() {
		this.refresher = new AlarmTimeRefresher( this.alarmList );
		this.refresher.start();
	}
	private void clearRefresher() {
		if ( this.refresher != null ) {
			this.refresher.stop();
			this.refresher = null;
		}
	}

	@Override
	public boolean onCreateOptionsMenu( Menu menu ) {
		this.getMenuInflater().inflate(R.menu.alarm_settings_menu, menu);
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// If this is the preset alarm, the options menu won't be shown, only
		// removing the "delete" menu might be appropriate if more options are
		// added
		if(this.alarm.isPresetAlarm()) {
			return false;
		}
		return true;
	}

	/**
	 * Handles a refresh event.
	 *
	 * @param evt the event.
	 */
	@Handler
	public void handleRefreshed( RefreshedEvent evt ) {
		this.runOnUiThread( new Runnable() {
			@Override
			public void run() {
				updateTimeSummary();
			}
		} );
	}

	private void updateTimeSummary() {
		AlarmTime t = alarm.getTime();
		boolean countdown = t instanceof CountdownTime;
		findPreference( TIME ).setSummary( (countdown ? "in " : "") + t.getTimeString( !countdown ) );
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Handler
	public void handleNameChange(MetaChangeEvent e) {
		if (e.getModifiedField() == Field.NAME) {
			String name = alarm.printName();
			Preference namePref = findPreference(NAME);

			// null if preference removed due to API level
			if (namePref != null) {
				namePref.setSummary(name);
			}

			if (Build.VERSION.SDK_INT >= 11) {
				((EditText)this.getActionBar().getCustomView().findViewById(R.id.alarm_edit_title_field)).setText(name);
			}
		}
	}

	@Handler
	public void handleRingerChange(AudioChangeEvent e) {
		if (e.getModifiedField() == Field.AUDIO_SOURCE) {
			updateRingerSummary();
		}
	}

	private void removeFlashLightPref() {
		Preference pref = (Preference) findPreference(FLASH);
		PreferenceCategory category = (PreferenceCategory) findPreference("pref_category_misc");
		category.removePreference(pref);
	}

	private void removeMiscCategoryIfEmpty() {
		PreferenceCategory category = (PreferenceCategory) findPreference("pref_category_misc");
		
		if(category.getPreferenceCount() == 0) {
			PreferenceScreen preferenceScreen = (PreferenceScreen) findPreference("alarm_preference_screen");
			preferenceScreen.removePreference(category);
		}
	}

	private void removeDeleteButton() {
		Preference pref = (Preference) findPreference(DELETE);
		PreferenceCategory category = (PreferenceCategory) findPreference("pref_category_misc");
		category.removePreference(pref);		
	}

	private void removeEditName() {
		this.removeDecendantOfScreen( findPreference(NAME) );
	}

	@SuppressWarnings( "deprecation" )
	private void removeDecendantOfScreen( Preference pref ) {
		if ( pref != null ) {
			this.getPreferenceScreen().removePreference( pref );
		}
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void removeEditTitle() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().getCustomView().findViewById(R.id.alarm_edit_title_field).setVisibility(View.INVISIBLE);
			
			getActionBar().getCustomView().findViewById(R.id.global_alarm_hidden_title).setVisibility(View.VISIBLE);
		}
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void removeAlarmToggle() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().getCustomView().findViewById(R.id.alarm_actionbar_toggle).setVisibility(View.INVISIBLE);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		AlarmActivity.startIfRinging( this );
				
		TextToSpeechUtil.checkTextToSpeech(this);
		
		alarmList = app().getAlarms();
		app().getBus().subscribe(this);

		this.alarm = AlarmIntentHelper.fetchAlarmOrPreset( this );

		this.setTitle( alarm.printName() );

		setupSimplePreferencesScreen();

		setupActionBar();

		
		// remove the flash light setting of the device doesn't support it. 
		if(!this.deviceSupportsFlashLight()) {
			this.removeFlashLightPref();
		}
		
		if(alarm.isPresetAlarm()) {
			// having a delete button for the presets alarm makes no sense, so remove it. 
			removeDeleteButton();
			removeEditTitle();
			removeAlarmToggle();		
		}
		
		removeMiscCategoryIfEmpty();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.alarm_settings_action_remove:
			this.deleteAlarm();
			break;

		case R.id.alarm_settings_action_set_time:
			this.issueNormalPicker();
			break;

		case R.id.alarm_settings_action_set_countdown:
			this.issueCountdownPicker();
			break;

		case android.R.id.home:
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void issueNormalPicker() {
		((TimepickerPreference) findPreference( TIME )).show();
	}

	private void issueCountdownPicker() {
		SecondTimePickerDialog.OnTimeSetListener onTimePickerSet = new SecondTimePickerDialog.OnTimeSetListener() {
			@Override
			public void onTimeSet( SecondTimePicker view, int h, int m, int s ) {
				alarm.setTime( new CountdownTime( h, m, s ) );
			}
		};

		// TODO possibly use some way that doesn't make the dialog close on rotate
		AlarmTime time = alarm.getTime();
		time.refresh();

		SecondTimePickerDialog tpd = new SecondTimePickerDialog(
			this, onTimePickerSet,
			time.getHour(), time.getMinute(), time.getSecond(),
			true
		);

		tpd.show();
	}

	private boolean deviceSupportsFlashLight() {
		Context context = this;
		PackageManager pm = context.getPackageManager();

		return pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
	}

	// Using deprecated methods because we need to support Android API level 8
	@SuppressWarnings("deprecation")
	private void setupSimplePreferencesScreen() {
		addPreferencesFromResource(R.xml.pref_alarm_general);

		bindPreferenceSummaryToValue(findPreference(TIME));
		bindPreferenceSummaryToValue(findPreference(NAME));
		bindPreferenceSummaryToValue(findPreference(DAYS));
		bindPreferenceSummaryToValue(findPreference(REPEAT));
		bindPreferenceSummaryToValue(findPreference(VIBRATION));
		bindPreferenceSummaryToValue(findPreference(VOLUME));
		bindPreferenceSummaryToValue(findPreference(CHALLENGE));
		bindPreferenceSummaryToValue(findPreference(ENABLE_SNOOZE));
		bindPreferenceSummaryToValue(findPreference(SNOOZE_TIME));
		bindPreferenceSummaryToValue(findPreference(SPEECH));	
		bindPreferenceSummaryToValue(findPreference(FLASH));

			
		findPreference(DELETE).setOnPreferenceClickListener(new OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				deleteAlarm();
				return true;
			}
		});

		findPreference(SPEECH_SAMPLE).setOnPreferenceClickListener(new OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				Debug.d("speech sample here");
			
				TextToSpeech tts = SFApplication.get().getTts();
				
				languageHasNoVoice(tts);
				
				
				String s = new SpeechLocalizer(tts, AlarmSettingsActivity.this).getSpeech("Dry and mostly cloudy");
				Debug.d("s: " + s);
				TextToSpeechUtil.speakAlarm(tts, s);				
				return true;
			}
		});
		
		this.bindChallengeAdvancedButton();

		this.setupRingerPreferences();
	}

	// if the user's current language doesn't have a voice installed
	// notify the user that the English voice will used instead.
	// also recommend the user to install a voice for his/her language. 
	public void languageHasNoVoice(TextToSpeech tts ) {
		Locale deviceLocale = Locale.getDefault();
		if(!TextToSpeechUtil.languageHasVoice(deviceLocale, tts, this)) {
			DialogUtils.showDoNotShowAgainMessageBox(this, 
					 getResources().getString(R.string.no_voice_installed_title),
			 getResources().getString(R.string.no_voice_installed_message),

					"no_voice_installed_for_language");

		}
		
	}

	private void bindChallengeAdvancedButton() {
		((EnablePlusSettingsPreference) findPreference(CHALLENGE)).setOnButtonClickListener(new OnClickListener() {
			@Override
			public void onClick( View v ) {
				Intent i = new Intent(AlarmSettingsActivity.this, ChallengeSettingsActivity.class);
				startActivity( new AlarmIntentHelper(i).setAlarm( alarm ).intent() );
			}
		});
	}

	private void deleteAlarm() {
		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				alarmList.remove(alarm);
				finish();
			}
		};
		DialogUtils.showConfirmationDialog(
				getResources().getString(R.string.confirm_delete),
				AlarmSettingsActivity.this,
				dialogClickListener);
	}

	private void setupRingerPreferences() {
		this.ringerPreference = this.findPreference( RINGER_SUBSCREEN );
		this.ringerPreference.setOnPreferenceClickListener( new OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick( Preference preference ) {
				startRingerEdit();
				return true;
			}
		} );

		this.updateRingerSummary();
	}

	private void updateRingerSummary() {
		AudioDriverFactory factory = app().getAudioDriverFactory();
		AudioDriver driver = factory.produce( this, this.alarm.getAudioSource() );
		this.ringerPreference.setSummary( driver.printSourceName() );
	}

	@Handler
	public void handleAudioChange( AudioChangeEvent evt ) {
		Debug.d("handle audio change");
		if ( evt.getModifiedField() == Field.AUDIO_SOURCE ) {
			this.updateRingerSummary();
		}
	}

	private void startRingerEdit() {
		Intent i = new Intent(this, RingerSettingsActivity.class );
		this.startActivity( new AlarmIntentHelper( i ).setAlarm( alarm ).intent() );
	}

	@SuppressWarnings( "deprecation" )
	public Preference findPreference( CharSequence key ) {
		return super.findPreference( key );
	}

	private Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
		@Override
		public boolean onPreferenceChange(Preference preference, Object value) {
			String stringValue = value.toString();
			
			if (TIME.equals(preference.getKey())) {
				TimepickerPreference tpPref = (TimepickerPreference) preference;

				AlarmTime time = new ExactTime( tpPref.getHour(), tpPref.getMinute() );
				alarm.setTime( time );

				updateTimeSummary();
			}
			else if (NAME.equals(preference.getKey())) {
				alarm.setName(stringValue);
				preference.setSummary(stringValue);
			}
			else if(DAYS.equals(preference.getKey())) {
				alarm.setEnabledDays(((MultiSelectListPreference) preference).getEntryChecked());
				preference.setSummary(DateTextUtils.makeEnabledDaysText(alarm));	
			}
			else if (REPEAT.equals(preference.getKey())) {
				alarm.setRepeat(("true".equals(stringValue)) ? true : false);
				
			}
			else if (VIBRATION.equals(preference.getKey())) {
				alarm.getAudioConfig().setVibrationEnabled(("true".equals(stringValue)) ? true : false);
			}
			else if (VOLUME.equals(preference.getKey())) {
				alarm.getAudioConfig().setVolume(Integer.parseInt(stringValue));
				preference.setSummary(stringValue + "%");
			}
			else if (CHALLENGE.equals(preference.getKey())) {
				boolean enabled = (Boolean) value;
				AlarmSettingsActivity.this.alarm.getChallengeSet().setEnabled( enabled );
			}
			else if (ENABLE_SNOOZE.equals(preference.getKey())) {
				alarm.getSnoozeConfig().setSnoozeEnabled("true".equals(stringValue) ? true : false);
			}
			else if (SNOOZE_TIME.equals(preference.getKey())) {
				if (stringValue.equals("") || stringValue.equals("0")) {
					stringValue = preference.getSummary().toString();
				}
				int time = Integer.parseInt(stringValue);
				alarm.getSnoozeConfig().setSnoozeTime(time);
				String summary = time + " " + getResources()
						.getQuantityText(R.plurals.minute, time).toString();
				preference.setSummary(summary);
			}
			else if (SPEECH.equals(preference.getKey())) {
				TextToSpeech tts = SFApplication.get().getTts();
				languageHasNoVoice(tts);
				alarm.setSpeech(("true".equals(stringValue)) ? true : false);
			}
			else if (FLASH.equals(preference.getKey())) {
				alarm.setFlash(("true".equals(stringValue)) ? true : false);
			}	
			
			
			return true;
		}
	};
	
	private void bindPreferenceSummaryToValue(Preference preference) {
		preference.setPersistent(false);
		
		if (NAME.equals(preference.getKey())) {
			String name = alarm.printName();
			((EditTextPreference) preference).setText( name );
			preference.setSummary( name );
		}
		else if (TIME.equals(preference.getKey())) {
			initiateTimePicker((TimepickerPreference)preference);
			updateTimeSummary();
		}
		else if(DAYS.equals(preference.getKey())) {
			((MultiSelectListPreference) preference).setEntryChecked(alarm.getEnabledDays());;
			preference.setSummary(DateTextUtils.makeEnabledDaysText(alarm));	
		}
		else if (REPEAT.equals(preference.getKey())) {
			((CheckBoxPreference) preference).setChecked(alarm.isRepeating());
		}
		else if (VIBRATION.equals(preference.getKey())) {
			((CheckBoxPreference) preference).setChecked(alarm.getAudioConfig().getVibrationEnabled());
		}
		else if (VOLUME.equals(preference.getKey())) {
			int vol = alarm.getAudioConfig().getVolume();
			((VolumePreference) preference).setVolume(vol);
			preference.setSummary(vol + "%");
		}
		else if (CHALLENGE.equals(preference.getKey())) {
			boolean enabled = this.alarm.getChallengeSet().isEnabled();
			((CheckBoxPreference) preference).setChecked(enabled);
		}
		else if (ENABLE_SNOOZE.equals(preference.getKey())) {
			boolean enabled = this.alarm.getSnoozeConfig().isSnoozeEnabled();
			((CheckBoxPreference) preference).setChecked(enabled);
		}
		else if (SNOOZE_TIME.equals(preference.getKey())) {
			int time = this.alarm.getSnoozeConfig().getSnoozeTime();
			NumberPickerDialogPreference pref = ((NumberPickerDialogPreference) preference);
			pref.setValue(time);
			String summary = time + " " + getResources().getQuantityText(R.plurals.minute, time).toString();
			pref.setSummary(summary);
		}else if (SPEECH.equals(preference.getKey())) {
			((CheckBoxPreference) preference).setChecked(alarm.isSpeech());
		}else if (FLASH.equals(preference.getKey())) {
			((CheckBoxPreference) preference).setChecked(alarm.isFlashEnabled());
		}

		preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);
	}
	
	private void initiateTimePicker( TimepickerPreference tp ) {
		AlarmTime time = this.alarm.getTime();
		time.refresh();

		tp.setOnPreferenceClickListener( new OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick( Preference preference ) {
				TimepickerPreference tp = (TimepickerPreference) preference;
				AlarmTime time = alarm.getTime();
				tp.setHour( time.getHour() );
				tp.setMinute( time.getMinute() );
				return false;
			}
		} );

		tp.setHour( time.getHour() );
		tp.setMinute( time.getMinute() );
	}
}
