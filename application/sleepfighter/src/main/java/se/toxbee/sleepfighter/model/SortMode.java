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

import se.toxbee.sleepfighter.model.Alarm.AlarmEvent;
import se.toxbee.sleepfighter.model.time.AlarmTime;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.Ordering;

/**
 * {@link SortMode} models the sort mode of {@link AlarmList}.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Nov 18, 2013
 */
public class SortMode {
	/**
	 * {@link SortMode.Field} models the fields that an {@link AlarmList} can be sorted on.
	 *
	 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
	 * @version 1.0
	 * @since Nov 18, 2013
	 */
	public static enum Field {
		ID( Ordering.<Alarm>natural() ),
		NAME( new Function<Alarm, String>() {
			@Override
			public String apply( Alarm input ) {
				return input.printName();
			}
		} ),
		TIMESTAMP( new Function<Alarm, Long>() {
			@Override
			public Long apply( Alarm input ) {
				return input.scheduledTimestamp();
			}
		} ),
		ALARM_TIME( new Function<Alarm, AlarmTime>() {
			@Override
			public AlarmTime apply( Alarm input ) {
				return input.getTime().exact();
			}
		} ),
		MANUAL( new Function<Alarm, Integer>() {
			@Override
			public Integer apply( Alarm input ) {
				return input.getOrder();
			}
		} );

		protected Ordering<Alarm> ordering;
		protected Ordering<Alarm> ordering() {
			return this.ordering;
		}
		Field( Ordering<Alarm> o ) {
			this.ordering = o;
		}

		Field( Function<Alarm, ? extends Comparable<?>> fn ) {
			this( Ordering.natural().nullsLast().onResultOf( fn ).compound( Ordering.natural() ) );
		}

		protected boolean requiresReordering( AlarmEvent evt ) {
			switch ( this ) {
			case MANUAL:
				return evt.getModifiedField() == Alarm.Field.ORDER;

			case NAME:
				return evt.getModifiedField() == Alarm.Field.NAME;

			case ALARM_TIME:
			case TIMESTAMP:
				return evt instanceof Alarm.ScheduleChangeEvent;

			default:
				return false;
			}
		}

		/**
		 * Returns a {@link Field} given the ordinality of the field.
		 *
		 * @param ordinal the ordinality.
		 * @return the {@link Field}.
		 */
		public static Field from( int ordinal ) {
			return values()[ordinal];
		}
	}

	private Field field;
	private boolean direction;

	/**
	 * Constructs the default sort mode.
	 */
	public SortMode() {
		this( Field.ID, true );
	}

	/**
	 * Constructs a sort mode.
	 *
	 * @param field see {@link #field()}
	 * @param direction see {@link #direction()}
	 */
	public SortMode( Field field, boolean direction ) {
		this.field = Preconditions.checkNotNull( field );
		this.direction = Preconditions.checkNotNull( direction );
	}

	/**
	 * Returns the field being sorted primarily on.
	 *
	 * @return the field.
	 */
	public Field field() {
		return this.field;
	}

	/**
	 * Returns the sorting direction used.
	 *
	 * @return true for ascending, false for descending.
	 */
	public boolean direction() {
		return this.direction;
	}

	/**
	 * Returns an {@link Ordering} corresponding to this mode.
	 *
	 * @return the ordering.
	 */
	public Ordering<Alarm> ordering() {
		Ordering<Alarm> ordering = this.field.ordering();
		return this.direction ? ordering : ordering.reverse();
	}

	/**
	 * Returns true if reordering is required as a result of evt being fired.
	 *
	 * @param evt the {@link AlarmEvent} that was fired.
	 * @return true = reorder.
	 */
	public boolean requiresReordering( AlarmEvent evt ) {
		// Facade.
		return this.field.requiresReordering( evt );
	}

	/**
	 * Returns true if the given {@link SortMode} is reverse to this.
	 *
	 * @param rhs the other {@link SortMode}.
	 * @return true if it is reverse.
	 */
	public boolean isReverse( SortMode rhs ) {
		return this.field == rhs.field && this.direction != rhs.direction;
	}

	@Override
	public boolean equals( Object o ) {
		if ( this == o ) {
			return true;
		}

		if ( o instanceof SortMode ) {
			SortMode rhs = (SortMode) o;
			return this.field == rhs.field && this.direction == rhs.direction;
		}

		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode( this.field, this.direction );
	}
}
