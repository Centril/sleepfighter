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

template<typename T> class basic_position;
typedef basic_position<int> Position;

template<typename T>
class basic_position {
public:
	typedef T value_type;

	enum direction {
		NORTHWEST = 0,
		NORTH = 1,
		NORTHEAST = 2,
		EAST = 3,
		SOUTHEAST = 4,
		SOUTH = 5,
		SOUTHWEST = 6,
		WEST = 7
	};

	static const int directions_count = 8;

	basic_position() : x(0), y(0) {
	}

	basic_position(value_type _x, value_type _y) : x(_x), y(_y) {
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

	Position translated( const value_type dx, const value_type dy ) const {
		return Position( x + dx, y + dy );
	}

	inline Position translated( direction dir ) const {
		return translated( deltaMap[dir][0], deltaMap[dir][1] );
	}

private:
	value_type x, y;

	// all the defined deltas in different directions.
	static const value_type const deltaMap[8][2] = {
	   {-1,-1},
	   {0,-1},
	   {1,-1},
	   {1, 0},
	   {1, 1},
	   {0, 1},
	   {-1,1},
	   {-1,0}
	};
};

#endif
