/*
 * Copyright 2014 toxbee.se
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package se.toxbee.sleepfighter.activity;

import android.app.Activity;
import android.os.Bundle;

import net.engio.mbassy.listener.Handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import se.toxbee.sleepfighter.app.SFApplication;
import se.toxbee.sleepfighter.challenge.Challenge;
import se.toxbee.sleepfighter.challenge.ChallengeProgressEvent;
import se.toxbee.sleepfighter.challenge.ChallengeResolvedParams;
import se.toxbee.sleepfighter.challenge.factory.ChallengeFactory;
import se.toxbee.sleepfighter.helper.AlarmIntentHelper;
import se.toxbee.sleepfighter.model.Alarm;
import se.toxbee.sleepfighter.model.challenge.ChallengeConfigSet;
import se.toxbee.sleepfighter.model.challenge.ChallengeType;
import se.toxbee.commons.math.RandomMath;

/**
 * An activity for different types of challenges.<br/>
 * Started with a bundled {@link ChallengeType} with name defined by
 * {@code BUNDLE_CHALLENGE_TYPE}. The calling activity can check if the user has
 * completed the challenge by starting this using {@code startActivityForResult}
 * and checking that the {@code resultCode} is {@code Activity.RESULT_OK}.
 */
public class ChallengeActivity extends Activity {
	public static final String BUNDLE_CHALLENGE_TYPE = "bundle_challenge_type";
	private static final String BUNDLE_CHALLENGE_DATA = "bundle_challenge_data";

	private Alarm alarm;
	private ChallengeConfigSet challengeSet;

	private ChallengeType challengeType;
	private Challenge challenge;

	private SFApplication app() {
		return SFApplication.get();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Fetch alarm & set.
		this.alarm = AlarmIntentHelper.fetchAlarmOrPreset(this);
		this.challengeSet = this.alarm.getChallengeSet();

		// Start listening for events.
		this.app().getBus().subscribe( this );

		// Start the challenge.
		if (savedInstanceState == null) {
			// Either fetch or make a random challenge type.
			Object bundled = getIntent().getSerializableExtra( BUNDLE_CHALLENGE_TYPE );
			ChallengeType type = bundled instanceof ChallengeType
							   ? (ChallengeType) bundled
							   : this.makeRandomChallenge();

			this.startFromScratch( type );
		} else {
			this.restart(savedInstanceState);
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		outState.putSerializable(BUNDLE_CHALLENGE_TYPE, this.challengeType);

		outState.putParcelable(BUNDLE_CHALLENGE_DATA,
				this.challenge.savedState());
	}

	/**
	 * Called when a challenge was saved to state.
	 * 
	 * @param state
	 *            the saved state to read from.
	 */
	private void restart(Bundle state) {
		this.challengeType = (ChallengeType) state
				.getSerializable(BUNDLE_CHALLENGE_TYPE);
		this.updateChallenge();

		Bundle challengeData = state.getBundle(BUNDLE_CHALLENGE_DATA);

		this.challenge.start(this, this.makeResolvedParams(), challengeData );
	}

	/**
	 * Called when no challenge was not saved to state.
	 */
	private void startFromScratch(ChallengeType type) {
		this.challengeType = type;
		this.updateChallenge();
		this.challenge.start(this, this.makeResolvedParams() );
	}

	/**
	 * Makes a ChallengeResolvedParams and resolves all params.
	 *
	 * @return the resolved params.
	 */
	private ChallengeResolvedParams makeResolvedParams() {
		ChallengeResolvedParams resolved = new ChallengeResolvedParams();
		resolved.resolve( this.challengeSet, ChallengeFactory.getPrototypeDefinition( this.challengeType ) );
		return resolved;
	}

	/**
	 * Sets the challenge using current type.
	 */
	private void updateChallenge() {
		this.challenge = ChallengeFactory.getChallenge(this.challengeType);
		this.challenge.setMessageBus( this.app().getBus() );
	}

	/**
	 * Generates a random challenge type.
	 * 
	 * @return the type.
	 */
	private ChallengeType makeRandomChallenge() {
		// Get a random challenge from the alarm's enabled challenges
		Set<ChallengeType> enabledChallenges = this.challengeSet
				.getEnabledTypes();

		List<ChallengeType> list = new ArrayList<ChallengeType>(
				enabledChallenges);
		int randPos = RandomMath.nextRandomRanged(new Random(), 0,
				list.size() - 1);

		ChallengeType type = list.get(randPos);

		return type;
	}

	/**
	 * Handles an event from the current Challenge.
	 *
	 * @param event the event.
	 */
	@Handler
	public void handleChallengeEvent( ChallengeProgressEvent event ) {
		switch ( event.getType() ) {
		case COMPLETED:
			this.complete();
			break;

		case FAILED:
			this.fail();
			break;

		default:
			throw new AssertionError();
		}
	}

	/**
	 * Called when a challenge is completed.
	 */
	private void complete() {
		setResult(Activity.RESULT_OK);
		
		finish();
	}

	/**
	 * Called when a challenge has not been completed.
	 */
	private void fail() {
		setResult(Activity.RESULT_CANCELED);
		finish();
	}

	@Override
	protected void onPause() {
		super.onPause();
		this.challenge.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		this.challenge.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		this.challenge.onDestroy();
	};
}
