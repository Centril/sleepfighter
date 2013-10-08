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
package se.chalmers.dat255.sleepfighter.challenge;

import java.util.Random;

import se.chalmers.dat255.sleepfighter.R;
import se.chalmers.dat255.sleepfighter.activity.ChallengeActivity;
import se.chalmers.dat255.sleepfighter.model.challenge.ChallengeType;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * Example implementation of Challenge.
 */
public class TestChallenge implements Challenge {
	/**
	 * PrototypeDefinition for TestChallenge.
	 *
	 * @version 1.0
	 * @since Oct 5, 2013
	 */
	public static class PrototypeDefinition extends ChallengePrototypeDefinition {{
		setType( ChallengeType.TEST );
	}}

	@Override
	public void start( final ChallengeActivity activity, ChallengeResolvedParams params ) {
		activity.setContentView(R.layout.alarm_challenge_test);
		Button completeButton = (Button) activity
				.findViewById(R.id.btn_complete);
		Button failButton = (Button) activity.findViewById(R.id.btn_fail);
		
		Random random = new Random();
		int randomInt = random.nextInt(2);
		completeButton.setBackgroundColor((randomInt) == 0 ? Color.RED : Color.GREEN);
		failButton.setBackgroundColor((randomInt) == 0 ? Color.GREEN : Color.RED);
		
		completeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				activity.complete();
			}
		});

		failButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				activity.fail();
			}
		});
	}

	@Override
	public void start( ChallengeActivity activity, ChallengeResolvedParams params, Bundle state ) {
		this.start( activity, params );
	}

	@Override
	public Bundle savedState() {
		return null;
	}

	@Override
	public void onPause() {
	}

	@Override
	public void onResume() {
	}

	@Override
	public void onDestroy() {
	}
}