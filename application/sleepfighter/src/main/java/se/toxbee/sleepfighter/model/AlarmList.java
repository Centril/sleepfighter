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

import com.badlogic.gdx.utils.IntArray;
import com.google.common.collect.Ordering;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.List;

import se.toxbee.sleepfighter.model.Alarm.AlarmEvent;
import se.toxbee.commons.collect.IdObservableList;

/**
 * {@link AlarmList} manages all the existing alarms.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.1
 * @since Sep 18, 2013
 */
public class AlarmList extends IdObservableList<Alarm> {
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
	 * Unnamed placements.
	 * --------------------------------
	 */

	@Override
	protected void fireEvent( Event e ) {
		switch( e.operation() ) {
		case UPDATE:
			// order() will cause an infinite loop if we dont bail here.
			return;

		case ADD:
			int maxId = this.maxId();

			// Set placement and order for all added alarms.
			for ( Object obj : e.elements() ) {
				Alarm curr = (Alarm) obj;
				this.setPlacement( curr );
				curr.setOrder( ++maxId );
			}

			// Reorder the list.
			this.order();

			// FALLTROUGH
		default:
			super.fireEvent( e );
		}
	}

	/**
	 * Returns the maximum id in the list.
	 *
	 * @return the maximum id.
	 */
	public int maxId() {
		int maxId = 0;
		if ( this.sortMode.field() == SortMode.Field.ID ) {
			// Optimized since we're already sorting by id.
			maxId = this.get( this.sortMode.direction() ? this.size() - 1 : 0 ).getId();
		} else {
			for ( Alarm elem : this.delegate() ) {
				int currId = elem.getId();
				if ( currId > maxId ) {
					maxId = currId;
				}
			}
		}

		return maxId;
	}

	private void setPlacement( Alarm alarm ) {
		if ( alarm.isUnnamed() ) {
			alarm.setUnnamedPlacement( this.findLowestUnnamedPlacement() );
		}
	}

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

	/* --------------------------------
	 * Public methods: etc.
	 * --------------------------------
	 */

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

	/* --------------------------------
	 * Public methods: Sorting.	 * --------------------------------
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
	 * @return true if the mode was changed.
	 */
	public boolean order( SortMode mode ) {
		if ( this.sortMode.equals( mode ) ) {
			return false;
		}

		this.sortMode = mode;
		this.ordering = mode.ordering();
		this.order();
		return true;
	}
}