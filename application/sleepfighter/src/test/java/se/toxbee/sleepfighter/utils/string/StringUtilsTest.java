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

package se.toxbee.sleepfighter.utils.string;

import static org.junit.Assert.*;

import org.junit.Test;

public class StringUtilsTest {
	@Test
	public void testCastUpper() {
		String subject = "Hello World";
		String result = "HELLO WORLD";

		assertEquals( StringUtils.castUpper( subject ), result );
	}

	@Test
	public void testCastLower() {
		String subject = "Hello World";
		String result = "hello world";

		assertEquals( StringUtils.castLower( subject ), result );
	}

	@Test
	public void testCapitalizeFirst() {
		String subject = "hello world";
		String result = "Hello world";
		assertEquals( result, StringUtils.capitalizeFirst( subject ) );
	}

	@Test
	public void testReadHexString() {
		String subjects[] = new String[] { "FF", "#FF", "0xFF" };
		int result = 0xFF;

		for ( String s : subjects ) {
			assertEquals( result, StringUtils.readHexString( s ) );
		}
	}

	@Test
	public void testJoinTime() {
		int[] subject = new int[] { 1, 31 };
		String result = "01:31";

		assertEquals( result, StringUtils.joinTime( subject ) );
	}

	@Test
	public void testGetDigitsIn() {
		String s1 = "";
		assertEquals( StringUtils.getDigitsIn( s1 ), -1 );

		String s2 = "12A3bc4";
		assertEquals( StringUtils.getDigitsIn( s2 ), 1234 );
	}
}
