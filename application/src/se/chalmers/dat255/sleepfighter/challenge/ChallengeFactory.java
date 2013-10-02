package se.chalmers.dat255.sleepfighter.challenge;

/**
 * A simple factory class for construction of challenges.
 */
public class ChallengeFactory {

	/**
	 * Construct a new instance of {@link Challenge} from a given
	 * {@link ChallengeType}.
	 * 
	 * @param type
	 *            the type of the challenge
	 * @return a new instance of the challenge
	 */
	public static Challenge getChallenge(ChallengeType type) {
		switch (type) {
		case TEST:
			return new TestChallenge();
		case MATH:
			return new SimpleMathChallenge();
		case MEMORY:
			return new MemoryChallenge();
		default:
			throw new IllegalArgumentException("Undefined challenge");
		}
	}
}
