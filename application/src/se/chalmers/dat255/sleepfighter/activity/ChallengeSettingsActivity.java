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
import se.chalmers.dat255.sleepfighter.android.preference.EnablePlusSettingsPreference;
import se.chalmers.dat255.sleepfighter.model.Alarm;
import se.chalmers.dat255.sleepfighter.model.challenge.ChallengeConfigSet;
import se.chalmers.dat255.sleepfighter.model.challenge.ChallengeType;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ChallengeSettingsActivity extends PreferenceActivity {

	private Alarm alarm;
	private String[] names;
	private String[] descriptions;

	@Override
	// Uses non-fragment based preferences
	@SuppressWarnings("deprecation")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.alarm = AlarmIntentHelper.fetchAlarmOrPreset( this );

		addPreferencesFromResource(R.xml.pref_alarm_challenge);

		// Programmatically add Preference entries in category for all
		// challenges defined in enum
		PreferenceCategory pc = (PreferenceCategory) findPreference("pref_challenge_category");
		ChallengeType[] types = ChallengeType.values();
		for (final ChallengeType type : types) {
			Preference p = getChallengePreference(type);
			pc.addPreference(p);
		}
	}

	/**
	 * Create a Preference for a ChallengeType
	 * 
	 * @param type
	 *            the ChallengeType
	 * @return a Preference for the ChallengeType
	 */
	private Preference getChallengePreference(final ChallengeType type) {
		final EnablePlusSettingsPreference preference = new EnablePlusSettingsPreference(this);

		boolean enabled = this.alarm.getChallengeSet().getConfig(type).isEnabled();
		preference.setChecked(enabled);

		// Makes sure nothing is stored in SharedPreferences
		preference.setPersistent(false);

		preference.setTitleColor( this.getResources().getColor( R.color.holo_red_light ) );
		preference.setOnButtonClickListener( new OnClickListener() {
			@Override
			public void onClick( View v ) {
				gotoPreferencesSettings( (Button) v );
			}
		} );

		String name = getName(type);
		String description = getDescription(type);

		preference.setTitle(name);
		preference.setSummary(description);
		preference.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			@Override
			public boolean onPreferenceChange(Preference preference,
					Object newValue) {
				Log.d("abc", "hey" );
				boolean checked = (Boolean) newValue;
				
				ChallengeConfigSet set = ChallengeSettingsActivity.this.alarm.getChallengeSet();
				set.setEnabled(type, checked);
				
				return true;
			}
		});
		return preference;
	}

	protected void gotoPreferencesSettings( Button v ) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Gets the localized name for a challenge.
	 * 
	 * @param type
	 *            the ChallengeType
	 * @return the localized name for the challenge
	 */
	private String getName(ChallengeType type) {
		if (this.names == null) {
			this.names = getResources().getStringArray(R.array.challenge_names);
		}
		int ordinal = type.ordinal();
		if (ordinal >= this.names.length) {
			// No name in array!
			// Return enum name, perhaps should throw exception later
			return type.name();
		}
		return this.names[ordinal];
	}

	/**
	 * Gets the localized description for a challenge.
	 * 
	 * @param type
	 *            the ChallengeType
	 * @return the localized description for the challenge
	 */
	private String getDescription(ChallengeType type) {
		if (this.descriptions == null) {
			this.descriptions = getResources().getStringArray(
					R.array.challenge_descriptions);
		}
		int ordinal = type.ordinal();
		if (ordinal >= this.descriptions.length) {
			// No description in array!
			// Return empty string, perhaps should throw exception later
			return "";
		}
		return this.descriptions[ordinal];
	}
}
