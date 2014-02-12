/*
 * Copyright 2014 toxbee.se
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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