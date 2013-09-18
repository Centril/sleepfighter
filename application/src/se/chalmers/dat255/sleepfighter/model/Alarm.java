package se.chalmers.dat255.sleepfighter.model;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Alarm models the alarm settings and business logic for an alarm.
 *
 * @version 1.0
 * @since Sep 16, 2013
 */
public class Alarm {
	private boolean isActivated;

	private int hour;
	private int minute;

	/** The weekdays that this alarm can ring. */
	private boolean[] enabledDays = { true, true, true, true, true, true, true };
	private static final int MAX_WEEKDAY_INDEX = 6;

	/** The value {@link #getNextMillis()} returns when Alarm can't happen. */
	public static long NEXT_NON_REAL = -1;

	/**
	 * Constructs an alarm given an hour and minute.
	 *
	 * @param hour the hour the alarm should ring.
	 * @param minute the minute the alarm should ring.
	 */
	public Alarm(int hour, int minute) {
		this.hour = hour;
		this.minute = minute;
	}

	/**
	 * Sets the hour and minute of this alarm.
	 *
	 * @param hour the hour this alarm rings.
	 * @param minute the minute this alarm rings.
	 */
	public void setTime(int hour, int minute) {
		if (hour < 0 || hour > 23 || minute < 0 || minute > 59) {
			throw new IllegalArgumentException();
		}
		this.hour = hour;
		this.minute = minute;
	}

	/**
	 * Returns the weekdays days this alarm is enabled for.<br/>
	 * For performance, a direct reference is returned.
	 *
	 * @return the weekdays alarm is enabled for.
	 */
	public boolean[] getEnabledDays() {
		return this.enabledDays;
	}

	/**
	 * Sets the weekdays this alarm is enabled for.<br/>
	 * For performance, the passed value is not cloned.
	 *
	 * @param enabledDays the weekdays alarm should be enabled for.
	 */
	public void setEnabledDays( boolean[] enabledDays ) {
		this.enabledDays = enabledDays;
	}

	/**
	 * Returns when this alarm will ring.<br/>
	 * If {@link #canHappen()} returns false, -1 will be returned.
	 *
	 * @return the time in unix epoch timestamp when alarm will next ring.
	 */
	public long getNextMillis() {
		if ( !this.canHappen() ) {
			return NEXT_NON_REAL;
		}

		Calendar now = new GregorianCalendar();
		Calendar next = (Calendar) now.clone();
		next.set( Calendar.HOUR, this.hour );
		next.set( Calendar.MINUTE, this.minute );

		// Huston, we've a problem, alarm is before now, adjust 1 day.
		if ( next.before( now ) ) {
			next.add( Calendar.DAY_OF_MONTH, 1 );
		}

		// Offset for weekdays. Checking canHappen() is important for this part.
		int offset = 0;
		for ( int currDay = now.get( Calendar.DAY_OF_WEEK - 1 ); !this.enabledDays[currDay]; currDay++ ) {
			if ( currDay > MAX_WEEKDAY_INDEX ) {
				currDay = 0;
			}

			offset++;
		}

		if ( offset > 0 ) {
			next.add( Calendar.DAY_OF_MONTH, offset );
		}

		return next.getTimeInMillis();
	}

	/**
	 * Returns true if the alarm can ring in the future,<br/>
	 * that is: if {@link #isActivated()} and some weekday is enabled.
	 *
	 * @return true if the alarm can ring in the future.
	 */
	public boolean canHappen() {
		if ( !this.isActivated() ) {
			return false;
		}

		for ( boolean day : this.enabledDays ) {
			if ( day == true ) {
				return true;
			}
		}

		return false;
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
		return this.hour + ":" + this.minute + " is" + (this.isActivated ? " " : " NOT ") + "activated.";
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
		if (this == obj) {
			return true;
		}

		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}

		Alarm other = (Alarm) obj;
		return this.hour == other.hour && this.isActivated == other.isActivated && minute == other.minute;
	}
}