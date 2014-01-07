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
package se.toxbee.sleepfighter.activity;

import se.toxbee.sleepfighter.R;
import se.toxbee.sleepfighter.android.utils.ActivityUtils;
import se.toxbee.sleepfighter.helper.AlarmIntentHelper;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.view.MenuItem;

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
