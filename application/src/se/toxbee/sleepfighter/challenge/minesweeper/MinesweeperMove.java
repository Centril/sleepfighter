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

import se.toxbee.sleepfighter.utils.geom.Position;

/**
 * MinesweeperMove models/describes an action in the game of minesweeper.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Nov 5, 2013
 */
public class MinesweeperMove {
	/**
	 * Action enumerates the type of action a move entails in the game of minesweeper.
	 *
	 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
	 * @version 1.0
	 * @since Nov 5, 2013
	 */
	public static enum Action {
		NORMAL, FLAG, QUESTION, EXPAND
	};

	/**
	 * Constructs a move given a pos and action.
	 * 
	 * @param pos the position the move is action on.
	 * @param action the action the move entails.
	 */
	public MinesweeperMove( Position pos, Action action ) {
		this.position = pos;
		this.action = action;
	}

	/**
	 * Returns the position a move is acting on.
	 * 
	 * @return the position.
	 */
	Position getPosition() {
		return this.position;
	}

	/**
	 * Returns the action the move entails.
	 * 
	 * @return the action.
	 */
	Action getAction() {
		return this.action;
	}

	private Position position;
	private Action action;
}