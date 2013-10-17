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
package se.chalmers.dat255.sleepfighter.challenge.minesweeper;

import java.util.Random;

import se.chalmers.dat255.sleepfighter.utils.math.RandomMath;

/**
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Oct 14, 2013
 */
public class MinesweeperModel {
	private Random rng;

	private MinesweeperSolver solver;

	private boolean solvingRequired;

	/**
	 * 
	 */
	public MinesweeperModel() {
	}

	/**
	 * Sets whether or not the grid must be solvable.
	 *
	 * @param flag true if it must be solvable.
	 */
	public void setSolvingRequired( boolean flag ) {
		this.solvingRequired = flag;
	}

	/**
	 * Returns whether or not the grid must be solvable.
	 *
	 * @return true if it must be solvable.
	 */
	public boolean isSolvingRequired() {
		return this.solvingRequired;
	}

	public IntGrid generateGrid( int w, int h, int n, int x, int y ) {
		IntGrid ret = new IntGrid( w, h );

		int ntries = 0;

		do {
			ntries++;

			// Start by placing n mines, none of which is at x, y or within one square of it.
			{
				IntGrid tmp = new IntGrid( w, h );
				int i, j, k, nn;

				// Write down the list of possible mine locations.
				k = 0;
				for ( i = 0; i < h; i++ ) {
					for ( j = 0; j < w; j++ ) {
						if ( Math.abs( i - y ) > 1 || Math.abs( j - x ) > 1 ) {
							tmp.set( k++, tmp.index( i, j ) );
						}
					}
				}

				// Now pick n off the list at random.
				nn = n;
				while ( nn-- > 0 ) {
					i = RandomMath.nextRandomRanged( rng, 0, k );
					ret.set( tmp.get( i ), 1 );
					tmp.set( i, --k );
				}
			}

			// Solve if needed.
			if ( !this.isSolvingRequired() || solver.solve( ret, n, x, y, ntries > 100, rng ) ) {
				return ret;
			}
		} while ( true );
	}
}