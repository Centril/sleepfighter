package se.toxbee.sleepfighter.utils;

import junit.framework.TestCase;
import se.toxbee.sleepfighter.utils.reflect.ReflectionUtil;

public class ReflectionUtilTest extends TestCase {
	public void testMakeArray() {
		Integer[] subject = ReflectionUtil.makeArray( Integer.class, 5 );
		assertSame( subject.length, 5 );
	}

	public void testArrayClass() {
		Integer[] subject = new Integer[5];
		assertEquals( Integer.class, ReflectionUtil.arrayClass( subject ) );
	}

	private static interface TestInterface {}
	private static class TestClass1 implements TestInterface {}
	private static class TestClass2 {}

	public void testAsSubclass() {
		assertFalse( null == ReflectionUtil.asSubclass( TestClass1.class, TestInterface.class ) );
		assertTrue( null == ReflectionUtil.asSubclass( TestClass2.class, TestInterface.class ) );
	}
}
