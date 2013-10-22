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
 * Class representing any rectangular entities.
 * 
 * @author Hassel
 *
 */
public class RectEntity extends Entity {
	private float width;
	private float height;
	
	/**
	 * @param x top left corner
	 * @param y top left corner
	 * @param width width of the rectangle
	 * @param height height of the rectangle
	 * @param paint color of this rectangle
	 */
	public RectEntity(float x, float y, float width, float height, Paint paint) {
		super(x, y, paint);
		this.width = width;
		this.height = height;
	}
	
	/**
	 * @return the width of this rectangle
	 */
	public float getWidth() {
		return width;
	}
	
	/**
	 * @return the height of this rectangle
	 */
	public float getHeight() {
		return height;
	}
}