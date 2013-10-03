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
