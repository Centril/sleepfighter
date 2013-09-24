package se.chalmers.dat255.sleepfighter.activities;

import se.chalmers.dat255.sleepfighter.R;
import se.chalmers.dat255.sleepfighter.challenges.SimpleMathActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * An abstract activity for the challenges
 * 
 * @version 1.0
 * @since Sep 24, 2013
 */

public class ChallengeActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alarm_prechallenge);

	}

	public void button(View view) {
		Intent intent = new Intent(this, SimpleMathActivity.class);
		startActivity(intent);
	}

}
