package se.chalmers.dat255.sleepfighter.model;

public class Alarm {
	
	private boolean isActivated;
	private int hour;
	private int minute;

	public Alarm(int hour, int minute) {
		this.hour = hour;
		this.minute = minute;
	}
	
	public void setTime(int hour, int minute) {
		if (hour < 0 || hour > 23 || minute < 0 || minute > 59) {
			throw new IllegalArgumentException();
		}
		this.hour = hour;
		this.minute = minute;
	}
	
	public int getHour() {
		return hour;
	}
	
	public int getMinute() {
		return minute;
	}
	
	public void setActivated(boolean isActivated) {
		this.isActivated = isActivated;
	}
	
	public boolean isActivated() {
		return this.isActivated;
	}
}