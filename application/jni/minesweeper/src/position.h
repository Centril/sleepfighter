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
#ifndef POSITION_H_
#define POSITION_H_

class Position {
public:
	typedef Position class_type;
	typedef int value_type;

	enum direction {
		NORTHWEST,
		NORTH,
		NORTHEAST,
		EAST,
		SOUTHEAST,
		SOUTH,
		SOUTHWEST,
		WEST
	};

	static const int directions_count = 8;

	Position() : x(0), y(0) {
	}

	Position(value_type _x, value_type _y) : x(_x), y(_y) {
	}

	value_type getX() const {
		return x;
	}

	value_type getY() const {
		return y;
	}

	bool isAdjacent(Position other) const {
		return std::abs(x - other.x) <= 1 && std::abs(y - other.y) <= 1;
	}

	class_type translated( const value_type dx, const value_type dy ) const {
		return class_type( x + dx, y + dy );
	}

	inline class_type translated( const int dir ) const {
		return translated( deltaMap[dir][0], deltaMap[dir][1] );
	}

	// all the defined deltas in different directions.
	static const value_type deltaMap[8][2];

private:
	value_type x, y;
};

const Position::value_type Position::deltaMap[8][2]  = {
		{ -1, -1 }, { 0, -1 }, { 1, -1 }, { 1, 0 }, { 1, 1 },
		{ 0, 1 }, { -1, 1 }, { -1, 0 } };

#endif
