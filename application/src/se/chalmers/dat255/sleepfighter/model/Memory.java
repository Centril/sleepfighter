/*******************************************************************************
 * Copyright (c) 2013 See AUTHORS file.
 * 
 * This file is part of SleepFighter.
 * 
 * SleepFighter is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * SleepFighter is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with SleepFighter. If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package se.chalmers.dat255.sleepfighter.model;

import java.util.Arrays;
import java.util.Random;

// class for modelling a memory game.
// the cards are zero-indexed. 
public class Memory {
	
	private final int rows;
	private final int cols;
	
	public final static int UNOCCUPIED = -1;
	
	private final static Random rng = new Random();
	
	// every pair of cards is assigned an unique integer.
	int[][] cards;
	
	private boolean isOdd(int n) {
		return (n % 2) == 1; 
	}
	
	/**
	 * The numbers of cards must be even.
	 * Therefore, both rows and cols must not be odd
	 * (an odd number times an odd number is an odd number)
	 */
	public Memory(final int rows, final int cols) {
		
		if(isOdd(rows) && isOdd(cols)) {
			throw new IllegalArgumentException("The numbers of cards must be even");
		}
		
		this.rows = rows;
		this.cols = cols; 
		
		cards = new int[rows][cols];
		
		placeOutCards();
	}
	
	public int getNumCards() {
		return rows * cols;
	}
	
	public int getNumPairs() {
		// because the number of cards is guaranteed to be even.
		return getNumCards() / 2;
	}
	
	
	public int getRows() {
		return rows;
	}
	
	public int getCols() {
		return cols; 
	}
	
	// each pair of cards is assigned an unique int. 
	public int getCard(int row, int col) {
		return cards[row][col];
	}
	
	// access the card using array indexing instead(zero-based indexing)
	public int getCard(int index) {
		int row = (int)Math.floor(index / cols);
		int col = index - row * cols;
		return this.getCard(row, col);
	}
	
	private void placeOutCards() {
		// all unassigned spaces we'll set to -1
		for(int i = 0; i < rows; ++i) {
			for(int j = 0; j < cols; ++j) {
				cards[i][j] = UNOCCUPIED;
			}
		}
		
		int nextCardNumber = 0;
		
		while(nextCardNumber != this.getNumPairs()) {
			
			// place out the first card in the pair
			int[] card1pos = new int[2];
			findUnoocupiedPosition(card1pos);
			cards[card1pos[0]][card1pos[1]] = nextCardNumber;
			
			// now the second card in the pair
			int[] card2pos = new int[2];
			findUnoocupiedPosition(card2pos);
			cards[card2pos[0]][card2pos[1]] = nextCardNumber;
			
			++nextCardNumber;
		}
	}
	
	// the position is returned by reference by the argument pos.
	// pos[0] is row, pos[1] is col.
	private void findUnoocupiedPosition(int[] pos) {
		do {
			pos[0] = rng.nextInt(this.getRows());
			pos[1] = rng.nextInt(this.getCols());
		} while(!isUnoccupied(pos[0], pos[1]));
	}
	
	private boolean isUnoccupied(int row, int col) {
		return cards[row][col] == UNOCCUPIED;
	}
	
	public String toString() {
		String s = "";
		s += "{";
		
        for (int[] row : cards) {
        	s += Arrays.toString(row) + "\n";
        }
        
        s += "}";
        
        return s;
	}
}
