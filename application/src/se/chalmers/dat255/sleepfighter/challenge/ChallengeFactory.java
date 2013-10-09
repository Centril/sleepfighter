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

import java.util.Map;

import se.chalmers.dat255.sleepfighter.challenge.fluidsnake.FluidSnakeChallenge;
import se.chalmers.dat255.sleepfighter.challenge.math.MathChallenge;
import se.chalmers.dat255.sleepfighter.challenge.memory.MemoryChallenge;
import se.chalmers.dat255.sleepfighter.challenge.rotosnake.RotoSnakeChallenge;
import se.chalmers.dat255.sleepfighter.challenge.sort.SortChallenge;
import se.chalmers.dat255.sleepfighter.model.challenge.ChallengeType;

import com.google.common.collect.Maps;

/**
 * A simple factory class for construction of challenges.
 */
public class ChallengeFactory {
	private static Map<ChallengeType, ChallengePrototypeDefinition> prototypeDefinitions;

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
			return new MathChallenge();
		case MEMORY:
			return new MemoryChallenge();
		case SORT:
			return new SortChallenge();
		case FLUID_SNAKE:
			return new FluidSnakeChallenge();
		case ROTO_SNAKE:
			return new RotoSnakeChallenge();
		case SHAKE:
			return new ShakeChallenge();
		default:
			throw new IllegalArgumentException("Undefined challenge");
		}
	}

	public static ChallengePrototypeDefinition getPrototypeDefinition( ChallengeType type ) {
		if ( prototypeDefinitions == null ) {
			prototypeDefinitions = Maps.newEnumMap( ChallengeType.class );
		}

		ChallengePrototypeDefinition def = prototypeDefinitions.get( type );

		if ( def == null ) {
			switch ( type ) {
			case TEST:
				def = new TestChallenge.PrototypeDefinition();
				break;

			case MATH:
				def = new MathChallenge.PrototypeDefinition();
				break;

			case MEMORY:
				def = new MemoryChallenge.PrototypeDefinition();
				break;

			case SORT:
				def = new SortChallenge.PrototypeDefinition();
				break;

			case FLUID_SNAKE:
				def = new FluidSnakeChallenge.PrototypeDefinition();
				break;
			case ROTO_SNAKE:
				def = new RotoSnakeChallenge.PrototypeDefinition();
				break;
			case SHAKE:
				def = new RotoSnakeChallenge.PrototypeDefinition();
				break;
			default:
				throw new IllegalArgumentException( "Undefined challenge" );
			}

			prototypeDefinitions.put( type, def );
		}

		return def;
	}
}
