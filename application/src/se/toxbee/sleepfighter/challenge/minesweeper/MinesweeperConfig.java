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

import java.util.Iterator;

import com.google.common.base.Preconditions;

import se.toxbee.sleepfighter.utils.geom.Dimension;
import se.toxbee.sleepfighter.utils.geom.FinalPosition;
import se.toxbee.sleepfighter.utils.string.StringUtils;

/**
 * MinesweeperConfig models a configuration of how to make a minesweeper board.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Nov 5, 2013
 */
public final class MinesweeperConfig {
	/**
	 * Level defines standard levels based on Microsoft's Minesweeper.
	 *
	 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
	 * @version 1.0
	 * @since Nov 9, 2013
	 */
	public enum Level {
		BEGINNER( 8, 8, 10 ),
		INTERMEDIATE( 16, 16, 40 ),
		EXPERT( 30, 16, 99 );

		Level( int w, int h, int mines ) {
			this.cfg = new MinesweeperConfig( w, h, mines );
		}

		/**
		 * Returns the config for the level.
		 *
		 * @return the config.
		 */
		public MinesweeperConfig getConfig() {
			return this.cfg;
		}

		/**
		 * Returns a level from an integer.<br/>
		 * If no level corresponds to the argument,<br/>
		 * {@link #BEGINNER} is returned.
		 *
		 * @param i the level corresponding to {@link #ordinal()}.
		 * @return the corresponding level.
		 */
		public static Level fromInt( int i ) {
			i = Math.abs( i );

			Level[] levels = values();
			return	i < levels.length
				?	levels[i]
				:	BEGINNER; // Houston, we've a problem - there's no such level, resort to lowest (beginner).
		}

		private MinesweeperConfig cfg;
	}

	/**
	 * Creates a config from a string in the format "{w},{h},{m}"<br/>
	 * where {w} = width, {h} = height, {m} = mines in board.<br/>
	 * All values are expected to be integral.<br/>
	 * The format is forgiving: any whitespace in between values are omitted.
	 *
	 * @param str the string containing arguments.
	 * @return the created config.
	 */
	public static MinesweeperConfig fromString( String str ) {
		if ( str == null || str.isEmpty() ) {
			throw new IllegalArgumentException( "String was empty/null, can't make config" );
		}

		/*
		 * We make the following assumptions:
		 * 1) The user intends positive values, abs(x) them.
		 * 3) [w] = [1
		 * 2) Anything else = fail.
		 *
		 * In other words: we're a bit forgiving.
		 */

		// First pass, split.
		Iterator<String> it = StringUtils.COMMA_SPLITTER.split( str ).iterator();

		if ( it.hasNext() ) {
			int w = iterNextInt( it );
	
			if ( it.hasNext() ) {
				int h = iterNextInt( it );

				if ( it.hasNext() ) {
					int m = iterNextInt( it );

					return new MinesweeperConfig( w, h, m );
				}
			}
		}

		throw new IllegalArgumentException( "Params string is invalid! Given: " + str );
	}

	private static int iterNextInt( Iterator<String> it ) {
		return Math.abs( Integer.parseInt( it.next() ) );
	}

	/**
	 * Constructs a MinesweeperConfig with (width, height) and mineCount.
	 *
	 * @param width the width of the board.
	 * @param height the height of the board.
	 * @param mineCount the mine count of boards made with this config.
	 */
	public MinesweeperConfig( int width, int height, int mineCount ) {
		this( new FinalPosition( width, height ), mineCount );
	}

	/**
	 * Constructs a MinesweeperConfig with dim and mineCount.
	 *
	 * @param dim the dimension of boards made with this config.
	 * @param mineCount the mine count of boards made with this config.
	 */
	public MinesweeperConfig( Dimension dim, int mineCount ) {
		if ( dim.n() != 2 ) {
			throw new IllegalArgumentException( "2D dim required, given:" + dim.n() );
		} else if ( Preconditions.checkNotNull( dim ).lowest() >= 3 ) {
			throw new IllegalArgumentException( "min(dim) >= 3 required." );
		} else if ( mineCount < 1 ) {
			throw new IllegalArgumentException( "mineCount >= 1 required, given: " + mineCount );
		} else if ( dim.cross() - mineCount < (3 * 4 - 1) ) {
			throw new IllegalArgumentException( "There must be space for atleast: " + (3 * 4 - 1) + " free cells." );
		}

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

	private final Dimension dim;
	private final int mineCount;
}
