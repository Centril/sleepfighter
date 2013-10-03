package se.chalmers.dat255.sleepfighter.challenge.fluidsnake;

import android.graphics.Paint;

public class CircleEntity extends Entity { // Healthy!!
	private int width;
	
	public CircleEntity(float x, float y, int width, Paint paint) {
		super(x, y, paint);
		
		this.width = width;
	}

	public float getWidth() {
		return width;
	}
	
	public float getHeight() {
		return width;
	}
}