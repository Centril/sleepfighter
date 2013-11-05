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

import se.toxbee.sleepfighter.utils.geom.Dimension;

/**
 * MinesweeperConfig models a configuration of how to make a minesweeper board.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Nov 5, 2013
 */
public final class MinesweeperConfig {
	private final Dimension dim;
	private final int mineCount;

	/**
	 * Constructs a MinesweeperConfig with dim and mineCount.
	 *
	 * @param dim the dimension of boards made with this config.
	 * @param mineCount the mine count of boards made with this config.
	 */
	public MinesweeperConfig( Dimension dim, int mineCount ) {
		this.dim = dim;
		this.mineCount = mineCount;
	}

	/**
	 * Returns the dimension of boards made with this config.
	 *
	 * @return the dimension.
	 */
	public Dimension dim() {
		return this.dim;
	}

	/**
	 * Returns the mine count of boards made with this config.
	 *
	 * @return the mine count.
	 */
	public int mineCount() {
		return this.mineCount;
	}
}
