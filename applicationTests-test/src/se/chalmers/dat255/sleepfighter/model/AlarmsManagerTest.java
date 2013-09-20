package se.chalmers.dat255.sleepfighter.model;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.joda.time.DateTime;

import se.chalmers.dat255.sleepfighter.model.AlarmsManager.EarliestInfo;
import se.chalmers.dat255.sleepfighter.utils.message.Message;
import se.chalmers.dat255.sleepfighter.utils.message.MessageBus;

public class AlarmsManagerTest extends TestCase {

	public void testGetEarliestInfo() {
		// Bootstrap.
		Alarm first = new Alarm(0, 2);
		Alarm second = new Alarm(0, 3);

		List<Alarm> list = new ArrayList<Alarm>();
		list.add(first);
		list.add( second );

		AlarmsManager manager = new AlarmsManager( list );

		long now = new DateTime(0,1,1,0,0).getMillis();

		// Test index correctness.
		EarliestInfo info = manager.getEarliestInfo( now );
		assertTrue( info.isReal() );
		assertEquals(0, info.getIndex() );

		second.setTime( 0, 1 );
		info = manager.getEarliestInfo( now );
		assertTrue( info.isReal() );
		assertEquals(1, info.getIndex() );

		// Test isReal() correctness.
		first.setActivated( false );
		second.setActivated( false );
		info = manager.getEarliestInfo( now );
		assertTrue( !info.isReal() );
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