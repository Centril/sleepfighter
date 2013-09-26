package se.chalmers.dat255.sleepfighter.activities;

import se.chalmers.dat255.sleepfighter.R;
import se.chalmers.dat255.sleepfighter.challenges.MemoryChallenge;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class MemoryActivity extends Activity {

	private MemoryChallenge mc;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alarm_challenge_memory);
		mc = new MemoryChallenge();
	}

	private void next() {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
	}
}
