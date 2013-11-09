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
import java.util.Random;

import se.toxbee.sleepfighter.challenge.minesweeper.MinesweeperBoard.State;
import se.toxbee.sleepfighter.challenge.minesweeper.MinesweeperMove.Action;
import se.toxbee.sleepfighter.utils.geom.Position;

/**
 * MinesweeperFactory is the factory for minesweeper games.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Nov 5, 2013
 */
public class MinesweeperFactory {
	/**
	 * SolverConfig provides configuration for factory on how to deal with solver.
	 *
	 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
	 * @version 1.0
	 * @since Nov 5, 2013
	 */
	public static class SolverConfig {
		// The class is really struct-ish.
		public boolean requireSolvable;
		public int maxSolvingTries;

		public SolverConfig() {
			this( true, 1000 );
		}

		public SolverConfig( boolean requireSolvable, int maxSolvingTries ) {
			this.requireSolvable = requireSolvable;
			this.maxSolvingTries = maxSolvingTries;
		}
	}

	private MinesweeperConfig config;
	private SolverConfig solverConfig;

	/**
	 * Constructs factory without setting anything.<br/>
	 * {@link #setConfig(MinesweeperConfig)} must at least be called at some point after this constructor.
	 */
	public MinesweeperFactory() {
	}

	/**
	 * Constructs a factory with config & using default {@link SolverConfig#SolverConfig()}.
	 *
	 * @param config the config to use for making games.
	 */
	public MinesweeperFactory( MinesweeperConfig config ) {
		this( config, new SolverConfig() );
	}

	/**
	 * Constructs a factory with config & solverConfig.
	 *
	 * @param config the config to use for making games.
	 * @param solverConfig the config to use for solving.
	 */
	public MinesweeperFactory( MinesweeperConfig config, SolverConfig solverConfig ) {
		this.setConfig( config ).setSolverConfig( solverConfig );
	}

	/**
	 * Sets the config to use for making games.
	 *
	 * @param config the config.
	 * @return this.
	 */
	public MinesweeperFactory setConfig( MinesweeperConfig config ) {
		this.config = config;
		return this;
	}

	/**
	 * Sets the config to use for solving, or null if we shouldn't solve.
	 *
	 * @param config the config for solver.
	 * @return this.
	 */
	public MinesweeperFactory setSolverConfig( SolverConfig config ) {
		this.solverConfig = config;
		return this;
	}

	/**
	 * Produces a game given an initial position.
	 *
	 * @param initial initial the initial position where not to place mines.
	 * @return the game.
	 */
	public MinesweeperGame produce( Position initial ) {
		// Bootstrap.
		MinesweeperGame game = null;
		Random rng = new Random();
		MinesweeperMove firstMove = new MinesweeperMove( initial, Action.NORMAL );

		// Solve?
		boolean solve = this.solverConfig == null ? false : this.solverConfig.requireSolvable;

		// Make game.
		if ( solve ) {
			for ( int i = 0; i < this.solverConfig.maxSolvingTries; ++i ) {
				game = this.make( rng );
				State state = this.solve( game, firstMove );

				if ( state == State.WON ) {
					break;
				}
			}

			game.board().resetToGenerated( firstMove );
		} else {
			game = this.make( rng );
			game.acceptMove( firstMove );
		}

		return game;
	}

	/**
	 * Constructs a game with given config & rng.
	 *
	 * @param config the config.
	 * @param rng the RNG.
	 * @return the made game.
	 */
	private MinesweeperGame make( Random rng ) {
		return new MinesweeperGame( this.config, rng );
	}

	/**
	 * Attempts to solve a game of minesweeper.
	 *
	 * @param game
	 * @param firstMove
	 * @return
	 */
	private State solve( MinesweeperGame game, MinesweeperMove firstMove ) {
		MinesweeperSolver solver = new MinesweeperSolver();

		// Make the initial first move.
		game.acceptMove( firstMove );

		// Now get the AI to work out the rest.
		do {
			List<MinesweeperMove> movesToPerform = solver.getMoves( game.board() );
			if ( movesToPerform != null ) {
				for ( MinesweeperMove move : movesToPerform ) {
					game.acceptMove( move );
				}
			}

		} while ( game.state() == State.PROGRESS );

		return game.state();
	}
}
