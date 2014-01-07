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
 * A simple entity
 * 
 * @author Hassel
 *
 */
abstract class Entity {
	private float x;
	private float y;
	
	private Paint paint;
	
	/**
	 * @param x x-coordinate of the entity
	 * @param y y-coordinate of the entity
	 * @param paint color of the entity
	 */
	public Entity(float x, float y, Paint paint) {
		this.x = x;
		this.y = y;
		this.paint = new Paint(paint);
	}
	
	/**
	 * @return x-coordinate of the entity
	 */
	public float getX() {
		return x;
	}
	
	/**
	 * @param x the x-coordinate of the entity
	 */
	public void setX(float x) {
		this.x = x;
	}
	
	/**
	 * @return y-coordinate of the entity
	 */
	public float getY() {
		return y;
	}
	
	/**
	 * @param y the y-coordinate of the entity
	 */
	public void setY(float y) {
		this.y = y;
	}
	
	/**
	 * @return the color of the entity
	 */
	public Paint getPaint() {
		return paint;
	}
	
	/**
	 * @param paint the color of the entity
	 */
	public void setPaint(Paint paint) {
		this.paint = paint;
	}
}