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

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.Random;

import se.toxbee.sleepfighter.challenge.minesweeper.MinesweeperCell.CellState;
import se.toxbee.sleepfighter.challenge.minesweeper.MinesweeperMove.Action;
import se.toxbee.sleepfighter.utils.collect.Grid;
import se.toxbee.sleepfighter.utils.geom.Direction;
import se.toxbee.sleepfighter.utils.geom.FinalPosition;
import se.toxbee.sleepfighter.utils.geom.MutablePosition;
import se.toxbee.sleepfighter.utils.geom.Position;

import com.google.common.collect.Lists;

/**
 * MinesweeperBoard models the board/grid in a minesweeper game.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Nov 5, 2013
 */
public class MinesweeperBoard extends Grid<MinesweeperCell> {
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
	 * Returns whether or not the board has been generated.
	 *
	 * @return true if it has.
	 */
	public boolean isGenerated() {
		return this.generated;
	}

	/**
	 * Returns the index for a move.
	 *
	 * @param move the move.
	 * @return the index.
	 */
	public int index( MinesweeperMove move ) {
		return this.index( move.getPosition() );
	}

	/**
	 * Constructs a board given a config and rng.
	 *
	 * @param config the config to get dimensions and mine-count from.
	 * @param rng RNG.
	 */
	public MinesweeperBoard( MinesweeperConfig config, Random rng ) {
		super( MinesweeperCell.class, config.dim() );

		this.mines = config.mineCount();
		this.rng = rng;
	}

	/**
	 * Performs move in the board.
	 *
	 * @param move the move.
	 * @return the resulting state of board.
	 */
	public State performMove( MinesweeperMove move ) {
		if ( !this.isGenerated() ) {
			this.generateGrid( move );
		}

		State resultingState = State.PROGRESS;

		Position position = move.getPosition();

		if ( this.inBounds( position ) ) {
			MinesweeperCell cell = this.get( position );
			CellState cellState = cell.getState();

			if ( cellState == CellState.NOT_CLICKED) {
				switch ( move.getAction() ) {
				case NORMAL:
					squaresLeft -= this.openEmptyCells( position );

					if (cell.getState() == CellState.NOT_CLICKED) {
						cell.setState( CellState.CLICKED );
						squaresLeft--;
					}

					// Check to see if you won or lost on this click.
					if ( cell.isMine() ) {
						// If you clicked a mine then you lost
						resultingState = State.LOST;
					} else if (squaresLeft == mines) {
						// If you did not click a mine and only mines are left on the board then you won!
						resultingState = State.WON;
					}
					break;

				case FLAG:
					if (cell.getState() != CellState.CLICKED) {
						cell.setState( CellState.FLAG_CLICKED );
					}
					break;

				case QUESTION:
					if (cell.getState() != CellState.CLICKED) {
						cell.setState( CellState.QUESTION_CLICKED );
					}
					break;

				case EXPAND:
					if (cell.getState() != CellState.CLICKED) {
						resultingState = this.expandCells( position );
					}
					break;
				}
			}
		} else {
			throw new IllegalArgumentException( "Position of given move is not in bounds." );
		}

		return resultingState;
	}

	/**
	 * Resets the board to its generated state.
	 *
	 * @param initial the initial move.
	 * @return the state of the board: {@link State#PROGRESS}.
	 */
	public State resetToGenerated( MinesweeperMove initial ) {
		// First reset the entire grid.
		squaresLeft = this.dim().cross( 1 );
		for ( MinesweeperCell c : this ) {
			c.reset();
		}

		// Now re-do the initial move.
		this.performMove( initial );

		return State.PROGRESS;
	}

	/* --------------------------------
	 * Private methods.
	 * --------------------------------
	 */

	/**
	 * Generates the grid of cells given an initial move.
	 *
	 * @param move the initial move.
	 */
	private void generateGrid( MinesweeperMove move ) {
		this.squaresLeft = this.dim().cross( 1 );
		Position initial = move.getPosition();

		// Generate the board
		for ( int i = 0; i < this.squaresLeft; ++i ) {
			this.set( i, new MinesweeperCell() );
		}

		// Pick the Mines
		{
			// Shuffle the positions so that we can pick the ones that we want to be the mines.
			List<Position> cellPositions = Lists.newArrayListWithCapacity( this.squaresLeft );
			Position current = Direction.NONE.delta();
			for ( int i = 0; i < this.squaresLeft; ++i ) {
				Position last = current;

				current = current.getX() + 1 == this.dim().width()
						? new FinalPosition( 0, current.y() + 1 )
						: current.move( Direction.EAST );

				// Skip adjacent to initial positions.
				if ( !last.isAdjacent( initial ) ) {
					cellPositions.add( last );
				}
			}
			Collections.shuffle( cellPositions, this.rng );

			// Select mines from top.
			for ( int c = 0; c < mines; ++c ) {
				this.get( cellPositions.get( c ) ).setValue( MinesweeperCell.MINE );
			}
		}

		// Calculate the numbers of the cells.
		Direction[] directions = Direction.all();
		for ( int row = 0; row < this.dim().height(); ++row ) {
			for ( int col = 0; col < this.dim().width(); ++col ) {
				MinesweeperCell cell = this.get( col, row );

				if ( !cell.isMine() ) {
					int count = MinesweeperCell.EMPTY;

					Position testPos = new MutablePosition( col, row );

					for ( Direction dir : directions ) {
						testPos = testPos.move( dir );
						if ( this.inBounds( testPos ) && this.get( testPos ).isMine() ) {
							++count;
						}
					}

					cell.setValue( count );
				}
			}
		}

		// Now finally state that the board has been generated
		generated = true;
	}

	/**
	 * Opens any empty squares starting at position.<br/>
	 * Runs a queue based flood fill algorithm.
	 *
	 * @param position initial position.
	 * @return the number of opened squares.
	 */
	private int openEmptyCells( Position pos ) {
		int opened = 0;

		// Setup the queue.
		Direction[] directions = Direction.all();
		Queue<Position> queue = new ArrayDeque<Position>( directions.length );

		// Feed the queue.
		queue.add( this.immutablePosition( pos ) );

		// Iteratively open all empty squares.
		// Performs a flood-fill type of algorithm, http://en.wikipedia.org/wiki/Flood_fill
		Position curr;
		while( (curr = queue.poll()) != null ) {
			int index = this.index( curr );

			if ( this.isValid( index ) ) {
				MinesweeperCell cell = this.get( index );

				if ( cell.getState() != CellState.CLICKED ) {
					cell.setState( CellState.CLICKED );
					opened++;

					if ( cell.isEmpty() ) {
						for ( Direction dir : directions ) {
							queue.add( curr.move( dir ) );
						}
					}
				}
			}
		}

		return opened;
	}

	/**
	 * Returns a immutable position given a position pos.
	 *
	 * @param pos the pos to return an immutable version of.
	 * @return the immutable pos.
	 */
	private FinalPosition immutablePosition( Position pos ) {
		return pos instanceof FinalPosition ? (FinalPosition) pos : new FinalPosition( pos );
	}

	/**
	 * Expands flagged cells.
	 *
	 * @param pos the initial position.
	 * @return the state after expansion.
	 */
	private State expandCells( Position pos ) {
		pos = this.immutablePosition( pos );

		int count = 0;

		// Count the number of adjacent flags
		Direction[] directions = Direction.all();
		for ( Direction dir : directions ) {
			Position temp = pos.move( dir );

			if ( this.inBounds( temp ) && this.get( temp ).getState() == CellState.FLAG_CLICKED ) {
				++count;
			}
		}

		State lastGameState = State.PROGRESS;

		// If you have clicked enough adjacent flags
		if ( count == this.get( pos ).getValue() ) {
			// Click each adjacent square normally
			for ( Direction dir : directions ) {
				Position temp = pos.move( dir );
				MinesweeperMove move = new MinesweeperMove(temp, Action.NORMAL );
				lastGameState = this.performMove( move );
			}
		}

		return lastGameState;
	}

	/* --------------------------------
	 * Fields.
	 * --------------------------------
	 */

	private int mines, squaresLeft = -1;

	private boolean generated;

	private Random rng;
}
