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
