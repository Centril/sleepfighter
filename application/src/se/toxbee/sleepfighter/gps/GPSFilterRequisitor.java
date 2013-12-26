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
package se.toxbee.sleepfighter.gps;

import se.toxbee.sleepfighter.model.gps.GPSFilterArea;
import se.toxbee.sleepfighter.model.gps.GPSFilterAreaSet;
import se.toxbee.sleepfighter.model.gps.GPSLatLng;
import se.toxbee.sleepfighter.preference.AppPreferenceManager;
import se.toxbee.sleepfighter.preference.LocationFilterPreferences;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.util.Log;

/**
 * GPSFilterRequisitor is responsible for checking that the user's<br/>
 * current location is within allowed areas or not.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Oct 8, 2013
 */
public class GPSFilterRequisitor {
	private static final String TAG = GPSFilterRequisitor.class.getSimpleName();

	private GPSFilterAreaSet set;
	private LocationFilterPreferences prefs;

	/**
	 * Constructs the GPSFilterRequisitor given a set of areas.
	 */
	public GPSFilterRequisitor( GPSFilterAreaSet set, AppPreferenceManager prefManager ) {
		this.set = set;
		this.prefs = prefManager.locFilter;
	}

	/**
	 * Is the requisitor satisfied? In other words, are the requirements met?
	 *
	 * @return true if requirements are met.
	 */
	public boolean isSatisfied( Context context ) {
		// Not globally enabled or no enabled and valid areas? Then we're satisfied!
		if ( !(this.prefs.isEnabled() && this.set.hasEnabledAndValid()) ) {
			return this.returnSatisfaction( true );
		}

		// Get last known location.
		GPSFilterLocationRetriever locRet = new GPSFilterLocationRetriever( new Criteria() );
		Location loc = locRet.getLocation( context );

		int maxAge = this.prefs.maxAllowedAge();
		if ( maxAge > 0 ) {
			long locAge = System.currentTimeMillis() - loc.getTime();
			if ( locAge > (long)maxAge * 60000 ) {
				// Last known location is too old, skip location filter.
				return this.returnSatisfaction( true );
			}
		}

		GPSLatLng pos = new GPSLatLng( loc.getLatitude(), loc.getLongitude() );

		// Partition!
		GPSFilterAreaSet[] partition = this.modePartitionSet();

		// Exclude areas first.
		for ( GPSFilterArea area : partition[0] ) {
			if ( area.contains( pos ) ) {
				return this.returnSatisfaction( false );
			}
		}

		// Include areas now.
		if ( partition[1].isEmpty() ) {
			return this.returnSatisfaction( true );
		} else {
			// At least one match needed.
			for ( GPSFilterArea area : partition[1] ) {
				if ( area.contains( pos ) ) {
					return this.returnSatisfaction( true );
				}
			}

			return this.returnSatisfaction( false );
		}
	}

	private boolean returnSatisfaction( boolean isSatisfied ) {
		Log.d( TAG, "I'm " + (isSatisfied ? "" : " NOT " ) + "satisfied" );
		return isSatisfied;
	}

	/**
	 * Partitions the set into one for includes, one for excludes.
	 *
	 * @return [excludes, includes]
	 */
	private GPSFilterAreaSet[] modePartitionSet() {
		GPSFilterAreaSet includes = new GPSFilterAreaSet();
		GPSFilterAreaSet excludes = new GPSFilterAreaSet();

		for ( GPSFilterArea area : this.set ) {
			// Take the opportunity to get rid of non-enabled & invalid areas.
			if ( !(area.isValid() && area.isEnabled()) ) {
				continue;
			}

			// Partition current area.
			switch ( area.getMode() ) {
			case INCLUDE:
				includes.add( area );
				break;

			case EXCLUDE:
				excludes.add( area );
				break;
			}
		}

		// And we're done.
		return new GPSFilterAreaSet[] { excludes, includes };
	}
}