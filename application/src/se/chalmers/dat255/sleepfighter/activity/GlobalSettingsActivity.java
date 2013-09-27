package se.chalmers.dat255.sleepfighter.activity;

import se.chalmers.dat255.sleepfighter.R;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

public class GlobalSettingsActivity extends PreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setupSimplePreferencesScreen();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	// Using deprecated methods because we need to support Android API level 8
	@SuppressWarnings("deprecation")
	private void setupSimplePreferencesScreen() {
		addPreferencesFromResource(R.xml.pref_global_settings);

		bindPreferenceSummaryToValue(findPreference("pref_alarm_turn_screen_on"));
		bindPreferenceSummaryToValue(findPreference("pref_alarm_bypass_lock_screen"));
		bindPreferenceSummaryToValue(findPreference("pref_global_when_or_in_how_much"));
	}
	
	private Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
		@Override
		public boolean onPreferenceChange(Preference preference, Object value) {
			
			
			return true;
		}
	};
	
	private void bindPreferenceSummaryToValue(Preference preference) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		
		if (preference instanceof CheckBoxPreference) {
			prefs.getBoolean(preference.getKey(), false);
		}
		
		preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);
	}
}
