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

package se.toxbee.commons.factory

import spock.lang.Specification

class FactorySingletonInstantiatorTest extends Specification {
	class A implements FactoryInstantiator<Integer, Integer> {
		private int i = 0;

		@Override
		Integer produce( Integer key ) {
			return i++;
		}
	}

	def "Produce"() {
		given:
			A fi = new A();
			FactorySingletonInstantiator fsi = new FactorySingletonInstantiator( fi );
		expect:
			fsi.produce( null ) == 0;
			fsi.produce( null ) == 0;
	}
}
