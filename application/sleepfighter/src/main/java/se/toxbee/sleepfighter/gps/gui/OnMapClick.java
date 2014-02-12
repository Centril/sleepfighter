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

package se.toxbee.sleepfighter.gps.gui;

import se.toxbee.sleepfighter.model.gps.GPSLatLng;

/**
 * Interface for map click listeners.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Jan, 15, 2014
 */
public interface OnMapClick {
	/**
	 * Invoked when the map is clicked.
	 *
	 * @param loc the location that was clicked in lat long coordinates.
	 * @return true if the click was handled.
	 */
	public boolean onMapClick( GPSLatLng loc );
}
