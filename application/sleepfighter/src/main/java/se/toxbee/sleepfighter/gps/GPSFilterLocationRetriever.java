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

import android.app.PendingIntent;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

/**
 * GPSFilterLocation is responsible for retrieving the users last known location.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Oct 8, 2013
 */
public class GPSFilterLocationRetriever {
	private Criteria criteria;
	private LocationManager manager;
	private String provider;

	/**
	 * Constructs a GPSFilterLocationRetriever with given criteria.
	 */
	public GPSFilterLocationRetriever( Criteria criteria ) {
		this.criteria = criteria;
	}

	/**
	 * Returns the system location manager.
	 *
	 * @param context android context.
	 * @return the manager.
	 */
	public LocationManager getManager( Context context ) {
		if ( this.manager == null ) {
			this.manager = (LocationManager) context.getSystemService( Context.LOCATION_SERVICE );
		}

		return this.manager;
	}

	/**
	 * Returns the best provider, or null.
	 *
	 * @param context android context.
	 * @return the provider.
	 */
	public String getBestProvider( Context context ) {
		if ( this.provider == null ) {
			this.provider = this.getManager( context ).getBestProvider( this.criteria, true );
		}

		return this.provider;
	}

	/**
	 * Returns the last known location.
	 *
	 * @param context android context.
	 * @return the last known location, or null if provider wasn't available.
	 */
	public Location getLocation( Context context ) {
		LocationManager manager = this.getManager( context );
		String provider = this.getBestProvider( context );
		return provider == null ? null : manager.getLastKnownLocation( provider );
	}

	/**
	 * Requests a single location fix/update given context, listener, and a looper 
	 *
	 * @param context android context.
	 * @param listener the listener to be notified of location.
	 */
	public void requestSingleUpdate( Context context, LocationListener listener ) {
		this.getManager( context ).requestSingleUpdate( this.getBestProvider( context ), listener, null );
	}

	/**
	 * Requests a single location fix/update given context, listener, and a looper 
	 *
	 * @param context android context.
	 * @param intent the pending intent that gets updates.
	 */
	public void requestSingleUpdate( Context context, PendingIntent intent ) {
		this.getManager( context ).requestSingleUpdate( this.getBestProvider( context ), intent );
	}

	/**
	 * Requests for pending location fixes/updates given context, looper, pending intent, minimum interval and minimum distance.
	 *
	 * @param context android context.
	 * @param intent the pending intent that gets updates.
	 * @param minInterval the minimum interval between updates.
	 * @param minDistance the minimum distance before an update is made.
	 */
	public void requestUpdates( Context context, PendingIntent intent, long minInterval, float minDistance ) {
		LocationManager manager = this.getManager( context );
		manager.requestLocationUpdates( minInterval, minDistance, this.criteria, intent );
	}
}