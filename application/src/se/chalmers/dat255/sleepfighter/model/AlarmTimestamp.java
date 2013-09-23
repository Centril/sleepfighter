package se.chalmers.dat255.sleepfighter.model;

/**
 * Provides information about when alarm occurs in unix timestamp and the alarm itself. alarm.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Sep 18, 2013
 */
public class AlarmTimestamp {
	private long millis;
	private Alarm alarm;

	/** The value of an invalid (non-existent AlarmTimestamp) */
	public final static AlarmTimestamp INVALID = null;

	/**
	 * Constructs an AlarmTimestamp.
	 *
	 * @param millis milliseconds to alarm.
	 * @param alarm the alarm object that corresponds to millis.
	 * 
	 */
	public AlarmTimestamp( Long millis, Alarm alarm) {
		this.millis = millis;
		this.alarm = alarm;
	}

	/**
	 * The earliest alarm in milliseconds.
	 *
	 * @return the earliest alarm in milliseconds.
	 */
	public Long getMillis() {
		return this.millis;
	}

	/**
	 * The earliest alarm.
	 *
	 * @return the earliest alarm.
	 */
	public Alarm getAlarm() {
		return this.alarm;
	}
}