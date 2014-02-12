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

package se.toxbee.sleepfighter.gps.osmdroid;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;

import org.osmdroid.ResourceProxy;
import org.osmdroid.tileprovider.MapTileProviderBase;
import org.osmdroid.util.BoundingBoxE6;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

/**
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Jan, 11, 2014
 */
public class OsmdroidMapView extends MapView {
	public OsmdroidMapView( Context context, int tileSizePixels, ResourceProxy resourceProxy, MapTileProviderBase tileProvider, Handler tileRequestCompleteHandler, AttributeSet attrs ) {
		super( context, tileSizePixels, resourceProxy, tileProvider, tileRequestCompleteHandler, attrs );
	}

	public OsmdroidMapView( Context context, AttributeSet attrs ) {
		super( context, attrs );
	}

	public OsmdroidMapView( Context context, int tileSizePixels ) {
		super( context, tileSizePixels );
	}

	public OsmdroidMapView( Context context, int tileSizePixels, ResourceProxy resourceProxy ) {
		super( context, tileSizePixels, resourceProxy );
	}

	public OsmdroidMapView( Context context, int tileSizePixels, ResourceProxy resourceProxy, MapTileProviderBase aTileProvider ) {
		super( context, tileSizePixels, resourceProxy, aTileProvider );
	}

	public OsmdroidMapView( Context context, int tileSizePixels, ResourceProxy resourceProxy, MapTileProviderBase aTileProvider, Handler tileRequestCompleteHandler ) {
		super( context, tileSizePixels, resourceProxy, aTileProvider, tileRequestCompleteHandler );
	}

	/**
	 * Zoom the map to enclose the specified bounding box, as closely as possible.
	 * Must be called after display layout is complete, or screen dimensions are not known, and
	 * will always zoom to center of zoom  level 0.
	 * Suggestion: Check getScreenRect(null).getHeight() > 0
	 */
	public void zoomToBoundingBox(final BoundingBoxE6 boundingBox, boolean animate) {
		int mZoomLevel = this.getZoomLevel();

		final BoundingBoxE6 currentBox = getBoundingBox();

		// Calculated required zoom based on latitude span
		final double maxZoomLatitudeSpan = mZoomLevel == getMaxZoomLevel() ?
		                                   currentBox.getLatitudeSpanE6() :
		                                   currentBox.getLatitudeSpanE6() / Math.pow(2, getMaxZoomLevel() - mZoomLevel);

		final double requiredLatitudeZoom =
				getMaxZoomLevel() -
				Math.ceil(Math.log(boundingBox.getLatitudeSpanE6() / maxZoomLatitudeSpan) / Math.log(2));


		// Calculated required zoom based on longitude span
		final double maxZoomLongitudeSpan = mZoomLevel == getMaxZoomLevel() ?
		                                    currentBox.getLongitudeSpanE6() :
		                                    currentBox.getLongitudeSpanE6() / Math.pow(2, getMaxZoomLevel() - mZoomLevel);

		final double requiredLongitudeZoom =
				getMaxZoomLevel() -
				Math.ceil(Math.log(boundingBox.getLongitudeSpanE6() / maxZoomLongitudeSpan) / Math.log(2));


		// Zoom to boundingBox center, at calculated maximum allowed zoom level
		getController().setZoom((int)(
				requiredLatitudeZoom < requiredLongitudeZoom ?
				requiredLatitudeZoom : requiredLongitudeZoom));

		GeoPoint center =
				new GeoPoint(boundingBox.getCenter().getLatitudeE6(), boundingBox.getCenter()
				                                                                 .getLongitudeE6());

		if ( animate ) {
			getController().animateTo( center );
		} else {
			getController().setCenter( center );
		}
	}
}
