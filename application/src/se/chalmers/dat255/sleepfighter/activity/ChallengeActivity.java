package se.chalmers.dat255.sleepfighter.activity;

import se.chalmers.dat255.sleepfighter.challenge.Challenge;
import se.chalmers.dat255.sleepfighter.challenge.gridsnake.MotionSnakeChallenge;
import se.chalmers.dat255.sleepfighter.challenge.TestChallenge;
import se.chalmers.dat255.sleepfighter.challenge.fluidsnake.SnakeChallenge;
import android.app.Activity;
import android.os.Bundle;

/**
 * An empty activity to be modified by a Challenge instance.<br/>
 * The calling activity can check if the user completed the challenge by
 * starting this using {@code startActivityForResult} and checking that the
 * {@code resultCode} is {@code Activity.RESULT_OK}.
 */
public class ChallengeActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// TODO do something else to get an instance of Challenge and call
		// challenge.start(this)
		Challenge challenge = new MotionSnakeChallenge();
		challenge.start(this);
	}

	
	public void complete() {
		setResult(Activity.RESULT_OK);
		finish();
	}

	public void fail() {
		setResult(Activity.RESULT_CANCELED);
		finish();
	}

}
