package se.chalmers.dat255.sleepfighter.model;

import java.util.Calendar;
import java.util.GregorianCalendar;

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
}
