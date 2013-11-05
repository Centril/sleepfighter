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

/**
 * MinesweeperCell models one cell in the game of minesweeper.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Nov 5, 2013
 */
public class MinesweeperCell {
	/**
	 * CellState enumerates the state a Cell is in, in the game of minesweeper.
	 *
	 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
	 * @version 1.0
	 * @since Nov 5, 2013
	 */
	public static enum CellState {
		NOT_CLICKED, FLAG_CLICKED, QUESTION_CLICKED, CLICKED
	};

	public static final int NA = -1;
	public static final int EMPTY = 0;
	public static final int MINE = 9;

	private int value;
	private CellState state;

	/**
	 * Returns the value of the cell.
	 *
	 * @return the value.
	 */
	public int getValue() {
		return this.value;
	}

	/**
	 * Returns the state of the cell.
	 *
	 * @return the cell.
	 */
	public CellState getState() {
		return this.state;
	}

	/**
	 * Returns whether or not the cell is a mine.
	 *
	 * @return true if it is a mine.
	 */
	public boolean isMine() {
		return value == MINE;
	}

	/**
	 * Returns whether or not the cell is empty.
	 *
	 * @return true if it is empty.
	 */
	public boolean isEmpty() {
		return value == EMPTY;
	}
}
