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

import se.toxbee.sleepfighter.challenge.minesweeper.MinesweeperCell.CellState;
import se.toxbee.sleepfighter.utils.geom.Dimension;
import se.toxbee.sleepfighter.utils.geom.Direction;
import se.toxbee.sleepfighter.utils.geom.FinalPosition;
import se.toxbee.sleepfighter.utils.geom.Position;

import com.google.common.collect.Lists;

/**
 * MinesweeperSolver is the AI that solves a minesweeper game.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Nov 5, 2013
 */
public class MinesweeperSolver {
	public List<MinesweeperMove> getMoves( MinesweeperBoard board ) {
		Direction[] directions = Direction.all();
		Dimension gridDim = board.dim();

		// 1) List number squares that touch non-clicked squares.
		List<Position> nonCompletedPositions = Lists.newArrayList();
		for ( int row = 0; row < gridDim.height(); ++row ) {
			for ( int col = 0; col < gridDim.width(); ++col ) {
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

		// 2) Get all of the adjacent squares that have not been clicked and identify them.
		int currentSquareId = 0;
		Map<Integer, Integer> new HashMap
		for ( Position pos : nonCompletedPositions ) {
			for ( Direction dir : directions ) {
				Position tempPos = pos.move( dir );

				if ( board.inBounds( tempPos ) ) {
					int index = board.index( tempPos );
					MinesweeperCell cell = board.get( index );

					if  ( cell.getState() == CellState.NOT_CLICKED ) {
						
					}
				}
			}
		}

		   // 2 Get all of the adjacent squares that have not been clicked and identify them.
		   int currentSquareId = 0;
		   map<int, int> idToPosition;
		   map<int, int> positionToId;
		   for(
		         list<Position>::const_iterator it = nonCompletedPositions.begin();
		         it != nonCompletedPositions.end();
		         ++it)
		   {
		      for(int i = 0; i < 8; ++i)
		      {
		         Position tempPos(it->getX() + adjMap[i][0], it->getY() + adjMap[i][1]);
		         if(board->isValidPos(tempPos)) 
		         {
		            int position = board->locPos(tempPos);
		            if(grid[position].state == NOT_CLICKED)
		            {
		               map<int, int>::iterator found = positionToId.find(position);
		               if(found == positionToId.end())
		               {
		                  positionToId[position] = currentSquareId;
		                  idToPosition[currentSquareId] = position;
		                  currentSquareId++;
		               }
		            }
		         }
		      }
		   }

		// TODO
		return null;
	}
}
