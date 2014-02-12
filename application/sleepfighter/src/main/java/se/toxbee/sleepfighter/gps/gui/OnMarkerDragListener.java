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
 * Interface for marker drag listeners.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Jan, 15, 2014
 */
public interface OnMarkerDragListener {
	/**
	 * Invoked when a marker was dragged.
	 *
	 * @param loc the new location of the marker.
	 */
	public void onMarkerDrag( GPSLatLng loc );

	/**
	 * Invoked when the user stopped dragging a marker.
	 *
	 * @param pointIndex the index of the point/maker in the list of points.
	 * @param loc the new location of the marker at the end of the dragging movement.
	 */
	public void onMarkerDragEnd( int pointIndex, GPSLatLng loc );

	/**
	 * Invoked when the user started dragging a marker.
	 *
	 * @param loc the new location of the marker at the start of the dragging movement.
	 */
	public void onMarkerDragStart( GPSLatLng loc );
}
