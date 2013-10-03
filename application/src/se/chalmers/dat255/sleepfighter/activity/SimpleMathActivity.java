/*******************************************************************************
 * Copyright (c) 2013 See AUTHORS file.
 * 
 * This file is part of SleepFighter.
 * 
 * SleepFighter is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * SleepFighter is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with SleepFighter. If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package se.chalmers.dat255.sleepfighter.activity;

import se.chalmers.dat255.sleepfighter.challenge.SimpleMathChallenge;
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
