package se.chalmers.dat255.sleepfighter.activity;

import se.chalmers.dat255.sleepfighter.R;
import se.chalmers.dat255.sleepfighter.utils.android.ActivityUtils;
import se.chalmers.dat255.sleepfighter.utils.android.IntentUtils;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;

public class GlobalSettingsActivity extends PreferenceActivity {
	
	private final String GLOBAL_PRESET_ALARM_SUBSCREEN = "perf_global_preset_alarm";

	// Needs to support API level 9
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ActivityUtils.setupStandardActionBar(this);
		
		addPreferencesFromResource(R.xml.pref_global_settings);
		
		setupGlobalPresetAlarmPreferences();
	}
	
	private void startGlobalPresetAlarmEdit() {
		Intent intent = new Intent(this, AlarmSettingsActivity.class );
		new IntentUtils( intent ).setSettingPresetAlarm(true);
		startActivity( intent );
	}
	
	private void setupGlobalPresetAlarmPreferences() {
		@SuppressWarnings("deprecation")
		Preference pref = this.findPreference( GLOBAL_PRESET_ALARM_SUBSCREEN );
		pref.setOnPreferenceClickListener( new OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick( Preference preference ) {
				
				startGlobalPresetAlarmEdit();
				return true;
			}
		} );

		//this.updateRingerSummary();
	}
}
