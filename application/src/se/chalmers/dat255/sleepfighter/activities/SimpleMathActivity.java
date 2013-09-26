package se.chalmers.dat255.sleepfighter.activities;

import se.chalmers.dat255.sleepfighter.R;
import se.chalmers.dat255.sleepfighter.challenges.SimpleMathChallenge;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

public class SimpleMathActivity extends ChallengeActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SimpleMathChallenge smc = new SimpleMathChallenge();
		smc.start(this);
	}

	public void buttonMath(View view) {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
	}

}
