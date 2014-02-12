/*
 * Copyright 2014 toxbee.se
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package se.toxbee.sleepfighter.model;

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
