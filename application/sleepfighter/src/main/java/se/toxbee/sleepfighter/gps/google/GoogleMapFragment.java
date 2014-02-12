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
