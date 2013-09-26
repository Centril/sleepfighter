package se.chalmers.dat255.sleepfighter.challenges;

import se.chalmers.dat255.sleepfighter.activities.ChallengeActivity;
import se.chalmers.dat255.sleepfighter.debug.Debug;
/**
 * Example implementation of Challenge.
 */
public class MemoryChallenge implements Challenge {

	@Override
	public void start(final ChallengeActivity activity) {
		Debug.d("starting memory challenge");
	}

}
