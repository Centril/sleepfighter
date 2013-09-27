package se.chalmers.dat255.sleepfighter.activities;

import se.chalmers.dat255.sleepfighter.R;
import se.chalmers.dat255.sleepfighter.challenges.SimpleMathChallenge;
import se.chalmers.dat255.sleepfighter.debug.Debug;
import android.app.Activity;
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

	// public void buttonMath(View view) {
	// submitAns();
	// }

	private void next() {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
	}

	// public void submitAns() {
	// boolean correctAnswer = false;
	// try {
	// if (Integer.parseInt(editText.getText().toString()) == smc
	// .getResult()) {
	// correctAnswer = true;
	// next();
	// finish();
	// Toast.makeText(getBaseContext(), "Alarm deactivated",
	// Toast.LENGTH_SHORT).show();
	// }
	// } catch (NumberFormatException e) {
	// // Handled as wrong answer
	// }
	// if (!correctAnswer) {
	// Toast.makeText(getBaseContext(), "Sorry, wrong answer!",
	// Toast.LENGTH_SHORT).show();
	// smc.runChallenge();
	// userText.setText(smc.getCalculation());
	// editText.setText("");
	// }
	// }
}
