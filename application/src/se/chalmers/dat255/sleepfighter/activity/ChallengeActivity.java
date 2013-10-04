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

import se.chalmers.dat255.sleepfighter.challenge.Challenge;
import se.chalmers.dat255.sleepfighter.challenge.ChallengeFactory;
import se.chalmers.dat255.sleepfighter.model.challenge.ChallengeType;
import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

/**
 * An activity for different types of challenges.<br/>
 * Started with a bundled {@link ChallengeType} with name defined by
 * {@code BUNDLE_CHALLENGE_TYPE}. The calling activity can check if the user has
 * completed the challenge by starting this using {@code startActivityForResult}
 * and checking that the {@code resultCode} is {@code Activity.RESULT_OK}.
 */
public class ChallengeActivity extends Activity {

	public static final String BUNDLE_CHALLENGE_TYPE = "bundle_challenge_type";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Object bundled = getIntent().getSerializableExtra(BUNDLE_CHALLENGE_TYPE);
		if (!(bundled instanceof ChallengeType)) {
			throw new IllegalArgumentException("No type sent");
		}
		ChallengeType type = (ChallengeType) bundled;
		Challenge challenge = ChallengeFactory.getChallenge(type);

		challenge.start(this);
	}

	public void complete() {
		Toast.makeText(this, "DEBUG: Completed challenge", Toast.LENGTH_SHORT).show();
		setResult(Activity.RESULT_OK);
		finish();
	}

	public void fail() {
		setResult(Activity.RESULT_CANCELED);
		finish();
	}
}
