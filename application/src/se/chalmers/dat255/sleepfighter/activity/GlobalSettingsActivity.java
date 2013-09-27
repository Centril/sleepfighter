package se.chalmers.dat255.sleepfighter.activity;

import se.chalmers.dat255.sleepfighter.R;
import se.chalmers.dat255.sleepfighter.utils.android.ActivityUtils;
import android.os.Bundle;
import android.preference.PreferenceActivity;

public class GlobalSettingsActivity extends PreferenceActivity {

	// Needs to support API level 9
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ActivityUtils.setupStandardActionBar(this);
		
		addPreferencesFromResource(R.xml.pref_global_settings);
	}
}
