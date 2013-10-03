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
package se.chalmers.dat255.sleepfighter.model;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.joda.time.DateTime;

import se.chalmers.dat255.sleepfighter.utils.message.Message;
import se.chalmers.dat255.sleepfighter.utils.message.MessageBus;

public class AlarmListTest extends TestCase {

	public void testGetEarliestInfo() {
		// Bootstrap.
		Alarm first = new Alarm(0, 2);
		first.setId( 1 );
		first.setActivated(true);
		Alarm second = new Alarm(0, 3);
		second.setId( 2 );
		second.setActivated(true);

		List<Alarm> list = new ArrayList<Alarm>();
		list.add(first);
		list.add( second );

		AlarmList manager = new AlarmList( list );

		long now = new DateTime(0,1,1,0,0).getMillis();

		// Test index correctness.
		AlarmTimestamp info = manager.getEarliestAlarm( now );
		assertTrue( info != AlarmTimestamp.INVALID );
		assertEquals( info.getAlarm(), first );

		second.setTime( 0, 1 );
		info = manager.getEarliestAlarm( now );
		assertTrue( info != AlarmTimestamp.INVALID );
		assertEquals( info.getAlarm(), second );

		// Test isReal() correctness.
		first.setActivated( false );
		second.setActivated( false );
		info = manager.getEarliestAlarm( now );
		assertTrue( info == AlarmTimestamp.INVALID );
	}

	public void testSetMessageBus(  ) {
		Alarm first = new Alarm(12, 2);
		Alarm second = new Alarm(12, 3);

		List<Alarm> list = new ArrayList<Alarm>();
		list.add(first);
		list.add( second );

		AlarmList manager = new AlarmList( list );

		MessageBus<Message> bus = new MessageBus<Message>();
		manager.setMessageBus(bus);
		
		assertEquals(bus, first.getMessageBus());
		assertEquals(bus, second.getMessageBus());
	}
	
	public void testFireEvent() {
		List<Alarm> list = new ArrayList<Alarm>();
		AlarmList manager = new AlarmList( list );
		MessageBus<Message> bus = new MessageBus<Message>();
		manager.setMessageBus(bus);
		
		Alarm first = new Alarm(12, 2);
		manager.add(first);
		assertEquals(bus, first.getMessageBus());
		
		Alarm second = new Alarm(12,3);
		manager.set(0, second);
		assertEquals(bus, second.getMessageBus());
	
	}
	
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

	public void testFindLowestUnnamedPlacement() {
		AlarmList list = new AlarmList();

		testUnnamedPart( list, 1 );

		try {
			Alarm first = new Alarm(0, 0);
			list.add( first );
			list.add( first.clone() );
			list.add( first.clone() );
			list.add( first.clone() );
		} catch ( CloneNotSupportedException e ) {
			fail(e.getLocalizedMessage());
		}

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
	
	public void setPresetAlarm() {
		
		List<Alarm> l = new ArrayList<Alarm>();
		Alarm a1 = new Alarm();
		Alarm a2 = new Alarm();
		Alarm a3 = new Alarm();
		l.add(a1);
		l.add(a2);
		l.add(a3);
		
		AlarmList list = new AlarmList(l);
		
		assertEquals(a2, list.getPresetAlarm());
	}
}
