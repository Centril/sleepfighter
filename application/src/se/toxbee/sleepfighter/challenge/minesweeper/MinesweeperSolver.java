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

import java.util.List;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import se.toxbee.sleepfighter.challenge.minesweeper.MinesweeperCell.CellState;
import se.toxbee.sleepfighter.challenge.minesweeper.MinesweeperMove.Action;
import se.toxbee.sleepfighter.utils.geom.Direction;
import se.toxbee.sleepfighter.utils.geom.FinalPosition;
import se.toxbee.sleepfighter.utils.geom.Position;
import se.toxbee.sleepfighter.utils.math.MatrixUtil;

import com.badlogic.gdx.utils.IntMap;
import com.google.common.collect.Lists;

/**
 * MinesweeperSolver is the AI that solves a minesweeper game.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Nov 5, 2013
 */
public class MinesweeperSolver {
	private class Optional {
		private boolean value, present;

		public Optional( boolean value ) {
			this.value = value;
			this.present = true;
		}

		public Optional() {
			present = false;
		}

		boolean isPresent() {
			return present;
		}

		boolean get() {
			return value;
		}
	};

	private List<Position> computeNonComplete( MinesweeperBoard board, Direction[] directions ) {
		int height = board.dim().height();
		int width = board.dim().width();

		List<Position> nonCompletedPositions = Lists.newArrayList();
		for ( int row = 0; row < height; ++row ) {
			for ( int col = 0; col < width; ++col ) {
				MinesweeperCell currentCell = board.get( col, row );

				if ( currentCell.getState() == CellState.CLICKED ) {
					Position pos = new FinalPosition( col, row );

					int value = currentCell.getValue();
					if ( value > MinesweeperCell.EMPTY && value < MinesweeperCell.MINE ) {
						// Convert to helper function.
						boolean unclickedFound = false;
						for ( int i = 0; i < directions.length && !unclickedFound; ++i ) {
							Direction dir = directions[i];
							Position tempPos = pos.move( dir );

							if ( board.inBounds( tempPos ) && board.get( tempPos ).getState() == CellState.NOT_CLICKED ) {
								unclickedFound = true;
							}
						}

						// If the square is not completely flagged.
						if ( unclickedFound ) {
							nonCompletedPositions.add( pos );
						}
					}
				}
			}
		}

		return nonCompletedPositions;
	}

	private void setOptional( List<Optional> results, RealMatrix solMat, int maxVariableColumn, int row, boolean base ) {
		// every non zero item is actually zero
		for ( int col = row; col < maxVariableColumn; ++col ) {
			double currentValue = solMat.getEntry( row, col );
			if ( currentValue > 0.0 ) {
				results.set( col, new Optional( base ) );
			}
			if ( currentValue < 0.0 ) {
				results.set( col, new Optional( !base ) );
			}
		}
	}

	private int identifyAdjacent( MinesweeperBoard board, Direction[] directions, List<Position> nonCompletedPositions, IntMap<Integer> posToId, IntMap<Integer> idToPos ) {
		int currentSquareId = 0;

		for ( Position pos : nonCompletedPositions ) {
			for ( Direction dir : directions ) {
				Position tempPos = pos.move( dir );

				if ( board.inBounds( tempPos ) ) {
					int index = board.index( tempPos );
					MinesweeperCell cell = board.get( index );

					if  ( cell.getState() == CellState.NOT_CLICKED && posToId.containsKey( index ) ) {
						posToId.put( index, currentSquareId );
						idToPos.put( currentSquareId++, index );
					}
				}
			}
		}

		return currentSquareId;
	}

	private RealMatrix generateSolveMatrix( MinesweeperBoard board, Direction[] directions, List<Position> nonCompletedPositions, int currentSquareId, IntMap<Integer> posToId ) {
		int totalSquares = currentSquareId;
		RealMatrix solMat = new Array2DRowRealMatrix();
		RealVector tempRow = new ArrayRealVector( totalSquares + 1 );

		int r = 0;
		for ( Position pos : nonCompletedPositions ) {
			tempRow.set( 0.0 );
			tempRow.setEntry( totalSquares, board.get( pos ).getValue() );

			for ( Direction dir : directions ) {
				Position adjacent = pos.move( dir );

				if ( board.inBounds( adjacent ) ) {
					int adjacentIndex = board.index( adjacent );
					CellState state = board.get( adjacentIndex ).getState();

					switch ( state ) {
					case NOT_CLICKED:
						int matrixColumn = posToId.get( adjacentIndex );
						tempRow.setEntry( matrixColumn, 1.0 );
						break;

					case FLAG_CLICKED:
						tempRow.setEntry( totalSquares, tempRow.getEntry( totalSquares ) - 1 );
						break;

					default:
						break;
					}
				}
			}

			solMat.setRowVector( r++, tempRow );
		}

		return solMat;
	}

	private List<MinesweeperMove> assembleMoves( MinesweeperBoard board, List<Optional> results, IntMap<Integer> idToPos, int matrixWidth ) {
		List<MinesweeperMove> moves = Lists.newArrayList();
		for ( int i = 0; i < matrixWidth - 1; ++i ) {
			Optional opt = results.get( i );
			if ( opt.isPresent() ) {
				Position pos = board.position( idToPos.get( i ) );
				Action act = opt.get() ? Action.FLAG : Action.NORMAL;
				moves.add( new MinesweeperMove( pos, act ) );
			}
		}

		return moves;
	}

	private int firstNonZeroRow( RealMatrix solMat ) {
		int firstNonZeroRow = 0;

		int matrixWidth = solMat.getColumnDimension();
		int matrixHeight = solMat.getRowDimension();

		// if the condition looks incorrect then look again. row is often unsigned on multiple platforms
		// so you cannot only say row >= 0 because that is always true you need to spot the integer
		// overflow as well otherwise you have an infinite loop.
		for ( int row = matrixHeight - 1; row >= 0 && row < matrixHeight; --row ) {
			boolean foundNonZero = false;

			for ( int col = 0; col < matrixWidth && !foundNonZero; ++col ) {
				foundNonZero |= solMat.getEntry( row, col ) != 0;
			}

			if ( foundNonZero ) {
				firstNonZeroRow = row;
				break;
			}
		}

		return firstNonZeroRow;
	}

	private List<Optional> prepareResults( int maxVariableColumn ) {
		List<Optional> results = Lists.newArrayListWithCapacity( maxVariableColumn );
		for ( int i = 0; i < maxVariableColumn; ++i ) {
			results.add( new Optional() );
		}

		return results;
	}

	public List<MinesweeperMove> getMoves( MinesweeperBoard board ) {
		Direction[] directions = Direction.all();

		// 1) List number squares that touch non-clicked squares.
		List<Position> nonCompletedPositions = this.computeNonComplete( board, directions );

		// 2) Get all of the adjacent squares that have not been clicked and identify them.
		IntMap<Integer> positionToId = new IntMap<Integer>();
		IntMap<Integer> idToPosition = new IntMap<Integer>();

		int currentSquareId = this.identifyAdjacent( board, directions, nonCompletedPositions, positionToId, idToPosition );

		if ( nonCompletedPositions.size() == 0 || currentSquareId == 0 ) {
			// There cannot be any solution.
			return null;
		}

		// 3) Create a matrix based on the numbers that we have discovered.
		// Base it off the nonFlagged squares.
		RealMatrix solMat = this.generateSolveMatrix( board, directions, nonCompletedPositions, currentSquareId, positionToId );

		// 4) Gaussian Eliminate the Matrix.
		MatrixUtil.gaussianEliminate( solMat );

		// 5) Use the eliminated matrix and reduce to discover which squares must be mines
		// and are unknown. Use those squares to generate a list of moves that you can return.
		// Step 1: Find the first non zero row.
		int matrixWidth = solMat.getColumnDimension();
		int firstNonZeroRow = this.firstNonZeroRow( solMat );

		int maxVariableColumn = matrixWidth - 1;
		List<Optional> results = this.prepareResults( maxVariableColumn );

		for ( int row = firstNonZeroRow; row >= 0 && row <= firstNonZeroRow; --row ) {
			// If there is not a 1 in the current square then look right until
			// you find one.
			// There cannot be values in a col that is < row because of the
			// gaussian elimination

			// Place values on the other side.
			boolean failedToFindValue = false;
			int pivot = row;

			double pivotVal = solMat.getEntry( row, pivot );
			double val = solMat.getEntry( row, maxVariableColumn );

			for ( int col = row + 1; col < maxVariableColumn; ++col ) {
				double currentValue = solMat.getEntry( row, col );

				// Update the pivot if need be.
				if ( pivotVal == 0.0 && currentValue != 0.0 ) {
					pivot = col;
					pivotVal = currentValue;
				}

				// Swap variables over to the other side.
				if ( currentValue != 0.0 ) {
					Optional opt = results.get( col );
					if ( opt.isPresent() ) {
						val -= currentValue * (opt.get() ? 1.0 : 0.0);
						solMat.setEntry( row, col, 0.0 );
					} else {
						failedToFindValue = true;
					}
				}
			}
			solMat.setEntry( row, maxVariableColumn, val );

			if ( pivotVal != 0.0 ) {
				if ( failedToFindValue ) {
					// Otherwise Calculate min and max values for lemmas
					double minValue = 0, maxValue = 0;

					for ( int col = row; col < maxVariableColumn; ++col ) {
						double currentValue = solMat.getEntry( row, col );
						if ( currentValue > 0.0 ) {
							maxValue += currentValue;
						}

						if ( currentValue < 0.0 ) {
							minValue += currentValue; // plus a negative
						}
					}

					if ( val == minValue ) {
						this.setOptional( results, solMat, maxVariableColumn, row, false );
					} else if ( val == maxValue ) {
						this.setOptional( results, solMat, maxVariableColumn, row, true );
					}
					// Apply elmmas to see if you can work it out using min and max properties.
				} else if ( !results.get( pivot ).isPresent() && (val == 0.0 || val == 1.0) ) {
					// If there is only the pivot left the row can be solved with normal methods
					results.set( pivot, new Optional( val == 1.0 ) );
				}
			}
		}

		// Assemble moves.
		return this.assembleMoves( board, results, idToPosition, matrixWidth );
	}
}
