package se.chalmers.dat255.sleepfighter.activity;

import se.chalmers.dat255.sleepfighter.challenge.MemoryChallenge;
import android.os.Bundle;

public class MemoryActivity extends ChallengeActivity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		MemoryChallenge challenge = new MemoryChallenge();
		challenge.start(this);
		

	}
}
