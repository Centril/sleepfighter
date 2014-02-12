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
package se.toxbee.sleepfighter.gps.google;

import android.app.Activity;
import android.location.Location;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.common.collect.Lists;

import java.util.List;

import se.toxbee.fimpl.annotation.ProvidedImplementation;
import se.toxbee.sleepfighter.gps.gui.LocationGUIProvider;
import se.toxbee.sleepfighter.gps.gui.LocationGUIReceiver;
import se.toxbee.sleepfighter.model.gps.GPSLatLng;

/**
 * GoogleLocationProvider uses Google Maps API v2<br/>
 * to let the user manipulate the latlng points of a location filter area.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Jan 8, 2014
 */
@ProvidedImplementation(of = LocationGUIProvider.class, priority = Integer.MAX_VALUE)
public class GoogleLocationProvider implements LocationGUIProvider, OnMapClickListener, OnMarkerDragListener, MapReadyCallback {
	public static final int STROKE_WIDTH = 2;

	private GoogleMap googleMap = null;
	private List<Marker> markers = null;
	private Polygon poly = null;

	private LocationGUIReceiver receiver;

	@Override
	public void bind( LocationGUIReceiver receiver ) {
		this.receiver = receiver;
	}

	@Override
	public boolean isAvailable( boolean allowError ) {
		Activity activity = this.receiver.getActivity();

		// Google Play Services available?
		int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable( activity );

		if ( status == ConnectionResult.SUCCESS ) {
			return true;
		}

		if ( allowError ) {
			int requestCode = 10;
			GooglePlayServicesUtil.getErrorDialog( status, activity, requestCode ).show();
		}

		return false;
	}

	@Override
	public void initMap( ViewGroup viewContainer ) {
		// Make fragment.
		GoogleMapFragment fragment = new GoogleMapFragment();
		fragment.setMapCallback( this );

		// Attach it.
		FragmentManager fragManager = this.receiver.getActivity().getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragManager.beginTransaction();
		fragmentTransaction.add( viewContainer.getId(), fragment );
		fragmentTransaction.commit();
	}

	@Override
	public void onMapReady( GoogleMap map ) {
		this.googleMap = map;

		this.markers = Lists.newArrayList();

		this.configMap();

		this.receiver.onMapReady();
	}

	private void configMap() {
		// Enable all gestures.
		this.googleMap.getUiSettings().setAllGesturesEnabled( true );

		// Add button for my location.
		this.googleMap.setMyLocationEnabled( true );

		// Bind events for markers.
		this.googleMap.setOnMapClickListener( this );
		this.googleMap.setOnMarkerDragListener( this );

		// Setup polygon.
		PolygonOptions options = new PolygonOptions()
			.add( new LatLng( 0, 0 ) ) // dummy point.
			.visible( false )
			.geodesic( true )
			.fillColor( this.receiver.getPolygonFillColor() )
			.strokeColor( this.receiver.getPolygonStrokeColor() )
			.strokeWidth( STROKE_WIDTH );

		this.poly = this.googleMap.addPolygon( options );
	}

	@Override
	public boolean satisfiesPolygon() {
		return this.markers.size() >= 3;
	}

	public boolean hasPoints() {
		return !this.markers.isEmpty();
	}

	public void addPoint( GPSLatLng loc ) {
		LatLng pos = this.convertLocation( loc );
		MarkerOptions options = new MarkerOptions().flat( true ).draggable( true ).position( pos );
		this.markers.add( this.googleMap.addMarker( options ) );
	}

	public void undoLastPoint() {
		this.markers.remove( this.markers.size() - 1 ).remove();
	}

	public void clearPoints() {
		for ( Marker marker : this.markers ) {
			marker.remove();
		}

		this.markers.clear();
	}

	private GPSLatLng convertLocation( LatLng loc ) {
		return new GPSLatLng( loc.latitude, loc.longitude );
	}

	private LatLng convertLocation( Location loc ) {
		return new LatLng( loc.getLatitude(), loc.getLongitude() );
	}

	private LatLng convertLocation( GPSLatLng loc ) {
		return new LatLng( loc.getLat(), loc.getLng() );
	}

	public void updateGuiPolygon() {
		boolean isPoly = this.satisfiesPolygon();
		if ( isPoly ) {
			List<LatLng> points = Lists.newArrayListWithCapacity( this.markers.size() );
			for ( Marker marker : this.markers ) {
				points.add( marker.getPosition() );
			}

			this.poly.setPoints( points );
		}

		this.poly.setVisible( isPoly );
	}

	@Override
	public void initCameraToPolygon() {
		// Add listener that moves camera so that all markers are in users view.
		this.googleMap.setOnCameraChangeListener( new OnCameraChangeListener() {
			@Override
			public void onCameraChange( CameraPosition arg0 ) {
				// Move camera.
				moveCameraToPolygon( false );

				// Remove listener to prevent position reset on camera move.
				googleMap.setOnCameraChangeListener( null );
			}
		} );
	}

	public void moveCamera( Location loc, boolean animate ) {
		LatLng pos = this.convertLocation( loc );
		CameraUpdate update = CameraUpdateFactory.newLatLngZoom( pos, this.receiver.getZoomFactor() );
		this.moveCamera( update, animate );
	}

	@Override
	public void moveCameraToPolygon( boolean animate ) {
		if ( this.markers.isEmpty() ) {
			return;
		}

		LatLngBounds.Builder builder = LatLngBounds.builder();
		for ( Marker marker : this.markers ) {
			builder.include( marker.getPosition() );
		}

		CameraUpdate update = CameraUpdateFactory.newLatLngBounds( builder.build(), receiver.computeMoveCameraPadding() );
		this.moveCamera( update, animate );
	}

	private void moveCamera( CameraUpdate update, boolean animate ) {
		if ( animate ) {
			this.googleMap.animateCamera( update );
		} else {
			this.googleMap.moveCamera( update );
		}
	}

	@Override
	public void onMapClick( LatLng latLng ) {
		this.receiver.onMapClick( this.convertLocation( latLng ) );
	}

	@Override
	public void onMarkerDrag( Marker marker ) {
		this.receiver.onMarkerDrag( this.convertLocation( marker.getPosition() ) );
	}

	@Override
	public void onMarkerDragEnd( Marker marker ) {
		this.receiver.onMarkerDragEnd( this.markers.indexOf( marker ), this.convertLocation( marker.getPosition() ) );
	}

	@Override
	public void onMarkerDragStart( Marker marker ) {
		this.receiver.onMarkerDragStart( this.convertLocation( marker.getPosition() ) );
	}
}
