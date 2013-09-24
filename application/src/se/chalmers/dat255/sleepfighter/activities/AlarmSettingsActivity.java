package se.chalmers.dat255.sleepfighter.activities;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.widget.Toast;
import android.support.v4.app.NavUtils;

import se.chalmers.dat255.sleepfighter.R;
import se.chalmers.dat255.sleepfighter.TimepickerPreference;
import se.chalmers.dat255.sleepfighter.debug.Debug;
import se.chalmers.dat255.sleepfighter.model.Alarm;
import se.chalmers.dat255.sleepfighter.model.AlarmList;
import se.chalmers.dat255.sleepfighter.SFApplication;

public class AlarmSettingsActivity extends PreferenceActivity {

	private final String NAME = "pref_alarm_name";
	private final String TIME = "pref_alarm_time";
	private final String DAYS = "pref_enabled_days";
	
	private Alarm alarm;	
	
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

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

		setupSimplePreferencesScreen();
	}

	private void setupSimplePreferencesScreen() {
		addPreferencesFromResource(R.xml.pref_alarm_general);

		bindPreferenceSummaryToValue(findPreference(TIME));
		bindPreferenceSummaryToValue(findPreference(NAME));
		bindPreferenceSummaryToValue(findPreference(DAYS));
	}

	private Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
		@Override
		public boolean onPreferenceChange(Preference preference, Object value) {
			String stringValue = value.toString();

			if (preference instanceof TimepickerPreference) {
				TimepickerPreference tpPref = (TimepickerPreference) preference;
				
				int hour = tpPref.getHour();
				int minute = tpPref.getMinute();
				
				alarm.setTime(hour, minute);
				
				preference.setSummary(stringValue);
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

	private void bindPreferenceSummaryToValue(Preference preference) {
		preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

		// TODO: Check if !persistent it works
		preference.setPersistent(false);
		
		if (NAME.equals(preference.getKey())) {
			preference.setSummary(alarm.getName());
		}
		else if (TIME.equals(preference.getKey())) {
			preference.setSummary(alarm.getTimeString());
		}
		
		sBindPreferenceSummaryToValueListener.onPreferenceChange(
				preference,
				PreferenceManager.getDefaultSharedPreferences(
						preference.getContext()).getString(preference.getKey(),
						""));
	}
}
