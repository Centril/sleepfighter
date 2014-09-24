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

package se.toxbee.commons.string

import spock.lang.Specification

class StringUtilsTest extends Specification {
	def "CastUpper"() {
		given:
			String subject = "Hello World"
			String result = "HELLO WORLD"
		expect:
			StringUtils.castUpper( subject ) == result
	}

	def "CastLower"() {
		given:
			String subject = "Hello World"
			String result = "hello world"

		expect:
			StringUtils.castLower( subject ) == result
	}

	def "CapitalizeFirst"() {
		given:
			String subject = "hello world"
			String result = "Hello world"
		expect:
			StringUtils.capitalizeFirst( subject ) == result
	}

	def "ReadHexString"() {
		expect:
			StringUtils.readHexString( subject ) == result
		where:
			subject << ["FF", "#FF", "0xFF"]
			result <<  [0xFF, 0xFF, 0xFF]
	}

	def "JoinTime"() {
		given:
			def subject = [1, 31]
			def result = "01:31"
		expect:
			StringUtils.joinTime( subject as int[] ) == result
	}

	def "GetDititsInWhenNull"() {
		when:
			StringUtils.getDigitsIn( null )
		then:
			thrown( NullPointerException )
	}

	def "GetDigitsIn"() {
		expect:
			StringUtils.getDigitsIn( str ) == result
		where:
			str         |   result
			""          |   -1
			"12A3bc4"   |   1234
			"12a3bc4"   |   1234
			"12A3BC4"   |   1234
	}
}
