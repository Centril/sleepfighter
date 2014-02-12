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

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing each segment of the snake body.
 * 
 * @author Hassel
 *
 */
public class Segment extends CircleEntity {
	private final List<Float> xList;
	private final List<Float> yList;
	
	private int iter;
	
	/**
	 * @param x x-coordinate of the middle of the segment
	 * @param y y-coordinate of the middle of the segment
	 * @param radius the radius of the segment
	 * @param paint the color of the segment
	 */
	public Segment(float x, float y, int radius, Paint paint) {
		super(x, y, radius, paint);
		
		iter = 0;
		
		xList = new ArrayList<Float>();
		yList = new ArrayList<Float>();
	}

	/**
	 * @return list of all x coordinates that this segment has stored
	 */
	public List<Float> getXList() {
		return xList;
	}

	/**
	 * @return list of all y coordinates that this segment has stored
	 */
	public List<Float> getYList() {
		return yList;
	}
	
	/**
	 * Increases the number of iterations by 1
	 */
	public void iter() {
		iter++;
	}
	
	/**
	 * @return the number of iterations this segment has stored
	 */
	public int getIter() {
		return iter;
	}
}