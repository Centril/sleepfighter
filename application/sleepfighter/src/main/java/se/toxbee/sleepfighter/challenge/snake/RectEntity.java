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