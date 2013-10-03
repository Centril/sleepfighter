package se.chalmers.dat255.sleepfighter.challenge.fluidsnake;

import android.graphics.Paint;

public class RectEntity extends Entity {
	private float width;
	private float height;
	
	public RectEntity(float x, float y, float width, float height, Paint paint) {
		super(x, y, paint);
		this.width = width;
		this.height = height;
	}
	
	public float getWidth() {
		return width;
	}
	
	public float getHeight() {
		return height;
	}
}