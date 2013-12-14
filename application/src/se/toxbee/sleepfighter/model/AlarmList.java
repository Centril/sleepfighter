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

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.List;

import se.toxbee.sleepfighter.model.Alarm.AlarmEvent;
import se.toxbee.sleepfighter.utils.collect.ObservableList;
import se.toxbee.sleepfighter.utils.message.Message;
import se.toxbee.sleepfighter.utils.message.MessageBus;

import com.badlogic.gdx.utils.IntArray;
import com.google.common.collect.Ordering;

/**
 * {@link AlarmList} manages all the existing alarms.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.1
 * @since Sep 18, 2013
 */
public class AlarmList extends ObservableList<Alarm> {
	/* --------------------------------
	 * Fields: Sorting.
	 * --------------------------------
	 */

	private SortMode sortMode;
	private Ordering<Alarm> ordering;

	/* --------------------------------
	 * Constructors.
	 * --------------------------------
	 */


	/**
	 * Constructs the list with no initial alarms.
	 */	
	public AlarmList() {
		this( new ArrayList<Alarm>() );
	}

	/**
	 * Constructs the list starting with given alarms.
	 *
	 * @param alarms list of given alarms. Don't modify this list directly.
	 */
	public AlarmList( List<Alarm> alarms ) {
		this.setDelegate( alarms );

		this.sortMode = new SortMode();
		this.ordering = this.sortMode.ordering();
	}

	/* --------------------------------
	 * Event interception / DI.
	 * --------------------------------
	 */

	@Override
	protected void fireEvent( Event e ) {
		if ( !e.operation().isRemove() ) {
			// Intercept add/update events and inject message bus.
			MessageBus<Message> bus = this.getMessageBus();

			if ( e.operation() == Operation.ADD ) {
				for ( Object obj : e.elements() ) {
					((Alarm) obj).setMessageBus( bus );
				}
			} else if ( e.operation() == Operation.UPDATE ) {
				this.get( e.index() ).setMessageBus( bus );
			}

			// Reorder the list.
			this.order();
		}

		super.fireEvent( e );
	}

	public void setMessageBus( MessageBus<Message> messageBus ) {
		super.setMessageBus( messageBus );

		for ( Alarm alarm : this ) {
			alarm.setMessageBus( messageBus );
		}
	}

	/* --------------------------------
	 * Public methods: etc.
	 * --------------------------------
	 */

	/**
	 * <p>Finds the lowest unnamed placement number.</p>
	 *
	 * <p>Time complexity: O(n),<br/>
	 * Space complexity: O(n).</p>
	 *
	 * @see Alarm#getUnnamedPlacement()
	 * @return the lowest unnamed placement number.
	 */
	public int findLowestUnnamedPlacement() {
		// Bail early if we can.
		if ( this.size() == 0 ) {
			return 1;
		}

		// First extract the unnamed placements defined.
		IntArray arr = new IntArray(); 
		arr.ordered = false;
		for ( Alarm alarm : this ) {
			if ( alarm.isUnnamed() ) {
				int place = alarm.getUnnamedPlacement();
				if ( place > 0 ) {
					arr.add( place );
				}
			}
		}

		// Another opportunity to bail.
		if ( arr.size == 0 ) {
			return 1;
		}

		arr.shrink();

		// Set all bits < N
		BitSet bits = new BitSet( arr.size );
		for ( int i = 0; i < arr.size; ++i ) {
			int v = arr.get( i ) - 1;
			if ( v < arr.size ) {
				bits.set( v );
			}
		}

		// Find first false bit.
		return bits.nextClearBit( 0 ) + 1;
	}

	/**
	 * Returns info about the earliest alarm.<br/>
	 * The info contains info about milliseconds and the alarm.
	 *
	 * @param now current time in UNIX epoch timestamp.
	 * @return info about the earliest alarm. 
	 */
	public AlarmTimestamp getEarliestAlarm( long now ) {
		Long millis = null;
		int earliestIndex = -1;

		for ( int i = 0; i < this.size(); i++ ) {
			Long currMillis = this.get( i ).getNextMillis( now );
			if ( currMillis != Alarm.NEXT_NON_REAL && (millis == Alarm.NEXT_NON_REAL || millis > currMillis) ) {
				earliestIndex = i;
				millis = currMillis;
			}
		}

		return earliestIndex == -1 ? AlarmTimestamp.INVALID : new AlarmTimestamp( millis, this.get( earliestIndex) );
	}
	
	/**
	 * Returns an the alarm with the unique id provided.
	 * 
	 * @param id the unique id of the alarm.
	 * @return the alarm, if not found it returns null.
	 */
	public Alarm getById( int id ) {
		for ( Alarm a : this ) {
			if ( a.getId() == id ) {
				return a;
			}
		}

		return null;
	}

	/* --------------------------------
	 * Public methods: Sorting.
	 * --------------------------------
	 */

	/**
	 * Returns the current sort mode.
	 *
	 * @return the mode.
	 */
	public SortMode getSortMode() {
		return this.sortMode;
	}

	/**
	 * Orders the list using the result of {@link #getSortMode()}.
	 */
	public void order() {
		Collections.sort( this, this.ordering );
	}

	/**
	 * Orders if needed according to current result of {@link #getSortMode()}.
	 *
	 * @param evt the event.
	 */
	public boolean orderIfNeeded( AlarmEvent evt ) {
		if ( this.sortMode.requiresReordering( evt ) ) {
			this.order();
			return true;
		}

		return false;
	}

	/**
	 * Sets the result of {@link #getSortMode()} and calls {@link #order()}.
	 *
	 * @param mode the {@link SortMode} to use.
	 */
	public void order( SortMode mode ) {
		if ( this.sortMode.equals( mode ) ) {
			return;
		}

		SortMode old = this.sortMode;
		this.sortMode = mode;

		if ( old.isReverse( this.sortMode ) ) {
			this.ordering = this.ordering.reverse();
		}

		this.order();
	}
}