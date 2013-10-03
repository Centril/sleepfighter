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

package se.chalmers.dat255.sleepfighter.challenge.fluidsnake;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Paint;

public class Segment extends CircleEntity {
	private final List<Float> xList;
	private final List<Float> yList;
	
	private int iter;
	
	public Segment(float x, float y, int width, Paint paint) {
		super(x, y, width, paint);
		
		iter = 0;
		
		xList = new ArrayList<Float>();
		yList = new ArrayList<Float>();
	}

	public List<Float> getXList() {
		return xList;
	}

	public List<Float> getYList() {
		return yList;
	}
	
	public void iter() {
		iter++;
	}
	
	public int getIter() {
		return iter;
	}
}