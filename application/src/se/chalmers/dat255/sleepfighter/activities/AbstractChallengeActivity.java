package se.chalmers.dat255.sleepfighter.activities;

import se.chalmers.dat255.sleepfighter.R;
import android.app.Activity;
import android.os.Bundle;

/**
 * An abstract activity for the challenges
 * 
 * @version 1.0
 * @since Sep 24, 2013
 */

public class AbstractChallengeActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alarm_challenge);

	}

}
