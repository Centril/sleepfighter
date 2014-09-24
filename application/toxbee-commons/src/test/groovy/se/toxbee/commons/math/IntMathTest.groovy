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

package se.toxbee.commons.math

import spock.lang.Specification

class IntMathTest extends Specification {
	def "test findClosestFactors"() {
		given:
			def factors = IntMath.findClosestFactors( input )
		expect:
			test == null ? factors == null : Arrays.equals( factors, test as int[] )
		where:
			input   |   test
			-1      |   [-1, 1]
			0       |   [0, 0]
			1       |   [1, 1]
			2       |   [1, 2]
			3       |   [1, 3]
			4       |   [2, 2]
			5       |   [1, 5]
			9       |   [3, 3]
	}
}
