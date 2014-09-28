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

package se.toxbee.commons.collect

import spock.lang.Specification

class PrimitiveArraysTest extends Specification {
	class R extends Random {
		IntRange range
		Iterator<Integer> iterator

		public void setRange( IntRange r ) {
			range = r
			iterator = r.iterator()
		}

		int nextInt( int n ) {
			n - 1 < (range.toInt + 1) / 2 ? n - 1 : iterator.next()
		}
	}

	def "test shuffle"() {
		given:
			def rng = new R()
		expect:
			all( 0..max as Object[] ) { Object[] array, Closure tc ->
				rng.range = 0..max
				def rev = apply( tc, array, PrimitiveArrays.&shuffle.rcurry( rng ) )
				assert rev == array.toList().reverse()
			}
		where:
			max << [9]
	}

	def "test reverseOrder"() {
		expect:
			all( 0..100 as Object[] ) { Object[] array, Closure tc ->
				def rev = apply( tc, array, PrimitiveArrays.&reverseOrder )
				assert rev == array.toList().reverse()
			}
	}

	def "test swap"() {
		expect:
			all( 0..1 as Object[] ) { Object[] array, Closure tc ->
				def swapped = apply( tc, array, PrimitiveArrays.&swap.rcurry( 0, 1 ) )
				assert swapped[0] == array[1]
				assert swapped[1] == array[0]
			}
	}

	def "test shift"() {
		given:
			def shift = PrimitiveArrays.&shift
		expect:
			all( 0..99 as Object[] ) { Object[] array, Closure tc ->
				def shifted = apply( tc, array, shift.rcurry( steps ) )
				array.eachWithIndex { def entry, int i ->
					assert entry == shifted[(i + steps) % array.length]
				}
			}
		where:
			steps << (-100..100)
	}

	// Helpers:

	def apply( Closure typeconverter, Object[] array, Closure apply ) {
		return (Object[]) typeconverter.curry( (Object[]) array.clone(), apply )()
	}

	def i( Object[] arr, ro ) {
		def e = arr as int[]
		ro.call( e )
		return e
	}

	def by( Object[] arr, ro ) {
		def e = arr as byte[]
		ro.call( e )
		return e
	}

	def s( Object[] arr, ro ) {
		def e = arr as short[]
		ro.call( e )
		return e
	}

	def l( Object[] arr, ro ) {
		def e = arr as long[]
		ro.call( e )
		return e
	}

	def f( Object[] arr, ro ) {
		def e = arr as float[]
		ro.call( e )
		return e
	}

	def d( Object[] arr, ro ) {
		def e = arr as double[]
		ro.call( e )
		return e
	}

	def c( Object[] arr, ro ) {
		def e = arr as char[]
		ro.call( e )
		return e
	}

	def bool( Object[] arr, ro ) {
		def e = arr as boolean[]
		ro.call( e )
		return e
	}

	def types = [this.&i, this.&by, this.&s, this.&l, this.&f, this.&d, this.&c]

	def all( v, Closure c ) {
		types.each {
			c( v, it )
		}

		c( v as Boolean[], this.&bool )

		return true
	}
}
