package se.chalmers.dat255.sleepfighter.model;

import java.util.Calendar;
import java.util.GregorianCalendar;

import net.engio.mbassy.listener.Handler;

import se.chalmers.dat255.sleepfighter.activities.MainActivity;
import se.chalmers.dat255.sleepfighter.debug.Debug;
import se.chalmers.dat255.sleepfighter.model.Alarm.DateChangeEvent;
import se.chalmers.dat255.sleepfighter.model.Alarm.MetaChangeEvent;
import se.chalmers.dat255.sleepfighter.utils.message.Message;
import se.chalmers.dat255.sleepfighter.utils.message.MessageBus;

import junit.framework.Assert;
import junit.framework.TestCase;

public class AlarmTest extends TestCase {


	public void testConstructor() {
		Alarm alarm = new Alarm(4, 3);
		
		assertEquals(4, alarm.getHour());
		assertEquals(3, alarm.getMinute());
	}
	
	public void testConstructorExceptions() {
		// invalid hour
		try {
			new Alarm(-1, 3);
			Assert.fail("Should have thrown IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			// success
		}
		try {
			new Alarm(24, 4);
			Assert.fail("Should have thrown IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			// success
		}

		// invalid minute
		try {
			new Alarm(4, -1);
			Assert.fail("Should have thrown IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			// success
		}
		try {
			new Alarm(4, 60);
			Assert.fail("Should have thrown IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			// success
		}
	}
	
	public void testSetTime() {
		Alarm alarm = new Alarm(4, 3);
		alarm.setTime(3, 4);
		
		assertEquals(3, alarm.getHour());
		assertEquals(4, alarm.getMinute());
	}
	
	public class Subscriber3 {
		
		public boolean passed = false;
		public Alarm alarm = new Alarm(1,2);
		
		@Handler
		public void handleMetaChange( DateChangeEvent evt ) {
			passed = (evt.getModifiedField() == Alarm.Field.TIME) && (alarm == evt.getAlarm());
		}
		
	}
	
	public void testSetTimeMessage() {
		Subscriber3 sub = new Subscriber3();

		MessageBus<Message> bus = new MessageBus<Message>();
		bus.subscribe( sub );

		sub.alarm.setMessageBus(bus);
		sub.alarm.setTime(8, 2);
		assertTrue(sub.passed);
	}
	
	public void testSetTimeExceptions() {
		Alarm alarm = new Alarm(4, 3);
		
		// invalid hour
		try {
			alarm.setTime(-1, 4);
			Assert.fail("Should have thrown IllegalArgumentException");
		} catch(IllegalArgumentException e) {
			// success
		}
		try {
			alarm.setTime(24, 4);
			Assert.fail("Should have thrown IllegalArgumentException");
		} catch(IllegalArgumentException e) {
			// success
		}
	}
	
	public void testEquals() {
		Alarm alarm1 = new Alarm(3,2);
		Alarm alarm2 = new Alarm(3,2);
		
		assertEquals(alarm1, alarm2);
		
		alarm2.setTime(3, 1);
		assertFalse(alarm1.equals(alarm2));
		
		// same reference test
		Alarm alarmCopy = alarm1;
		assertEquals(alarmCopy, alarm1);
		
		// compare with null
		assertFalse(alarm1.equals(null));
	}
	
	public void testCanHappen() {
		Alarm alarm = new Alarm(3, 1);
		alarm.setActivated(false);
		// does it respect isActivated?
		assertFalse(alarm.canHappen());
		
		alarm.setActivated(true);
		
		// does it respect enabledDays?
		 boolean[] enabledDays =  { false, false, false, false, false, false, true };
		 alarm.setEnabledDays(enabledDays);
		 assertTrue(alarm.canHappen());
		 
		 boolean[] enabledDays2 =  { false, false, false, false, false, false, false };
		 alarm.setEnabledDays(enabledDays2);
		 assertFalse(alarm.canHappen());
	}
	
	public void testGetNextMillis() {
		Alarm alarm = new Alarm(1, 1);
		
		// does it respect canHappen?
		alarm.setActivated(false);
		assertEquals(Alarm.NEXT_NON_REAL, alarm.getNextMillis());
		
		alarm.setActivated(true);
		// day is Wednesday. 
		// alarm is one minute after this time
		Calendar cal = new GregorianCalendar(2013, 8, 18, 1, 0);
		
		Calendar cal2 = (Calendar) cal.clone();
		cal2.add(Calendar.MINUTE, 1);
		
		assertEquals(cal2.getTimeInMillis(), alarm.getNextMillis(cal));
	}
	
	public class Subscriber1 {
		
		public boolean passed = false;
		public Alarm alarm = new Alarm(1,2);
		
		@Handler
		public void handleMetaChange( MetaChangeEvent evt ) {
			passed = (evt.getModifiedField() == Alarm.Field.ID) && (alarm == evt.getAlarm());
		}
		
	}
	
	public void testSetId() {
		Subscriber1 sub = new Subscriber1();

		MessageBus<Message> bus = new MessageBus<Message>();
		bus.subscribe( sub );

		sub.alarm.setMessageBus(bus);
		sub.alarm.setId(2);
		assertTrue(sub.passed);
	}	

	public class Subscriber2 {
		
		public boolean passed = false;
		public Alarm alarm = new Alarm(1,2);
		
		@Handler
		public void handleMetaChange( MetaChangeEvent evt ) {
			passed = (evt.getModifiedField() == Alarm.Field.NAME) && (alarm == evt.getAlarm());
		}
		
	}
	
	public void testSetName() {
		Subscriber2 sub = new Subscriber2();

		MessageBus<Message> bus = new MessageBus<Message>();
		bus.subscribe( sub );

		sub.alarm.setMessageBus(bus);
		sub.alarm.setName("hell world");
		assertTrue(sub.passed);
	}	
	
	public class Subscriber4 {
		
		public boolean passed = false;
		public Alarm alarm = new Alarm(1,2);
		
		@Handler
		public void handleMetaChange( DateChangeEvent evt ) {
			passed = (evt.getModifiedField() == Alarm.Field.ACTIVATED) && (alarm == evt.getAlarm());
		}
		
	}
	
	public void testSetActivated() {
		Subscriber4 sub = new Subscriber4();

		MessageBus<Message> bus = new MessageBus<Message>();
		bus.subscribe( sub );

		sub.alarm.setMessageBus(bus);
		sub.alarm.setActivated(true);
		assertTrue(sub.passed);
	}	

	public class Subscriber5 {
		
		public boolean passed = false;
		public Alarm alarm = new Alarm(1,2);
		
		@Handler
		public void handleMetaChange( DateChangeEvent evt ) {
			passed = (evt.getModifiedField() == Alarm.Field.ENABLED_DAYS) && (alarm == evt.getAlarm());
		}
		
	}
	
	public void testSetEnabledDays() {
		Subscriber5 sub = new Subscriber5();

		MessageBus<Message> bus = new MessageBus<Message>();
		bus.subscribe( sub );

		sub.alarm.setMessageBus(bus);
		boolean[] enabledDays = { true, true, true, true, true, true, true };
		
		sub.alarm.setEnabledDays(enabledDays);
		assertTrue(sub.passed);
	}		
	
}
	
