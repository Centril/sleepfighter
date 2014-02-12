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
