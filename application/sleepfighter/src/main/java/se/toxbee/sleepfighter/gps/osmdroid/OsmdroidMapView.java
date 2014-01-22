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
