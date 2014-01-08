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

import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.view.ViewGroup;

import java.util.List;

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
	public interface LocationGUIReceiver {
		/**
		 * Returns a FragmentActivity to add view to, etc.
		 *
		 * @return the activity.
		 */
		public FragmentActivity getActivity();

		/**
		 * Computes the padding to use when moving camera to polygon.
		 *
		 * @return the padding.
		 */
		public int computeMoveCameraPadding();

		/**
		 * Returns the fill-color to use.
		 *
		 * @return the color value.
		 */
		public int getPolygonFillColor();

		/**
		 * Returns the stroke-color to use.
		 *
		 * @return the color value.
		 */
		public int getPolygonStrokeColor();

		/**
		 * Returns the zoom factor to use.
		 *
		 * @return the zoom factor.
		 */
		public float getZoomFactor();

		/**
		 * Returns true if n(points) >= 3.
		 *
		 * @return true if >= 3 points.
		 */
		public boolean satisfiesPolygon();

		/**
		 * Invoked when the map is ready.
		 */
		public void onMapReady();

		public void onMapClick( GPSLatLng loc );
		public void onMarkerDrag( GPSLatLng loc );
		public void onMarkerDragEnd( int pointIndex, GPSLatLng loc );
		public void onMarkerDragStart( GPSLatLng loc );
		public boolean onMarkerClick( GPSLatLng loc );
	}

	/**
	 * Returs true if the GUI is alive.
	 *
	 * @return true if the GUI is alive.
	 */
	public boolean isAlive();

	/**
	 * Whether or not the current markers satisfy being a polygon (having 3 edges / markers).
	 *
	 * @return true if it satisfies being a polygon.
	 */
	public boolean satisfiesPolygon();

	/**
	 * Returns true if there are any points.
	 *
	 * @return true if any points.
	 */
	public boolean hasPoints();

	/**
	 * Returns a list of all the points.
	 *
	 * @return a list.
	 */
	public List<GPSLatLng> getPoints();

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

	/**
	 * Updates the GUI polygon, removing the old one,<br/>
	 * adding a new one if we have >= 3 points.
	 */
	public void updateGuiPolygon();

	/**
	 * Initializes the map, returning true if successful.
	 *
	 * @param viewContainer the parent view that will contain the map / gui-provider.
	 * @param errorOnFail if true, an init error should display an error to user.
	 * @return true if map was initalized.
	 */
	public boolean initMap( ViewGroup viewContainer, boolean errorOnFail );

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