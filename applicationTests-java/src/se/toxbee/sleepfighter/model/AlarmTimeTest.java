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
package se.toxbee.sleepfighter.model;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Set;

import se.toxbee.sleepfighter.model.AlarmTime;

import com.google.common.collect.Sets;

public class AlarmTimeTest {
	/**
	 * Test method for {@link se.toxbee.sleepfighter.model.AlarmTime#hashCode()}.
	 */
	@Test
	public void testHashCode() {
		assertEquals( new AlarmTime( 0, 0, 0 ).hashCode(), new AlarmTime( 0, 0, 0 ).hashCode() );

		// Wrapping test.
		assertEquals( new AlarmTime( 24, 60, 60 ).hashCode(), new AlarmTime( 0, 0, 0 ).hashCode() );

		assertFalse( new AlarmTime( 13, 53, 32 ).hashCode() == new AlarmTime( 12, 12, 12 ).hashCode() );
	}

	/**
	 * Test method for {@link se.toxbee.sleepfighter.model.AlarmTime#AlarmTime(int, int)}.
	 */
	@Test
	public void testAlarmTimeIntInt() {
		testAlarmFields( 1, 1, 0, new AlarmTime( 1, 1 ) );
	}

	/**
	 * Test method for {@link se.toxbee.sleepfighter.model.AlarmTime#AlarmTime(int, int, int)}.
	 */
	@Test
	public void testAlarmTimeIntIntInt() {
		testAlarmFields( 1, 1, 1, new AlarmTime( 1, 1, 1 ) );
	}

	private void testAlarmFields( int h, int m, int s, AlarmTime t ) {
		assertTrue( t.getHour() == h );
		assertTrue( t.getMinute() == m );
		assertTrue( t.getSecond() == s );
	}

	/**
	 * Test method for {@link se.toxbee.sleepfighter.model.AlarmTime#pack()}.
	 */
	@Test
	public void testPack() {
		Set<Integer> unique = Sets.newHashSet();
		for ( int h = 0; h < 24; ++h ) {
			for ( int m = 0; m < 60; ++m ) {
				for ( int s = 0; s < 60; ++s ) {
					AlarmTime t1 = new AlarmTime( h, m, s );
					Integer packed = t1.pack();

					testAlarmFields( h, m, s, new AlarmTime( packed ) );

					assertTrue( unique.add( packed ) );
				}
			}
		}
	}

	/**
	 * Test method for {@link se.toxbee.sleepfighter.model.AlarmTime#equals(java.lang.Object)}.
	 */
	@Test
	public void testEqualsObject() {
		assertEquals( new AlarmTime( 13, 53, 49 ), new AlarmTime( 13, 53, 49 ) );
		assertFalse( new AlarmTime( 3, 4, 1 ).equals( new AlarmTime( 6, 9, 5 ) ) );
	}

	/**
	 * Test method for {@link se.toxbee.sleepfighter.model.AlarmTime#asArray(boolean)}.
	 */
	@Test
	public void testAsArray() {
		AlarmTime t = new AlarmTime( 16, 1, 2 );
		assertTrue( Arrays.equals( t.asArray( false ), new int[] { 16, 1, 2 } ) );
		assertTrue( Arrays.equals( t.asArray( true ), new int[] { 16, 1 } ) );
	}

	/**
	 * Test method for {@link se.toxbee.sleepfighter.model.AlarmTime#
	 */
	@Test
	public void testGetTimeString() {
		AlarmTime t = new AlarmTime( 16, 1, 2 );
		assertEquals( t.getTimeString( false ), "16:01:02" );
		assertEquals( t.getTimeString( true ), "16:01" );
	}
}
