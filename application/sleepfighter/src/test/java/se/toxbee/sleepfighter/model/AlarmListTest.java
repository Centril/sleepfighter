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
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Test;

import se.toxbee.sleepfighter.model.time.ExactTime;
import se.toxbee.commons.message.Message;
import se.toxbee.commons.message.MessageBus;

public class AlarmListTest {
	@Test
	public void testGetEarliestInfo() {
		// Bootstrap.
		Alarm first = new Alarm();
		first.setTime( new ExactTime( 0, 2 ) );
		first.setId( 1 );
		first.setActivated(true);
		Alarm second = new Alarm();
		second.setTime(  new ExactTime( 0, 3 ) );
		second.setId( 2 );
		second.setActivated(true);

		List<Alarm> list = new ArrayList<Alarm>();
		list.add(first);
		list.add( second );

		AlarmList manager = new AlarmList( list );

		long now = new DateTime( 0, 1, 1, 0, 0 ).getMillis();

		// Test index correctness.
		AlarmTimestamp info = manager.getEarliestAlarm( now );
		assertTrue( info != AlarmTimestamp.INVALID );
		assertEquals( info.getAlarm(), first );

		second.setTime( new ExactTime( 0, 1 ) );
		info = manager.getEarliestAlarm( now );
		assertTrue( info != AlarmTimestamp.INVALID );
		assertEquals( info.getAlarm(), second );

		// Test isReal() correctness.
		first.setActivated( false );
		second.setActivated( false );
		info = manager.getEarliestAlarm( now );
		assertTrue( info == AlarmTimestamp.INVALID );
	}

	@Test
	public void testSetMessageBus() {
		Alarm first = new Alarm();
		first.setTime( new ExactTime( 12, 2 ) );
		Alarm second = new Alarm();
		second.setTime( new ExactTime( 12, 3 ) );

		List<Alarm> list = new ArrayList<Alarm>();
		list.add(first);
		list.add( second );

		AlarmList manager = new AlarmList( list );

		MessageBus<Message> bus = new MessageBus<Message>();
		manager.setMessageBus(bus);
		
		assertEquals(bus, first.getMessageBus());
		assertEquals(bus, second.getMessageBus());
	}

	@Test
	public void testFireEvent() {
		List<Alarm> list = new ArrayList<Alarm>();
		AlarmList manager = new AlarmList( list );
		MessageBus<Message> bus = new MessageBus<Message>();
		manager.setMessageBus(bus);
		
		Alarm first = new Alarm();
		first.setTime( new ExactTime( 12, 2 ) );
		manager.add(first);
		assertEquals(bus, first.getMessageBus());
		
		Alarm second = new Alarm();
		second.setTime( new ExactTime( 12, 3 ) );
		manager.set(0, second);
		assertEquals(bus, second.getMessageBus());
	
	}

	@Test
	public void testGetById() {
		AlarmList alarmList = new AlarmList();
		
		// Test with no elements in list
		assertEquals(null, alarmList.getById(0));
		
		// Add one element to list
		Alarm alarm1 = new Alarm();
		alarm1.setId(5);
		alarmList.add(alarm1);
		
		// Test with 1 element in list
		assertEquals(alarm1, alarmList.getById(5));
		
		// Add a second element
		Alarm alarm2 = new Alarm();
		alarm2.setId(7);
		alarmList.add(alarm2);
		
		// Look for both IDs when both in list
		assertEquals(alarm1, alarmList.getById(5));
		assertEquals(alarm2, alarmList.getById(7));
		
		// Test looking for ID that no alarm in list has
		assertEquals(null, alarmList.getById(6));
	}

	@Test
	public void testFindLowestUnnamedPlacement() {
		AlarmList list = new AlarmList();

		testUnnamedPart( list, 1 );

		Alarm first = new Alarm();
		first.setTime( new ExactTime( 0, 0 ) );
		list.add(first);
		list.add(new Alarm(first));
		list.add(new Alarm(first));
		list.add(new Alarm(first));

		// tests for whole list.
		for ( int i = 0; i < 4; i++ ) {
			list.get( i ).setUnnamedPlacement( testUnnamedPart( list, i + 1 ) );
		}

		// name the second alarm and test, slot 2 should be empty and be returned now.
		list.get( 1 ).setName( "test-name" );
		testUnnamedPart( list, 2 );
	}

	private int testUnnamedPart( AlarmList list, int test ) {
		int lowest = list.findLowestUnnamedPlacement();
		assertTrue( lowest == test );
		return lowest;
	}
}