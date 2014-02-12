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

package se.toxbee.sleepfighter.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Set;

import org.junit.Test;

import se.toxbee.sleepfighter.model.time.AlarmTime;
import se.toxbee.sleepfighter.model.time.ExactTime;

import com.google.common.collect.Sets;

public class ExactTimeTest {
	/**
	 * Test method for {@link se.toxbee.sleepfighter.model.time.ExactTime#hashCode()}.
	 */
	@Test
	public void testHashCode() {
		assertEquals( new ExactTime( 0, 0, 0 ).hashCode(), new ExactTime( 0, 0, 0 ).hashCode() );

		// Wrapping test.
		assertEquals( new ExactTime( 24, 60, 60 ).hashCode(), new ExactTime( 0, 0, 0 ).hashCode() );

		assertFalse( new ExactTime( 13, 53, 32 ).hashCode() == new ExactTime( 12, 12, 12 ).hashCode() );
	}

	/**
	 * Test method for {@link se.toxbee.sleepfighter.model.time.ExactTime#ExactTime(int, int)}.
	 */
	@Test
	public void testAlarmTimeIntInt() {
		testAlarmFields( 1, 1, 0, new ExactTime( 1, 1 ) );
	}

	/**
	 * Test method for {@link se.toxbee.sleepfighter.model.time.ExactTime#ExactTime(int, int, int)}.
	 */
	@Test
	public void testAlarmTimeIntIntInt() {
		testAlarmFields( 1, 1, 1, new ExactTime( 1, 1, 1 ) );
	}

	private void testAlarmFields( int h, int m, int s, AlarmTime t ) {
		assertTrue( t.getHour() == h );
		assertTrue( t.getMinute() == m );
		assertTrue( t.getSecond() == s );
	}

	/**
	 * Test method for {@link se.toxbee.sleepfighter.model.time.ExactTime#toCode()}.
	 */
	@Test
	public void testToCode() {
		Set<Integer> unique = Sets.newHashSet();
		for ( int h = 0; h < 24; ++h ) {
			for ( int m = 0; m < 60; ++m ) {
				for ( int s = 0; s < 60; ++s ) {
					ExactTime t1 = new ExactTime( h, m, s );
					Integer packed = t1.toCode();

					testAlarmFields( h, m, s, new ExactTime( packed ) );

					assertTrue( unique.add( packed ) );
				}
			}
		}
	}

	/**
	 * Test method for {@link se.toxbee.sleepfighter.model.time.ExactTime#equals(java.lang.Object)}.
	 */
	@Test
	public void testEqualsObject() {
		assertEquals( new ExactTime( 13, 53, 49 ), new ExactTime( 13, 53, 49 ) );
		assertFalse( new ExactTime( 3, 4, 1 ).equals( new ExactTime( 6, 9, 5 ) ) );
	}

	/**
	 * Test method for {@link se.toxbee.sleepfighter.model.time.ExactTime#asArray(boolean)}.
	 */
	@Test
	public void testAsArray() {
		AlarmTime t = new ExactTime( 16, 1, 2 );
		assertTrue( Arrays.equals( t.asArray( false ), new int[] { 16, 1, 2 } ) );
		assertTrue( Arrays.equals( t.asArray( true ), new int[] { 16, 1 } ) );
	}

	/**
	 * Test method for {@link se.toxbee.sleepfighter.model.AlarmTime#
	 */
	@Test
	public void testGetTimeString() {
		AlarmTime t = new ExactTime( 16, 1, 2 );
		assertEquals( t.getTimeString( false ), "16:01:02" );
		assertEquals( t.getTimeString( true ), "16:01" );
	}
}
