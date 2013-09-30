package se.chalmers.dat255.sleepfighter.challenge.sort;

import se.chalmers.dat255.sleepfighter.activity.ChallengeActivity;
import se.chalmers.dat255.sleepfighter.challenge.Challenge;

public class SortChallenge implements Challenge {
	private ChallengeActivity activity;

	public SortChallenge() {
	}

	@Override
	public void start( ChallengeActivity activity ) {
		this.activity = activity;
	}
}