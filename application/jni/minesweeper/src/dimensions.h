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
#ifndef DIMENSIONS_H_
#define DIMENSIONS_H_

/**
 * \brief This class represents dimensions.
 *
 * Please note that it is immutable.
 */
class Dimensions {
public:
	typedef int value_type;

	Dimensions(value_type w, value_type h) : width(w), height(h) {
	}

	value_type getWidth() const {
		return width;
	}

	value_type getHeight() const {
		return height;
	}

	value_type size() const {
		return width * height;
	}

private:
	value_type width, height;
};


#endif /* DIMENSIONS_H_ */
