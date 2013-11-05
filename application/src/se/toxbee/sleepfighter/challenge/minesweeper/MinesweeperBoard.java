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
package se.toxbee.sleepfighter.challenge.minesweeper;

import java.util.Random;

import se.toxbee.sleepfighter.utils.geom.Dimension;
import se.toxbee.sleepfighter.utils.geom.Position;

/**
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Nov 5, 2013
 */
public class MinesweeperBoard {
	/**
	 * State enumerates the state a game of minesweeper is in.
	 *
	 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
	 * @version 1.0
	 * @since Nov 5, 2013
	 */
	public static enum State {
		PROGRESS, WON, LOST
	}

	/* --------------------------------
	 * Public interface.
	 * --------------------------------
	 */

	/**
	 * Returns the dimension of board.
	 *
	 * @return the dimension.
	 */
	public Dimension dimension() {
		return this.dim;
	}

	/**
	 * Returns whether or not the board has been generated.
	 *
	 * @return true if it has.
	 */
	public boolean isGenerated() {
		return this.generated;
	}

	// TODO START.

	/**
	 * Constructs a board given a dim, mineCount and rng.
	 *
	 * @param dim the dimension of the board.
	 * @param mineCount the count of mines in board.
	 * @param rng RNG.
	 */
	public MinesweeperBoard( Dimension dim, int mineCount, Random rng ) {
	}

	public State performMove( MinesweeperMove move ) {
		return null;
	}

	public MinesweeperCell[] getGrid() {
		return null;
	}

	MinesweeperCell getCell( int row, int col ) {
		return null;
	}

	public Position posLoc( int position ) {
		return null;	
	}

	public int locPos( MinesweeperMove move) {
		return 0;		
	}

	public int locPos( Position pos) {
		return 0;		
	}

	public int locPos( int col, int row) {
		return row;	
	}

	public boolean isValidPos( Position pos) {
		return false;
	}

	public boolean isValidPos( int col, int row) {
		return false;
	}

	public State resetToGenerated( MinesweeperMove initial ) {
		return null;
	}

	/* --------------------------------
	 * Private methods.
	 * --------------------------------
	 */
	private void generateGrid( MinesweeperMove move) {
	}

	private int openEmptySquares( Position position) {
		return 0;
	}

	private State expandSquares( Position position) {
		return null;
	}

	// TODO END.

	/* --------------------------------
	 * Fields.
	 * --------------------------------
	 */

	private MinesweeperCell[] grid;
	private Dimension dim;
	private int mines, squaresLeft;

	private boolean generated;

	private Random rng;
}
