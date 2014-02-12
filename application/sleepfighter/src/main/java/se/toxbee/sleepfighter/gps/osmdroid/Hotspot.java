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
import android.graphics.Canvas;
import android.graphics.RectF;
import android.view.MotionEvent;

import org.osmdroid.ResourceProxy;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Overlay;

/**
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Jan, 12, 2014
 */
public class Hotspot extends Overlay {
	public interface OnHotspotTap {
		public boolean onHotspotTap( MotionEvent e, MapView mapView, Hotspot hotspot );
	}

	private RectF bounds;
	protected OnHotspotTap listener;

	public Hotspot( Context ctx ) {
		super( ctx );
	}

	public Hotspot( ResourceProxy pResourceProxy ) {
		super( pResourceProxy );
	}

	public void set( RectF bounds, OnHotspotTap listener ) {
		this.setBounds( bounds );
		this.setListener( listener );
	}

	public void setListener( OnHotspotTap listener ) {
		this.listener = listener;
	}

	public void setBounds( RectF bounds ) {
		this.bounds = bounds;
	}

	public RectF getBounds() {
		return this.bounds;
	}

	@Override
	protected void draw( Canvas c, MapView osmv, boolean shadow ) {
		// Does nothing.
	}

	@Override
	public boolean onSingleTapConfirmed( MotionEvent e, MapView mapView ) {
		if ( this.listener == null ) {
			return false;
		}

		for ( int i = 0; i < e.getPointerCount(); ++i ) {
			float x = e.getX( i );
			float y = e.getY( i );

			if ( bounds.contains( x, y ) ) {
				return this.listener.onHotspotTap( e, mapView, this );
			}
		}

		return false;
	}
}
