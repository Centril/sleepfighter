package se.chalmers.dat255.sleepfighter.activities;

import se.chalmers.dat255.sleepfighter.challenges.SimpleMathChallenge;
import android.os.Bundle;

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
/*
	private void next() {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
	}*/

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
