package se.chalmers.dat255.sleepfighter.activities;

import se.chalmers.dat255.sleepfighter.R;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

public class GlobalSettingsActivity extends PreferenceActivity {

/*	private static final String NAME = "pref_alarm_name";
	private static final String TIME = "pref_alarm_time";
	private static final String DAYS = "pref_enabled_days";
	*/
	// is used in sBindPreferenceSummaryToValueListener
	
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

/*		bindPreferenceSummaryToValue(findPreference(TIME));
		bindPreferenceSummaryToValue(findPreference(NAME));
		bindPreferenceSummaryToValue(findPreference(DAYS));*/
	}
	
	private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
		@Override
		public boolean onPreferenceChange(Preference preference, Object value) {
			
			return true;
		}
	};
	
	private static void bindPreferenceSummaryToValue(Preference preference) {
		preference.setPersistent(false);
		/*
		if (NAME.equals(preference.getKey())) {
			preference.setSummary(alarm.getName());
		}
		else if (TIME.equals(preference.getKey())) {
			preference.setSummary(alarm.getTimeString());
		} else if(DAYS.equals(preference.getKey())) {
			preference.setSummary(formatDays(alarm));	
		}*/
		
		preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);
	}
}
