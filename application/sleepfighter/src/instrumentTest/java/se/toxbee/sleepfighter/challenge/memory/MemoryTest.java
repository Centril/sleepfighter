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

package se.toxbee.sleepfighter.challenge.memory;

import junit.framework.Assert;
import junit.framework.TestCase;

import se.toxbee.sleepfighter.utils.debug.Debug;

public class MemoryTest extends TestCase {

	// there should be an exception if the number of cards is odd. 
	public void testOdd()  {
		try {
			new Memory(3,3);
			Assert.fail("Should have thrown IllegalArgumentException");
			
		} catch (IllegalArgumentException e) {
			// success
		}
		
		Memory mem = new Memory(4,3);
		assertEquals(4, mem.getRows());
		assertEquals(3, mem.getCols());
		assertEquals(12, mem.getNumCards());
		assertEquals(6, mem.getNumPairs());
	}
	
	public boolean testPlaceOutCards(int rows, int cols) {
		Memory mem = new Memory(rows, cols);
		Debug.d(mem.toString());
		
		// Requirement 1: there should not be any onoccupied spaces.
		for(int i = 0; i < rows; ++i) {
			for(int j = 0; j < cols; ++j) {
				if(mem.isUnoccupied(i, j)) {
					return false;
				}
			}
		}
		
		// Requirement 2: There are exactly two of every card type(it is memory after all)
		// And every card type is represented by a positive integer. 
		
		int[] cardCount = new int[mem.getNumPairs()];
		
		// count the number of every card type
		for(int i = 0; i < rows; ++i) {
			for(int j = 0; j < cols; ++j) {
				cardCount[mem.getCard(i, j)]++;
			}
		}
		
		for(int i = 0; i < cardCount.length; ++i) {
			if(cardCount[i] != 2)
				return false;
		}
		
		return true;
	}
	
	public void testPlaceOutCards() {
		/*
		 * Since placeOutCards is based on Random numbers, it is quite difficult to rigorously test.
		 * But the result of its calculations must fulfill certain requirements for it to 
		 * be considered a success, so we will check for these requirements for a couple hundred 
		 * randomly generated card placements. 
		 */
		
		int TESTS = 30;
		for(int i = 0; i < TESTS; ++i) {
			
			testPlaceOutCards(6,6);
		}
	}
	
	public void testGetCard() {
		
		
		Memory mem = new Memory(4, 3);
	
		Debug.d(mem.toString());
		
		// we already knows that getCard(row, col) works. We are testing getCard(i)
		
		assertEquals(mem.getCard(0, 2), mem.getCard(2));
		assertEquals(mem.getCard(1, 1), mem.getCard(4));

		// the very last card
		assertEquals(mem.getCard(3, 2), mem.getCard(11));
	}
}
