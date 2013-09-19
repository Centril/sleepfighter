package se.chalmers.dat255.sleepfighter.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import se.chalmers.dat255.sleepfighter.model.AlarmsManager.EarliestInfo;
import se.chalmers.dat255.sleepfighter.utils.message.Message;
import se.chalmers.dat255.sleepfighter.utils.message.MessageBus;

import junit.framework.TestCase;

public class AlarmsManagerTest extends TestCase {

	public void testGetEarliestInfo() {
		// Bootstrap.
		Alarm first = new Alarm(12, 2);
		Alarm second = new Alarm(12, 3);

		List<Alarm> list = new ArrayList<Alarm>();
		list.add(first);
		list.add( second );

		AlarmsManager manager = new AlarmsManager( list );

		Calendar now = new GregorianCalendar();
		now.set( Calendar.HOUR, 12 );
		now.set( Calendar.MINUTE, 0 );

		// Test index correctness.
		EarliestInfo info = manager.getEarliestInfo( now );
		assertTrue( info.isReal() );
		assertEquals(0, info.getIndex() );

		second.setTime( 12, 1 );
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
}