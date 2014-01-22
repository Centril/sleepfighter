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
