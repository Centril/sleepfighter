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

package se.toxbee.commons.reflect

import spock.lang.Specification

class ReflectionUtilTest extends Specification {
	def "test makeArray"() {
		when:
			Integer[] r = ReflectionUtil.makeArray( Integer.class, 5 );
		then:
			r.length == 5
	}

	def "test arrayClass"() {
		expect:
			Integer == ReflectionUtil.arrayClass( new Integer[5] )
	}

	static interface TestInterface {}
	static class TestClass1 implements TestInterface {}
	static class TestClass2 {}

	def "test asSubclass"() {
		expect:
			null != ReflectionUtil.asSubclass( TestClass1.class, TestInterface.class )
			null == ReflectionUtil.asSubclass( TestClass2.class, TestInterface.class )
	}
}
