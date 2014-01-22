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
