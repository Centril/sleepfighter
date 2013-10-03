package se.chalmers.dat255.sleepfighter.challenge.fluidsnake;

import android.graphics.Paint;

abstract class Entity {
	private float x;
	private float y;
	
	private Paint paint;
	
	public Entity(float x, float y, Paint paint) {
		this.x = x;
		this.y = y;
		this.paint = new Paint(paint);
	}
	
	public float getX() {
		return x;
	}
	
	public void setX(float x) {
		this.x = x;
	}
	
	public float getY() {
		return y;
	}
	
	public void setY(float y) {
		this.y = y;
	}
	
	public Paint getPaint() {
		return paint;
	}
	
	public void setPaint(Paint paint) {
		this.paint = paint;
	}
}