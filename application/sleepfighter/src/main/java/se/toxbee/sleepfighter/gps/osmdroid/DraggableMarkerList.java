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
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;

import org.osmdroid.DefaultResourceProxyImpl;
import org.osmdroid.ResourceProxy;
import org.osmdroid.ResourceProxy.bitmap;
import org.osmdroid.api.IGeoPoint;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.MapView.Projection;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.OverlayItem;

import java.util.List;

/**
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Jan, 11, 2014
 */
public class DraggableMarkerList<Item extends OverlayItem> extends ItemizedIconOverlay<Item> {
	public static interface MarkerDragListener<T extends OverlayItem> {
		public void onMarkerDrag( T item );
		public void onMarkerDragEnd( T item );
		public void onMarkerDragStart( T item );
	}

	private MarkerDragListener<Item> listener;
	private Item inDragItem = null;

	public DraggableMarkerList( final List<Item> pList, final Drawable pDefaultMarker, final OnItemGestureListener<Item> pOnItemGestureListener, final ResourceProxy pResourceProxy ) {
		super( pList, pDefaultMarker, pOnItemGestureListener, pResourceProxy );
	}

	public DraggableMarkerList( final List<Item> pList, final OnItemGestureListener<Item> pOnItemGestureListener, final ResourceProxy pResourceProxy ) {
		this( pList, pResourceProxy.getDrawable( bitmap.marker_default ), pOnItemGestureListener, pResourceProxy );
	}

	public DraggableMarkerList( final Context pContext, final List<Item> pList, final OnItemGestureListener<Item> pOnItemGestureListener ) {
		this( pList, new DefaultResourceProxyImpl( pContext ).getDrawable( bitmap.marker_default ), pOnItemGestureListener, new DefaultResourceProxyImpl( pContext ) );
	}

	public DraggableMarkerList( final Context pContext, final List<Item> pList ) {
		this( pContext, pList, null );
	}

	public DraggableMarkerList<Item> setDragListener( MarkerDragListener<Item> listener ) {
		this.listener = listener;
		return this;
	}

	private boolean inDrag() {
		return this.inDragItem != null;
	}

	public List<Item> getItems() {
		return this.mItemList;
	}

	public boolean onTouchEvent( MotionEvent event, MapView mapView ) {
		boolean result = false;

		final Projection pj = mapView.getProjection();

		final int x = (int) event.getX();
		final int y = (int) event.getY();

		switch ( event.getAction() ) {
			case MotionEvent.ACTION_DOWN:
				Point p = new Point( 0, 0 );
				Point t = new Point( 0, 0 );
				pj.fromMapPixels( x, y, t );

				for ( Item item : this.mItemList ) {
					pj.toPixels( item.getPoint(), p );

					int dx = t.x - p.x;
					int dy = t.y - p.y;

					final Drawable marker = item.getMarker( 0 ) == null ? this.mDefaultMarker : item.getMarker( 0 );

					if ( this.hitTest( item, marker, dx, dy ) ) {
						this.inDragItem = item;
						result = true;
						this.listener.onMarkerDragStart( item );
						break;
					}
				}
				break;

			case MotionEvent.ACTION_MOVE:
				if ( this.inDrag() ) {
					result = true;
					this.setLocation( pj, x, y );
					this.listener.onMarkerDrag( this.inDragItem );
				}
				break;

			case MotionEvent.ACTION_UP:
				if ( this.inDrag() ) {
					this.setLocation( pj, x, y );
					this.listener.onMarkerDragEnd( this.inDragItem );
					result = true;
					this.inDragItem = null;
				}
				break;
		}

		if ( result ) {
			mapView.invalidate();
		}

		return result || super.onTouchEvent( event, mapView );
	}

	private void setLocation( Projection pj, int x, int y ) {
		IGeoPoint loc = pj.fromPixels( x, y );
		GeoPoint pt = this.inDragItem.getPoint();
		pt.setCoordsE6( loc.getLatitudeE6(), loc.getLongitudeE6() );
	}

	public void removeLast() {
		this.removeItem( this.size() - 1 );
	}
}
