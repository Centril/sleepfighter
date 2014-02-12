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
package se.toxbee.sleepfighter.gps.gui;

import android.location.Location;
import android.view.ViewGroup;

import se.toxbee.sleepfighter.model.gps.GPSLatLng;

/**
 * LocationGUIProvider provides an interface for libraries<br/>
 * to let the user manipulate the latlng points of a location filter area.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Jan 8, 2014
 */
public interface LocationGUIProvider {
	/* --------------------------------
	 * Map initialization and status.
	 * --------------------------------
	 */

	/**
	 * Binds the receiver to the provider.
	 *
	 * @param receiver the receiver to bind.
	 */
	public void bind( LocationGUIReceiver receiver );

	/**
	 * Returns whether or not the provider is available.
	 *
	 * @param allowError if unavailable, and allowError is true,
	 *                      the provider is allowed to show an error to the user.
	 * @return true if available.
	 */
	public boolean isAvailable( boolean allowError );

	/**
	 * Initializes the map.
	 *
	 * @param viewContainer the parent view that will contain the map / gui-provider.
	 */
	public void initMap( ViewGroup viewContainer );

	/* --------------------------------
	 * Points / Markers related.
	 * --------------------------------
	 */

	/**
	 * Returns true if there are any points.
	 *
	 * @return true if any points.
	 */
	public boolean hasPoints();

	/**
	 * Adds a marker on map at loc.
	 *
	 * @param loc the loc/point to add maker at.
	 */
	public void addPoint( GPSLatLng loc );

	/**
	 * Removes the last added marker/point.
	 */
	public void undoLastPoint();

	/**
	 * Clears all markers/points.
	 */
	public void clearPoints();

	/* --------------------------------
	 * Polygon related.
	 * --------------------------------
	 */

	/**
	 * Whether or not the current markers satisfy being a polygon (having 3 edges / markers).
	 *
	 * @return true if it satisfies being a polygon.
	 */
	public boolean satisfiesPolygon();

	/**
	 * Updates the GUI polygon, removing the old one,<br/>
	 * adding a new one if we have >= 3 points.
	 */
	public void updateGuiPolygon();

	/* --------------------------------
	 * Zoom & Location related.
	 * --------------------------------
	 */

	/**
	 * Initializes the camera to the polygon.<br/>
	 * This is the same as moveCameraToPolygon(false) but wrapped in a listener.
	 */
	public void initCameraToPolygon();

	/**
	 * Moves and zooms the camera so that all the markers are visible in users view.
	 *
	 * @param animate whether or not to animate the movement.
	 */
	public void moveCameraToPolygon( boolean animate );

	/**
	 * Moves the camera to a given a Location.
	 *
	 * @param loc the Location object to use for location.
	 * @param animate whether or not to animate the movement.
	 */
	public void moveCamera( Location loc, boolean animate );
}