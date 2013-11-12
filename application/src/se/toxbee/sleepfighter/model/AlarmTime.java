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
package se.toxbee.sleepfighter.model;

import org.joda.time.DateTime;
import org.joda.time.ReadableDateTime;

import com.google.common.collect.ComparisonChain;

import se.toxbee.sleepfighter.utils.string.StringUtils;

/**
 * AlarmTime holds the time of an alarm.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Nov 11, 2013
 */
public class AlarmTime implements Comparable<AlarmTime> {
	private int second;
	private int minute;
	private int hour;

	/**
	 * Returns the second.
	 *
	 * @return the second
	 */
	public int getSecond() {
		return this.second;
	}

	/**
	 * Returns the minute.
	 *
	 * @return the minute
	 */
	public int getMinute() {
		return this.minute;
	}

	/**
	 * Returns the hour.
	 *
	 * @return the hour.
	 */
	public int getHour() {
		return this.hour;
	}

	/**
	 * Constructs time with h (hour) m (month) and s = 0.
	 *
	 * @param h hour.
	 * @param m month.
	 */
	public AlarmTime( int h, int m ) {
		this( h, m, 0);
	}

	/**
	 * Constructs time with h (hour), m (month), s (second).
	 * Times are wrapped.
	 *
	 * @param h hour.
	 * @param m month.
	 * @param s second.
	 */
	public AlarmTime( int h, int m, int s ) {
		this.hour = h % 24;
		this.minute = m % 60;
		this.second = s % 60;
	}

	/**
	 * Constructs time with values derived from a UNIX epoch timestamp.
	 *
	 * @param time time in UNIX epoch timestamp.
	 */
	public AlarmTime( long time ) {
		this( new DateTime(time) );
	}

	/**
	 * Constructs time with values derived from a {@link ReadableDateTime} object.
	 * 
	 * @param time a {@link ReadableDateTime} object. 
	 */
	public AlarmTime(ReadableDateTime time) {
		this( time.getHourOfDay(), time.getMinuteOfHour(), time.getSecondOfMinute() );
	}

	/**
	 * Constructs time from a packed integer with format: HHHHHMMMMMMSSSSSS
	 */
	public AlarmTime( int i ) {
		this.second = i & 0x3F;
		this.minute = (i >> 6) & 0x3F;
		this.hour = i >> 12;
	}

	/**
	 * Packs the time to an integer with format: HHHHHMMMMMMSSSSSS.
	 *
	 * @return the integer.
	 */
	public int pack() {
		return (this.hour << 12) | (this.minute << 6) | this.second;
	}

	/**
	 * <p>Uses {@link #pack()}</p>
	 *
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return this.pack();
	}

	/**
	 * <p>Uses {@link #hashCode()}</p>
	 *
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals( Object o ) {
		return this == o || (o != null && o.getClass() == this.getClass() && this.hashCode() == o.hashCode());
	}

	/**
	 * Returns the time as an array of integers: [h, m (, s)].
	 *
	 * @param omitSecond omit the second?
	 * @return the array.
	 */
	public int[] asArray( boolean omitSecond ) {
		return	omitSecond
			?	new int[] { this.hour, this.minute }
			:	new int[] { this.hour, this.minute, this.second };
	}

	/**
	 * Returns the AlarmTime as a string with format: HH:MM::SS.
	 *
	 * @return the string.
	 */
	public String toString() {
		return this.getTimeString( false );
	}

	/**
	 * Returns the AlarmTime as a string with format: HH:MM(::SS).
	 *
	 * @param omitSecond omit the second?
	 * @return the string.
	 */
	public String getTimeString( boolean omitSecond ) {
		return StringUtils.joinTime( this.asArray( omitSecond ) );
	}

	/**
	 * <p>Compares with following order: {@link #getSecond()}, {@link #getMinute()}, {@link #getHour()}.</p>
	 *
	 * {@inheritDoc}
	 */
	@Override
	public int compareTo( AlarmTime r ) {
		return ComparisonChain.start()
			.compare( this.second, r.second )
			.compare( this.minute, r.minute )
			.compare( this.hour, r.hour )
			.result();
	}
}
