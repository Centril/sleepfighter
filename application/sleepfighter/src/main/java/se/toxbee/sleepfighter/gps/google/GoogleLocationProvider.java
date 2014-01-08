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
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
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

import se.toxbee.sleepfighter.gps.LocationGUIProvider;
import se.toxbee.sleepfighter.model.gps.GPSLatLng;

/**
 * GoogleLocationProvider uses Google Maps API v2<br/>
 * to let the user manipulate the latlng points of a location filter area.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Jan 8, 2014
 */
public class GoogleLocationProvider implements LocationGUIProvider, OnMapClickListener, OnMarkerDragListener, OnMarkerClickListener, MapReadyCallback {
	public static final int STROKE_WIDTH = 2;

	public GoogleLocationProvider( LocationGUIReceiver receiver ) {
		this.receiver = receiver;
	}

	private final LocationGUIReceiver receiver;

	private GoogleMap googleMap = null;
	private List<Marker> markers = null;
	private Polygon poly = null;

	@Override
	public boolean initMap( ViewGroup viewContainer, boolean errorOnFail ) {
		this.markers = Lists.newArrayList();
		int status = this.isServiceAvailable();
		if ( status == ConnectionResult.SUCCESS ) {
			// Google Play Services are available.
			if ( this.googleMap == null ) {
				// Make fragment.
				GoogleMapFragment fragment = new GoogleMapFragment();
				fragment.setMapCallback( this );

				// Attach it.
				FragmentManager fragManager = this.receiver.getActivity().getSupportFragmentManager();
				FragmentTransaction fragmentTransaction = fragManager.beginTransaction();
				fragmentTransaction.add( viewContainer.getId(), fragment );
				fragmentTransaction.commit();
			}

			return true;
		} else if ( errorOnFail ) {
			// Google Play Services are not available.
			int requestCode = 10;
			GooglePlayServicesUtil.getErrorDialog( status, this.receiver.getActivity(), requestCode ).show();
		}

		return false;
	}

	private int isServiceAvailable() {
		return GooglePlayServicesUtil.isGooglePlayServicesAvailable( this.receiver.getActivity() );
	}

	@Override
	public boolean isAlive() {
		return this.googleMap != null;
	}

	@Override
	public void onMapReady( GoogleMap map ) {
		this.googleMap = map;

		if ( !this.isAlive() ) {
			return;
		}

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
		this.googleMap.setOnMarkerClickListener( this );
	}

	@Override
	public boolean satisfiesPolygon() {
		return this.markers.size() >= 3;
	}

	public boolean hasPoints() {
		return !this.markers.isEmpty();
	}

	@Override
	public List<GPSLatLng> getPoints() {
		List<GPSLatLng> points = Lists.newArrayListWithCapacity( this.markers.size() );
		for ( Marker marker : this.markers ) {
			points.add( this.fromLocation( marker.getPosition() ) );
		}

		return points;
	}

	private GPSLatLng fromLocation( LatLng loc ) {
		return new GPSLatLng( loc.latitude, loc.longitude );
	}

	public void addPoint( GPSLatLng loc ) {
		if ( !this.isAlive() ) {
			return;
		}

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

	private LatLng convertLocation( Location loc ) {
		return new LatLng( loc.getLatitude(), loc.getLongitude() );
	}

	private LatLng convertLocation( GPSLatLng loc ) {
		return new LatLng( loc.getLat(), loc.getLng() );
	}

	public void updateGuiPolygon() {
		if ( !this.isAlive() ) {
			return;
		}

		if ( this.poly != null ) {
			this.poly.remove();
		}

		// A polygon is at minimum a triangle.
		if ( !this.satisfiesPolygon() ) {
			return;
		}

		PolygonOptions options = new PolygonOptions();
		options.geodesic( true )
		       .strokeWidth( STROKE_WIDTH )
		       .fillColor( this.receiver.getPolygonFillColor() )
		       .strokeColor( this.receiver.getPolygonStrokeColor() );

		for ( Marker marker : this.markers ) {
			options.add( marker.getPosition() );
		}

		this.poly = this.googleMap.addPolygon( options );
	}

	@Override
	public void initCameraToPolygon() {
		if ( !this.isAlive() ) {
			return;
		}

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

		final LatLngBounds.Builder builder = LatLngBounds.builder();
		for ( Marker marker : this.markers ) {
			builder.include( marker.getPosition() );
		}

		CameraUpdate update = CameraUpdateFactory.newLatLngBounds( builder.build(), receiver.computeMoveCameraPadding() );
		this.moveCamera( update, animate );
	}

	private void moveCamera( CameraUpdate update, boolean animate ) {
		if ( !this.isAlive() ) {
			return;
		}

		if ( animate ) {
			this.googleMap.animateCamera( update );
		} else {
			this.googleMap.moveCamera( update );
		}
	}

	@Override
	public void onMapClick( LatLng latLng ) {
		this.receiver.onMapClick( this.fromLocation( latLng ) );
	}

	@Override
	public void onMarkerDrag( Marker marker ) {
		this.receiver.onMarkerDrag( this.fromLocation( marker.getPosition() ) );
	}

	@Override
	public void onMarkerDragEnd( Marker marker ) {
		this.receiver.onMarkerDragEnd( this.markers.indexOf( marker ), this.fromLocation( marker.getPosition() ) );
	}

	@Override
	public void onMarkerDragStart( Marker marker ) {
		this.receiver.onMarkerDragStart( this.fromLocation( marker.getPosition() ) );
	}

	@Override
	public boolean onMarkerClick( final Marker marker ) {
		return this.receiver.onMarkerClick( this.fromLocation( marker.getPosition() ) );
	}
}
