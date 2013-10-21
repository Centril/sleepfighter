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

#include <cstdlib>

#include "minesweeper.h"
#include "game.h"
#include "solver.h"
#include "logging/logging.h"
#include "logging/android_logging.h"

using namespace std;

static class GameMaker;
static int random(int i);
static void random_seed();
static Game* makeRandomGame( const GameMaker& maker, const Position& pos, bool solve, int maxSolve );
static GameState solveRandomGame(Game& game, Move& firstMove, const Position& pos);

static android_LogPriority g_log_prio;
static std::string& g_log_tag;

void Minesweeper::configLogger( const int prio, const char* tag ) {
	g_log_prio = prio;
	g_log_tag = string(tag);
}

Game* Minesweeper::makeGame( const MinesweeperConfig& config, const Position& pos ) {
	logger = new android_logger( g_log_prio, g_log_tag );
	GameMaker maker = maker( config.dim, config.mineCount, random_seed, random, logger );
	return makeRandomGame( maker, pos, config.requireSolvable, config.maxSolvingTries );
}

void Minesweeper::freeGame( const Game* game ) {
	delete game;
}

static class GameMaker {
public:
	GameMaker(const Dimensions& dim, int mineCount, const logger& logger) :
			dim(dim), mineCount(mineCount), logger(logger) {
	}

	Game* operator()() {
		return new Game( dim, mineCount, random, logger );
	}

private:
	const Dimensions& dim;
	const int mineCount;
	const logger& logger;
};

// requirement: maxSolve > 0.
static Game* makeRandomGame( const GameMaker& maker, const Position& pos, bool solve, int maxSolve ) {
	Game* game;

	random_seed();

	if (!solve) {
		game = maker();
		game->firstMove(pos);
		game->print();
	} else {
		Move move;

		int i = 0;
		while( true ) {
			game = maker();
			GameState state = solveRandomGame(*game, move, pos);
			if (state == WON) {
				(*game->getLogger()) << "Solved game! Quitting.";
				break;
			} else if ( i == maxSolve - 1) {
				(*game->getLogger()) << "Did not solved game with " + i + " of " + maxSolve + " tries. Quitting.";
				break;
			}

			delete game;

			++i;
		}

		game->resetToGenerated( move );
	}

	return game;
}

static GameState solveRandomGame(Game& game, Move& firstMove, const Position& pos) {
	solver turnSolver;

	logger& log = game.getLogger();

	// Make the initial move provided by pos.
	firstMove = game.firstMove( pos );

	// Now get the AI to work out the rest.
	// see: http://bulldozer00.com/2012/02/09/vectors-and-lists/
	vector<Move>* movesToPerform = NULL;
	do {
		if (movesToPerform != NULL) {
			delete movesToPerform;
			movesToPerform = NULL;
		}

		movesToPerform = turnSolver.getMoves(game.getBoard(), &log);

		if (movesToPerform != NULL) {
			for (list<Move>::const_iterator it = movesToPerform->begin();
					it != movesToPerform->end(); ++it) {
				Move currentMove = *it;
				game.acceptMove(currentMove);
			}
		}
		game.print();
	} while (game.getState() == PROGRESS && movesToPerform != NULL
			&& !movesToPerform->empty());

	// Clear the final moves
	if (movesToPerform != NULL) {
		delete movesToPerform;
		movesToPerform = NULL;
	}

	return game.getState();
}

static int random(int i) {
	return std::rand() % i;
}

static void random_seed() {
	std::srand( time( 0 ) );
}
