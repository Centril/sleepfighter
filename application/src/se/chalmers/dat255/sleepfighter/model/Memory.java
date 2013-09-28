package se.chalmers.dat255.sleepfighter.model;

// class for modelling a memory game.
// the cards are zero-indexed. 
public class Memory {
	
	private final int rows;
	private final int cols;
	
	private final static int UNOCCUPIED = -1;
	
	// every pair of cards is assigned an unique integer.
	int[][] cards;
	
	
	Memory(final int rows, final int cols) {
		this.rows = rows;
		this.cols = cols; 
		
		cards = new int[rows][cols];
		
		placeOutCards();
	}
	
	private int getNumCards() {
		return rows * cols;
	}
	
	private void placeOutCards() {
		// all unassigned spaces we'll set to -1
		for(int i = 0; i < rows; ++i) {
			for(int j = 0; j < cols; ++j) {
				cards[i][j] = UNOCCUPIED;
			}
		}
		
		
		int assignedCards = 0;
		while(assignedCards !=getNumCards()) {
			
			
			// find two empty spaces were we can place out a pair
			int card1row;
			int card1col;
			
			int card2col;
			int card2row;
		
		}
	}
	
	
	private boolean isUnoccupied(int row, int col) {
		return cards[row][col] == UNOCCUPIED;
	}
}
