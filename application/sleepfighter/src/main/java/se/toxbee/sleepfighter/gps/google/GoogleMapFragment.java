/*
 * Copyright (c) 2014. See AUTHORS file.
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
 */
package se.toxbee.sleepfighter.gps.google;

import android.os.Bundle;

import com.google.android.gms.maps.SupportMapFragment;

/**
* GoogleMapFragment is adds a callback on-ready to SupportMapFragment.
*
* @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
* @version 1.0
* @since Jan, 08, 2014
*/
final class GoogleMapFragment extends SupportMapFragment {
	private MapReadyCallback callback;

	public void setMapCallback( MapReadyCallback callback ) {
		this.callback = callback;
	}

	@Override
	public void onActivityCreated( Bundle saved ) {
		super.onActivityCreated( saved );

		if ( this.callback != null ) {
			this.callback.onMapReady( this.getMap() );
		}
	}
}
