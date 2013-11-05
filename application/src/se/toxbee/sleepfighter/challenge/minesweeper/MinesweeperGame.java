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

import se.toxbee.sleepfighter.challenge.minesweeper.MinesweeperBoard.State;
import se.toxbee.sleepfighter.challenge.minesweeper.MinesweeperMove.Action;
import se.toxbee.sleepfighter.utils.geom.Dimension;
import se.toxbee.sleepfighter.utils.geom.Position;

/**
 * MinesweeperGame models a game of minesweeper.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Nov 5, 2013
 */
public class MinesweeperGame {
	/**
	 * Constructs a game given a board.
	 *
	 * @param board the board.
	 */
	public MinesweeperGame( MinesweeperBoard board ) {
		this.board = board;
		this.state = State.PROGRESS;
	}

	/**
	 * Constructs a game given dimension, mineCount and a RNG.
	 *
	 * @param dim the dimension of board.
	 * @param mineCount mine count in board.
	 * @param rng RNG.
	 */
	public MinesweeperGame( Dimension dim, int mineCount, Random rng ) {
		this( new MinesweeperBoard( dim, mineCount, rng ) );
	}

	/**
	 * Returns the board of the game.
	 *
	 * @return the board.
	 */
	public MinesweeperBoard board() {
		return this.board;
	}

	/**
	 * Returns the state of the game.
	 *
	 * @return the state.
	 */
	public State state() {
		return this.state;
	}

	/**
	 * Performs a move in the game.<br/>
	 * Changes the state of the game.
	 *
	 * @param m the move.
	 */
	public void acceptMove( MinesweeperMove m ) {
		if ( this.state == State.PROGRESS ) {
			this.state = board.performMove( m );
		}
	}

	/**
	 * Performs the first move in the game opening the cell at pos.
	 *
	 * @param pos the pos.
	 * @return the move.
	 */
	public MinesweeperMove firstMove( Position pos ) {
		MinesweeperMove move = new MinesweeperMove( pos, Action.NORMAL );
		this.acceptMove( move );
		return move;
	}

	// TODO START

	private void generateBoard(int rows, int cols) {
		
	}

	// TODO END

	private MinesweeperBoard board;
	private State state;
}
