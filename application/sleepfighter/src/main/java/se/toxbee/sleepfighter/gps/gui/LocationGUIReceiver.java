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

import android.support.v4.app.FragmentActivity;

/**
 * LocationGUIReceiver receives and communicates with a {@link LocationGUIProvider}.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Jan, 15, 2014
 */
public interface LocationGUIReceiver extends OnMapClick, OnMarkerDragListener {
	/**
	 * Returns a FragmentActivity to add view to, etc.
	 *
	 * @return the activity.
	 */
	public FragmentActivity getActivity();

	/* --------------------------------
	 * Zoom & Location related.
	 * --------------------------------
	 */

	/**
	 * Computes the padding to use when moving camera to polygon.
	 *
	 * @return the padding.
	 */
	public int computeMoveCameraPadding();

	/**
	 * Moves the user to its current location.
	 */
	public void moveToCurrentLocation();

	/**
	 * Returns the zoom factor to use.
	 *
	 * @return the zoom factor.
	 */
	public float getZoomFactor();

	/* --------------------------------
	 * Polygon related.
	 * --------------------------------
	 */

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
	 * Returns true if n(points) >= 3.
	 *
	 * @return true if >= 3 points.
	 */
	public boolean satisfiesPolygon();

	/* --------------------------------
	 * Map Events.
	 * --------------------------------
	 */

	/**
	 * Invoked when the map is ready.
	 */
	public void onMapReady();
}
