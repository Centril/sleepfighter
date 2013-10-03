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