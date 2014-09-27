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

package se.toxbee.commons.geom

import spock.lang.Specification

class DirectionTest extends Specification {
	def "DEG_90"() {
		given:
			int i = 0;
			Direction[] dirs = Direction.deg90();
		expect:
			dirs.length == 4;
			dirs[i++] == Direction.WEST;
			dirs[i++] == Direction.NORTH;
			dirs[i++] == Direction.EAST;
			dirs[i++] == Direction.SOUTH;
	}

	def "DEG_45"() {
		given:
			int i = 0;
			Direction[] dirs = Direction.deg45();
		expect:
			dirs.length == 4;
			dirs[i++] == Direction.NORTH_WEST;
			dirs[i++] == Direction.NORTH_EAST;
			dirs[i++] == Direction.SOUTH_EAST;
			dirs[i++] == Direction.SOUTH_WEST;
	}

	def "DEG_ALL"() {
		given:
			int i = 0;
			Direction[] dirs = Direction.all();
		expect:
			dirs.length == 8;
			dirs[i++] == Direction.WEST;
			dirs[i++] == Direction.NORTH_WEST;
			dirs[i++] == Direction.NORTH;
			dirs[i++] == Direction.NORTH_EAST;
			dirs[i++] == Direction.EAST;
			dirs[i++] == Direction.SOUTH_EAST;
			dirs[i++] == Direction.SOUTH;
			dirs[i++] == Direction.SOUTH_WEST;
	}

	def "getOpposite"() {
		expect:
			a.getOpposite() == b
		where:
			a                    | b
			Direction.NONE       | Direction.NONE
			Direction.NORTH      | Direction.SOUTH
			Direction.NORTH_EAST | Direction.SOUTH_WEST
			Direction.EAST       | Direction.WEST
			Direction.SOUTH_EAST | Direction.NORTH_WEST
			Direction.SOUTH      | Direction.NORTH
			Direction.SOUTH_WEST | Direction.NORTH_EAST
			Direction.WEST       | Direction.EAST
	}

	def "isOpposite"() {
		Direction.values().each { dir ->
			opp = dir.getOpposite();
			assert dir.isOpposite( opp );

			List<Direction> falseTests = Direction.values().collect().remove( opp );
			falseTests.each { test -> assert !dir.isOpposite( test ) }
		}
	}
}
