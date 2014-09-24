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
import spock.lang.Shared
import spock.lang.Specification

class ConversionTest extends Specification {
	def @Shared inputs2 = [
		[] as boolean[],
		[true, true, true, true] as boolean[],
		[true, true, true] as boolean[],
		[true, true, false, true] as boolean[]
	]

	def @Shared inputs = [
		[ false, false, false, false ] as boolean[],
		[ true, true, true, true ] as boolean[],
		[ true, true, true, false ] as boolean[],
		[ true, true, false, true ] as boolean[]
	]

	def @Shared tests = [0, 15, 7, 11]

	def "BoolArrayToIntNullPtr"() {
		when:
			Conversion.boolArrayToInt( null )
		then:
			thrown( NullPointerException )
	}

	def "BoolArrayToInt"() {
		expect:
			Conversion.boolArrayToInt( input ) == test
		where:
			input << inputs
			test << tests
	}

	def "IntToBoolArrayNegative"() {
		when:
			Conversion.intToBoolArray( i, b )
		then:
			thrown( IllegalArgumentException )
		where:
			i   |   b
			-1  |   -1
			-1  |   0
			0   |   -1
	}

	def "IntToBoolArray1Negative"() {
		when:
			Conversion.intToBoolArray( -1 )
		then:
			thrown( IllegalArgumentException )
		when:
			Conversion.intToBoolArray( 0 )
		then:
			notThrown( IllegalArgumentException )
	}

	def "IntToBoolArray"() {
		expect:
			Arrays.equals( input, Conversion.intToBoolArray( test, input.length ) )
		where:
			input << inputs
			test << tests
	}

	def "IntToBoolArray1"() {
		expect:
			Arrays.equals( input, Conversion.intToBoolArray( test ) )
		where:
			input << inputs2
			test << tests
	}
}
