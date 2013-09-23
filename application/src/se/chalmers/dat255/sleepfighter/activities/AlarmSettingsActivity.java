package se.chalmers.dat255.sleepfighter.activities;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.text.TextUtils;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;

import java.util.List;

import se.chalmers.dat255.sleepfighter.R;
import se.chalmers.dat255.sleepfighter.R.string;
import se.chalmers.dat255.sleepfighter.R.xml;
import se.chalmers.dat255.sleepfighter.TimepickerPreference;

public class AlarmSettingsActivity extends PreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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

		bindPreferenceSummaryToValue(findPreference("pref_alarm_time"));
		bindPreferenceSummaryToValue(findPreference("pref_alarm_name"));
	}

	private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
		@Override
		public boolean onPreferenceChange(Preference preference, Object value) {
			String stringValue = value.toString();

			if (preference instanceof TimepickerPreference) {
				TimepickerPreference tpPref = (TimepickerPreference) preference;
				
				int hour = tpPref.getHour();
				int minute = tpPref.getMinute();
				
				// TODO: Assign the actual alarm the values
				
				preference.setSummary(stringValue);
			}
			else {
				preference.setSummary(stringValue);
			}
			return true;
		}
	};

	private static void bindPreferenceSummaryToValue(Preference preference) {
		preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

		// TODO: Check if !persistent it works
		preference.setPersistent(false);			
		
		sBindPreferenceSummaryToValueListener.onPreferenceChange(
				preference,
				PreferenceManager.getDefaultSharedPreferences(
						preference.getContext()).getString(preference.getKey(),
						""));
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static class GeneralPreferenceFragment extends PreferenceFragment {
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.pref_alarm_general);

			bindPreferenceSummaryToValue(findPreference("pref_alarm_time"));
			bindPreferenceSummaryToValue(findPreference("pref_alarm_name"));
		}
	}
}
