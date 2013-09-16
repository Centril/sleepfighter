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
		return this.hour;
	}
	
	public int getMinute() {
		return this.minute;
	}
	
	public void setActivated(boolean isActivated) {
		this.isActivated = isActivated;
	}
	
	public boolean isActivated() {
		return this.isActivated;
	}
	
	@Override
	public String toString() {
		return hour + ":" + minute + " is" + (isActivated ? " " : " NOT ") + "activated.";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + hour;
		result = prime * result + (isActivated ? 1231 : 1237);
		result = prime * result + minute;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Alarm other = (Alarm) obj;
		if (hour != other.hour)
			return false;
		if (isActivated != other.isActivated)
			return false;
		if (minute != other.minute)
			return false;
		return true;
	}
	
	
}