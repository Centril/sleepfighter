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

package se.toxbee.sleepfighter.challenge.snake;

import android.graphics.Paint;

/**
 * A Simple circular entity.
 * 
 * @author Hassel
 *
 */
public class CircleEntity extends Entity {
	private int radius;
	
	/**
	 * @param x the middle of the circle
	 * @param y the middle of the circle
	 * @param radius the radius of the circle
	 * @param paint the color of the circle
	 */
	public CircleEntity(float x, float y, int radius, Paint paint) {
		super(x, y, paint);
		
		this.radius = radius;
	}

	/**
	 * @return the radius
	 */
	public float getRadius() {
		return radius;
	}
}