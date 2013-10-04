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

	public String toString() {
		return "AlarmTimestamp[@: " + Long.toString( this.getMillis() ) + ", alarm: " + this.getAlarm() + "]";
	}
}
