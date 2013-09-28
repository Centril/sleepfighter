package se.chalmers.dat255.sleepfighter.model;

import junit.framework.Assert;
import junit.framework.TestCase;

public class MemoryTest extends TestCase {

	// there should be an exception if the number of cards is odd. 
	public void testOdd()  {
		try {
			new Memory(3,3);
			Assert.fail("Should have thrown IllegalArgumentException");
			
		} catch (IllegalArgumentException e) {
			// success
		}
	
	}
}
