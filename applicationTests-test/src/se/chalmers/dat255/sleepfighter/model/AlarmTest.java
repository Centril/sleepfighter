package se.chalmers.dat255.sleepfighter.model;

import junit.framework.Assert;
import junit.framework.TestCase;

public class AlarmTest extends TestCase {


	public void testConstructor() {
		Alarm alarm = new Alarm(4, 3);
		
		assertEquals(4, alarm.getHour());
		assertEquals(3, alarm.getMinute());
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
}
