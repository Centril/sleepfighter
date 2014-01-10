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
package se.toxbee.sleepfighter.activity;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Criteria;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.google.common.collect.Lists;

import net.engio.mbassy.listener.Handler;

import java.util.List;

import se.toxbee.sleepfighter.R;
import se.toxbee.sleepfighter.android.location.LocationAdapter;
import se.toxbee.sleepfighter.android.utils.DialogUtils;
import se.toxbee.sleepfighter.android.utils.Toaster;
import se.toxbee.sleepfighter.app.SFApplication;
import se.toxbee.sleepfighter.gps.GPSFilterLocationRetriever;
import se.toxbee.sleepfighter.gps.LocationGUIHandler;
import se.toxbee.sleepfighter.gps.LocationGUIHandler.LocationGUIClient;
import se.toxbee.sleepfighter.gps.LocationGUIProvider;
import se.toxbee.sleepfighter.gps.LocationGUIProvider.LocationGUIReceiver;
import se.toxbee.sleepfighter.gps.google.GoogleLocationProvider;
import se.toxbee.sleepfighter.model.gps.GPSFilterArea;
import se.toxbee.sleepfighter.model.gps.GPSFilterAreaSet;
import se.toxbee.sleepfighter.model.gps.GPSFilterMode;
import se.toxbee.sleepfighter.model.gps.GPSFilterPolygon;
import se.toxbee.sleepfighter.model.gps.GPSLatLng;

/**
 * EditGPSFilterAreaActivity is the activity for editing an GPSFilterArea.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Oct 6, 2013
 */
public class EditGPSFilterAreaActivity extends FragmentActivity implements LocationGUIClient {
	private static final String TAG = EditGPSFilterAreaActivity.class.getSimpleName();

	public static final String EXTRAS_AREA_ID = "gpsfilter_area_id";
	public static final String EXTRAS_AREA_IS_NEW = "gpsfilter_are_isnew";

	private static final long SPLASH_FADE_DELAY = 150;

	private LinearLayout splashInfoContainer;

	private Animation splashFadeOut;

	private GPSFilterArea area;

	private GPSFilterAreaSet set;

	private boolean isNew;

	private LocationGUIHandler mapHandler;

	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );

		this.setContentView( R.layout.activity_edit_gpsfilter_area );

		this.fetchArea();

		this.setupMap();

		this.setupActionBar();

		this.setupSplash();

		this.setupModeSpinner();

		this.setupBottomUndo();

		this.area.getMessageBus().subscribe( this );
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		String name = this.area.printName();

		if ( Build.VERSION.SDK_INT >= 11 ) {
			// add the custom view to the action bar.
			ActionBar actionBar = getActionBar();
			actionBar.setCustomView( R.layout.gpsfilter_area_actionbar );
			actionBar.setDisplayOptions( ActionBar.DISPLAY_SHOW_HOME
					| ActionBar.DISPLAY_HOME_AS_UP
					| ActionBar.DISPLAY_SHOW_CUSTOM );

			View customView = actionBar.getCustomView();

			// Setup edit name component.
			EditText editNameView = (EditText) customView.findViewById( R.id.edit_gpsfilter_area_title_edit );
			editNameView.setText( name );
			editNameView.setOnEditorActionListener( new OnEditorActionListener() {
				@Override
				public boolean onEditorAction( TextView v, int actionId, KeyEvent event ) {
					area.setName( v.getText().toString() );
					InputMethodManager imm = (InputMethodManager) getSystemService( Context.INPUT_METHOD_SERVICE );
					imm.hideSoftInputFromWindow( v.getWindowToken(), 0 );
					return false;
				}
			} );
			editNameView.clearFocus();

			// Setup enabled switch.
			CompoundButton activatedSwitch = (CompoundButton) customView.findViewById( R.id.edit_gpsfilter_area_toggle );
			activatedSwitch.setChecked( this.area.isEnabled() );
			activatedSwitch.setOnCheckedChangeListener( new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged( CompoundButton buttonView, boolean isChecked ) {
					area.setEnabled( isChecked );
				}
			} );
		} else {
			this.setTitle( name );
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.edit_gpsfilter_area, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected( MenuItem item ) {
		switch ( item.getItemId() ) {
		case android.R.id.home:
			finish();
			return true;
			
		case R.id.action_edit_gpsfilter_area_help:
			this.showSplash();
			return true;

		case R.id.action_edit_gpsfilter_area_rename:
			this.showRenameDialog();
			return true;

		case R.id.action_edit_gpsfilter_area_clear:
			this.mapHandler.clearPoints();
			return true;

		case R.id.action_edit_gpsfilter_area_undo:
			this.mapHandler.undoLastPoint();
			return true;

		case R.id.action_edit_gpsfilter_area_remove:
			this.removeArea();
			return true;

		case R.id.action_edit_gpsfilter_area_zoom:
			this.mapHandler.zoomToArea();
			return true;

		case R.id.action_edit_gpsfilter_area_manual:
			this.addManualDialog();
			return true;

		case R.id.action_gpsfilter_settings:
			this.gotoSettings();
			return true;

		default:
			return super.onOptionsItemSelected( item );
		}
	}

	/**
	 * Moves the user to global options > location filter.
	 */
	private void gotoSettings() {
		Intent i = new Intent( this, GlobalSettingsActivity.class );
		this.startActivity( i );
	}

	/**
	 * Handles a change in area via event.
	 *
	 * @param evt the change event.
	 */
	@TargetApi( Build.VERSION_CODES.HONEYCOMB )
	@Handler
	public void handleAreaChange( GPSFilterArea.ChangeEvent evt ) {
		switch ( evt.getModifiedField() ) {
		case ENABLED:
			if ( Build.VERSION.SDK_INT >= 11 ) {
				CompoundButton activatedSwitch = (CompoundButton) this.getActionBar().getCustomView().findViewById( R.id.edit_gpsfilter_area_toggle );
				activatedSwitch.setChecked( evt.getArea().isEnabled() );
			}
			break;

		case NAME:
			String name =  this.area.printName();
			if ( Build.VERSION.SDK_INT >= 11 ) {
				((EditText) this.getActionBar().getCustomView().findViewById( R.id.edit_gpsfilter_area_title_edit )).setText( name );
			} else {
				this.setTitle( name );
			}
			break;

		case MODE:
		case POLYGON:
		default:
			break;
		}
	}

	/**
	 * Sets up the "action-bar" at the bottom undo button.
	 */
	private void setupBottomUndo() {
		this.findViewById( R.id.action_edit_gpsfilter_area_undo_button ).setOnClickListener( new OnClickListener() {
			@Override
			public void onClick( View v ) {
				mapHandler.undoLastPoint();
			}
		} );
	}

	/**
	 * Fetches the area from set.
	 */
	private void fetchArea() {
		// Fetch the isNew.
		Intent i = this.getIntent();
		this.isNew = i.getBooleanExtra( EXTRAS_AREA_IS_NEW, true );

		// Fetch ID.
		int id = i.getIntExtra( EXTRAS_AREA_ID, -1 );
		if ( id == -1 ) {
			Toast.makeText( this, "No area ID provided, finishing!", Toast.LENGTH_LONG ).show();
			this.finish();
		}

		// Get the set from application.
		this.set = SFApplication.get().getGPSSet();

		// For some weird reason, NullPointerException will happen if we don't do this.
		this.set.setMessageBus( SFApplication.get().getBus() );

		// Find area in set.
		this.area = this.set.getById( id );

		Log.d( TAG, "Fetched area " + this.area );

		if ( this.area == null ) {
			Toaster.out( this, "The area ID provided did not exist in set." );
			this.finish();
		}
	}

	/**
	 * Removes the current area, finishes activity.
	 */
	private void removeArea() {
		this.set.remove( this.area );
		this.finish();
	}

	/**
	 * Sets up the spinner for modes.
	 */
	private void setupModeSpinner() {
		Spinner spinner = (Spinner) this.findViewById( R.id.action_edit_gpsfilter_area_mode );
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource( this, R.array.action_edit_gpsfilter_area_mode, android.R.layout.simple_spinner_item );
		adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
		spinner.setAdapter( adapter );

		spinner.setSelection( this.area.getMode() == GPSFilterMode.INCLUDE ? 0 : 1 );

		spinner.setOnItemSelectedListener( new OnItemSelectedListener() {
			@Override
			public void onItemSelected( AdapterView<?> parent, View view, int position, long id ) {
				updateMode( position );
			}

			@Override
			public void onNothingSelected( AdapterView<?> parent ) {
			}
		} );
	}

	/**
	 * Updates the mode from slider position.
	 *
	 * @param position the position in array, 0 == Include, 1 == exclude.
	 */
	protected void updateMode( int position ) {
		GPSFilterMode mode = position == 0 ? GPSFilterMode.INCLUDE : GPSFilterMode.EXCLUDE;
		this.area.setMode( mode );
		this.mapHandler.updatePolygon();
	}

	/**
	 * Shows a rename dialog.
	 */
	private void showRenameDialog() {
		AlertDialog.Builder alert = new AlertDialog.Builder( this );

		alert.setTitle( this.getString( R.string.action_edit_gpsfilter_area_rename ) );

		final EditText input = new EditText( this );
		input.setText( this.area.getName() );
		alert.setView( input );

		alert.setPositiveButton( android.R.string.ok, new DialogInterface.OnClickListener() {
			public void onClick( DialogInterface dialog, int whichButton ) {
				area.setName( input.getText().toString() );
			}
		} );
		alert.setNegativeButton( android.R.string.cancel, DialogUtils.getNoopClickListener() );

		alert.show();
	}

	/**
	 * Sets up the help splash.
	 */
	private void setupSplash() {
		this.splashInfoContainer = (LinearLayout) this.findViewById( R.id.edit_gpsfilter_area_splash );

		TextView textView = (TextView) this.splashInfoContainer.findViewById( R.id.edit_gpsfilter_area_splash_text );
		textView.setText( Html.fromHtml( this.getString( R.string.edit_gpsfilter_area_splash_text ) ) );

		textView.setMovementMethod( new ScrollingMovementMethod() );
		
		// Define fade out animation.
		this.splashFadeOut = new AlphaAnimation( 1.00f, 0.00f );
		this.splashFadeOut.setDuration( SPLASH_FADE_DELAY );
		this.splashFadeOut.setAnimationListener( new AnimationListener() {
			public void onAnimationStart( Animation animation ) {
			}

			public void onAnimationRepeat( Animation animation ) {
			}

			public void onAnimationEnd( Animation animation ) {
				splashInfoContainer.setVisibility( View.GONE );
			}
		} );

		// Don't show if not new and we got polygon.
		if ( !(this.isNew || this.mapHandler.isFresh() ) ) {
			this.splashInfoContainer.setVisibility( View.GONE );
		}
	}

	/**
	 * Shows the help splash.
	 */
	private void showSplash() {
		this.splashInfoContainer.setVisibility( View.VISIBLE );
	}

	/**
	 * Hides the help splash.
	 */
	private void hideSplash() {
		this.splashInfoContainer.startAnimation( this.splashFadeOut );
	}

	private void setupMap() {
		ViewGroup viewContainer = (ViewGroup) this.findViewById( R.id.edit_gpsfilter_area_mapcontainer );

		this.mapHandler = new LocationGUIHandler( this );
		this.mapHandler.setupMap( viewContainer );
	}

	@Override
	public GPSFilterArea getArea() {
		return this.area;
	}

	@Override
	public FragmentActivity getActivity() {
		return this;
	}

	@Override
	public boolean onMapClick( GPSLatLng loc ) {
		if ( this.splashInfoContainer.getVisibility() == View.VISIBLE ) {
			this.hideSplash();
			return true;
		}

		return false;
	}

	private void addManualDialog() {
		ViewGroup inputs = (ViewGroup) this.getLayoutInflater().inflate( R.layout.edit_gpsfilter_area_manual, null );
		final EditText lat = (EditText) inputs.findViewById( R.id.latitude );
		final EditText lng = (EditText) inputs.findViewById( R.id.longitude );

		AlertDialog.Builder alert = new AlertDialog.Builder( this );
		alert.setTitle( R.string.action_edit_gpsfilter_area_manual );
		alert.setView( inputs );
		alert.setNegativeButton( android.R.string.cancel, DialogUtils.getNoopClickListener() );
		alert.setPositiveButton( R.string.add, new DialogInterface.OnClickListener() {
			public void onClick( DialogInterface dialog, int whichButton ) {
				mapHandler.addPoint( lat.getText(), lng.getText() );
			}
		} );

		alert.show();
	}
}