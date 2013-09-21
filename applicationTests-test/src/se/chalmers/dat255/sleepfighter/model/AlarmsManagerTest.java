package se.chalmers.dat255.sleepfighter.model;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.joda.time.DateTime;

import se.chalmers.dat255.sleepfighter.utils.message.Message;
import se.chalmers.dat255.sleepfighter.utils.message.MessageBus;

public class AlarmsManagerTest extends TestCase {

	public void testGetEarliestInfo() {
		// Bootstrap.
		Alarm first = new Alarm(0, 2);
		first.setId( 1 );
		Alarm second = new Alarm(0, 3);
		second.setId( 2 );

		List<Alarm> list = new ArrayList<Alarm>();
		list.add(first);
		list.add( second );

		AlarmsManager manager = new AlarmsManager( list );

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

		AlarmsManager manager = new AlarmsManager( list );

		MessageBus<Message> bus = new MessageBus<Message>();
		manager.setMessageBus(bus);
		
		assertEquals(bus, first.getMessageBus());
		assertEquals(bus, second.getMessageBus());
	}
	
	public void testFireEvent() {
		List<Alarm> list = new ArrayList<Alarm>();
		AlarmsManager manager = new AlarmsManager( list );
		MessageBus<Message> bus = new MessageBus<Message>();
		manager.setMessageBus(bus);
		
		Alarm first = new Alarm(12, 2);
		manager.add(first);
		assertEquals(bus, first.getMessageBus());
		
		Alarm second = new Alarm(12,3);
		manager.set(0, second);
		assertEquals(bus, second.getMessageBus());
	
	}
}