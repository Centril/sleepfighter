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
#ifndef _MINESWEEPER_MINESWEEPER_H
#define _MINESWEEPER_MINESWEEPER_H

#include <string>
#include "game.h"
#include "logging.h"

using namespace std;

struct make_config {
	// board config.
	Dimensions& dim;
	int mineCount;

	// initial move position.
	Position& pos;

	// solver config.
	bool requireSolvable;
	int maxSolvingTries;

	// logger config.
	android_LogPriority logPrio;
	string& tag;
};

Game* makeGame( const make_config& config );

void freeGame( const Game* game );

#endif
