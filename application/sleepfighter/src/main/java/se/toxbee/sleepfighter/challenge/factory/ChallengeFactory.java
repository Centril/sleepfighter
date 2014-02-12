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

package se.toxbee.sleepfighter.challenge.factory;

import com.google.common.collect.Maps;

import java.util.Map;

import se.toxbee.sleepfighter.challenge.Challenge;
import se.toxbee.sleepfighter.challenge.ChallengePrototypeDefinition;
import se.toxbee.sleepfighter.challenge.fluidsnake.FluidSnakeChallenge;
import se.toxbee.sleepfighter.challenge.math.MathChallenge;
import se.toxbee.sleepfighter.challenge.memory.MemoryChallenge;
import se.toxbee.sleepfighter.challenge.rotosnake.RotoSnakeChallenge;
import se.toxbee.sleepfighter.challenge.shake.ShakeChallenge;
import se.toxbee.sleepfighter.challenge.sort.SortChallenge;
import se.toxbee.sleepfighter.model.challenge.ChallengeType;

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
				def = new ShakeChallenge.PrototypeDefinition();
				break;
			default:
				throw new IllegalArgumentException( "Undefined challenge" );
			}

			prototypeDefinitions.put( type, def );
		}

		return def;
	}
}
