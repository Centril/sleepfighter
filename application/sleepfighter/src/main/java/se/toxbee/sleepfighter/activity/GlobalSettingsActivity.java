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

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.view.MenuItem;

import se.toxbee.sleepfighter.R;
import se.toxbee.sleepfighter.android.utils.ActivityUtils;
import se.toxbee.sleepfighter.helper.AlarmIntentHelper;

public class GlobalSettingsActivity extends PreferenceActivity   {
	
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
		Intent i = new Intent(this, AlarmSettingsActivity.class );
		startActivity( new AlarmIntentHelper( i ).setSettingPresetAlarm(true).intent() );
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
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
