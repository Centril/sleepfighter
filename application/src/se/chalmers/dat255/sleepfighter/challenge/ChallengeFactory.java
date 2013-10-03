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

import se.chalmers.dat255.sleepfighter.challenge.fluidsnake.FluidSnakeChallenge;
import se.chalmers.dat255.sleepfighter.challenge.memory.MemoryChallenge;
import se.chalmers.dat255.sleepfighter.challenge.sort.SortChallenge;

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
		case SORT:
			return new SortChallenge();
		case FLUID_SNAKE:
			return new FluidSnakeChallenge();
		default:
			throw new IllegalArgumentException("Undefined challenge");
		}
	}
}
