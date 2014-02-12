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