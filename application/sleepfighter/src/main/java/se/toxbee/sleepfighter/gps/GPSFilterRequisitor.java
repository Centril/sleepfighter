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

package se.toxbee.sleepfighter.gps;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.util.Log;

import se.toxbee.sleepfighter.model.gps.GPSFilterArea;
import se.toxbee.sleepfighter.model.gps.GPSFilterAreaSet;
import se.toxbee.sleepfighter.model.gps.GPSLatLng;
import se.toxbee.sleepfighter.preference.AppPreferenceManager;
import se.toxbee.sleepfighter.preference.LocationFilterPreferences;

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