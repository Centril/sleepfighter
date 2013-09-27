package se.chalmers.dat255.sleepfighter.challenge;

import se.chalmers.dat255.sleepfighter.activity.ChallengeActivity;


/**
 * Interface implemented by challenges.
 * 
 * It works by giving access to methods in an empty {@code ChallengeActivity}
 * that should be used in order modify its contents. The implementing class
 * should at some point call {@code complete()} in the given
 * {@code ChallengeActivity}, when the user completes the challenge.
 */
public interface Challenge {
	/**
	 * Method called from the outside when the challenge is to be started.
	 * 
	 * Here should at least {@code setContentView()},
	 * or similar method in ChallengeActivity, be called.
	 * 
	 * @param activity
	 *            the ChallengeActivity that the Challenge modifies
	 */
	void start(ChallengeActivity activity);
}
