package se.chalmers.dat255.sleepfighter.activities;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.view.MenuItem;
import android.widget.Toast;
import android.support.v4.app.NavUtils;

import se.chalmers.dat255.sleepfighter.R;
import se.chalmers.dat255.sleepfighter.TimepickerPreference;
import se.chalmers.dat255.sleepfighter.model.Alarm;
import se.chalmers.dat255.sleepfighter.model.AlarmList;
import se.chalmers.dat255.sleepfighter.SFApplication;

public class AlarmSettingsActivity extends PreferenceActivity {

	private static final String NAME = "pref_alarm_name";
	private static final String TIME = "pref_alarm_time";
	private static final String DAYS = "pref_enabled_days";
	
	private static Alarm alarm;	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if (getIntent().getExtras() == null) {
			throw new IllegalArgumentException();
		}
		
		int id = this.getIntent().getExtras().getInt("id");
		
		AlarmList alarms = ((SFApplication) getApplication()).getAlarms();
		
		alarm = alarms.getById(id);
		
		if (alarm == null) {
			// TODO: Better handling for final product
			Toast.makeText(this, "Alarm is null (ID: " + id + ")", Toast.LENGTH_SHORT).show();
			finish();
		}
		
		if (!"".equals(alarm.getName())) {
			this.setTitle(alarm.getName());
		}
		
		// TODO: Remove this debug thing
		this.setTitle(this.getTitle() + " (ID: " + alarm.getId() + ")");
		
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
		addPreferencesFromResource(R.xml.pref_alarm_general);

		bindPreferenceSummaryToValue(findPreference(TIME));
		bindPreferenceSummaryToValue(findPreference(NAME));
		bindPreferenceSummaryToValue(findPreference(DAYS));
	}

	private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
		@Override
		public boolean onPreferenceChange(Preference preference, Object value) {
			String stringValue = value.toString();

			if (preference instanceof TimepickerPreference) {
				TimepickerPreference tpPref = (TimepickerPreference) preference;
				
				int hour = tpPref.getHour();
				int minute = tpPref.getMinute();
				
				alarm.setTime(hour, minute);
				
				preference.setSummary((hour < 10 ? "0" : "") + hour + ":" + (minute < 10 ? "0" : "") + minute);
			}
			else {
				if (NAME.equals(preference.getKey())) {
					alarm.setName(stringValue);
				}
				preference.setSummary(stringValue);
			}
			return true;
		}
	};

	private static void bindPreferenceSummaryToValue(Preference preference) {
		
		// TODO: Check if !persistent it works
		preference.setPersistent(false);
		
		if (NAME.equals(preference.getKey())) {
			preference.setSummary(alarm.getName());
		}
		else if (TIME.equals(preference.getKey())) {
			preference.setSummary(alarm.getTimeString());
		}
		
		preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);
	}
}
