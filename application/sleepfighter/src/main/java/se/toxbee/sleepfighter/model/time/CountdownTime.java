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

package se.toxbee.sleepfighter.model.time;

import com.google.common.base.Preconditions;
import com.google.common.primitives.Longs;

import org.joda.time.MutableDateTime;
import org.joda.time.Period;

import se.toxbee.sleepfighter.utils.model.CodifiableLong;

/**
 * <p>CountdownTime is a time occurring at an exact UNIX epoch timestamp.<br/>
 * However, the values of {@link #getHour()}, {@link #getMinute()},<br/>
 * {@link #getSecond()} are counting down all the time.</p>
 *
 * <p>To store the time, a long is at least required.</p>
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Nov 16, 2013
 */
public class CountdownTime extends AlarmTime implements CodifiableLong {
	public static class Factory implements CodifiableLong.Factory {
		@Override
		public CountdownTime produce( Long key ) {
			try {
				return new CountdownTime( key );
			} catch( IllegalArgumentException e ) {
				return null;
			}
		}
	}

	private final long timestamp;

	/**
	 * Copies a CountdownTime, if is null, null is returned.
	 *
	 * @param rhs the time to copy.
	 * @return the copied time.
	 */
	public static CountdownTime copy( CountdownTime rhs ) {
		return rhs == null ? null : new CountdownTime( rhs );
	}

	/**
	 * Copy constructor.
	 *
	 * @param rhs the time to copy from.
	 */
	public CountdownTime( CountdownTime rhs ) {
		super( rhs );
		this.timestamp = rhs.timestamp;
	}

	/**
	 * Constructs time counting down to h (hour), m (minute), s = 0.
	 * Times are wrapped.
	 *
	 * @param h hour.
	 * @param m minute.
	 */
	public CountdownTime( int h, int m ) {
		this( h, m, 0 );
	}

	/**
	 * Constructs time counting down to h (hour), m (minute), s (second).
	 * Times are wrapped.
	 *
	 * @param h hour.
	 * @param m minute.
	 * @param s second.
	 */
	public CountdownTime( int h, int m, int s ) {
		super( h, m, s );

		// Compute the timestamp.
		this.timestamp = this.computeTimestamp();
	}

	/**
	 * Constructs time counting down to the given UNIX epoch timestamp.
	 * Executes {@link #refresh()} immediately.
	 *
	 * @param timestamp the timestamp.
	 */
	public CountdownTime( long timestamp ) {
		super();

		long now = this.now();
		Preconditions.checkArgument( timestamp >= now );
		this.timestamp = timestamp;

		this.refresh();
	}

	/**
	 * Returns a UNIX epoch timestamp adding this to "now".
	 *
	 * @return the timestamp.
	 */
	private long computeTimestamp() {
		MutableDateTime t = new MutableDateTime();
		t.addHours( this.hour );
		t.addMinutes( this.minute );
		t.addSeconds( this.second );
		return t.getMillis();
	}

	@Override
	protected boolean equalsImpl( AlarmTime t ) {
		return this.timestamp == ((CountdownTime) t).timestamp;
	}

	@Override
	public int hashCode() {
		return Longs.hashCode( this.timestamp );
	}

	@Override
	public void refresh() {
		Period p = new Period( this.timestamp - this.now() );

		this.hour = p.getHours();
		this.minute = p.getMinutes();
		this.second = p.getSeconds();
	}

	private long now() {
		return System.currentTimeMillis();
	}

	@Override
	public long scheduledTimestamp( long now, Object... inject ) {
		return this.timestamp;
	}

	@Override
	public boolean canHappen( Object... inject ) {
		return true;
	}

	@Override
	public long toCodeLong() {
		return this.timestamp;
	}

	@Override
	public AlarmTime exact() {
		return new ExactTime( this.timestamp );
	}
}
