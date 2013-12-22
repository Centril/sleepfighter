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

import net.engio.mbassy.listener.Handler;

import org.joda.time.DateTime;

import se.toxbee.sleepfighter.R;
import se.toxbee.sleepfighter.adapter.GPSFilterAreaAdapter;
import se.toxbee.sleepfighter.app.SFApplication;
import se.toxbee.sleepfighter.model.AlarmTimestamp;
import se.toxbee.sleepfighter.model.gps.GPSFilterArea;
import se.toxbee.sleepfighter.model.gps.GPSFilterArea.Field;
import se.toxbee.sleepfighter.model.gps.GPSFilterAreaSet;
import se.toxbee.sleepfighter.model.gps.GPSFilterMode;
import se.toxbee.sleepfighter.preference.LocationFilterPreferences;
import se.toxbee.sleepfighter.receiver.GPSFilterRefreshReceiver;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.TextView;

/**
 * ManageEditAreasActivity is the activity for managing the<br/>
 * list of available {@link GPSFilterArea}s.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Oct 5, 2013
 */
public class ManageGPSFilterAreasActivity extends Activity {
	private static final String TAG = ManageGPSFilterAreasActivity.class.getSimpleName();

	private static final long SPLASH_FADE_DELAY = 150;

	private GPSFilterAreaAdapter setAdapter;
	private GPSFilterAreaSet set;

	private Animation splashFadeOut;
	private ViewGroup splashInfoContainer;

	private LocationFilterPreferences prefs() {
		return SFApplication.get().getPrefs().locFilter;
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if ( Build.VERSION.SDK_INT >= 11 ) {
			// add the custom view to the action bar.
			ActionBar actionBar = getActionBar();
			actionBar.setCustomView( R.layout.gpsfilter_manage_actionbar );
			actionBar.setDisplayOptions( ActionBar.DISPLAY_SHOW_HOME
					| ActionBar.DISPLAY_HOME_AS_UP
					| ActionBar.DISPLAY_SHOW_CUSTOM );

			View customView = actionBar.getCustomView();

			// Setup enabled switch.
			CompoundButton activatedSwitch = (CompoundButton) customView.findViewById( R.id.manage_gpsfilter_toggle );
			activatedSwitch.setChecked( prefs().isEnabled() );
			activatedSwitch.setOnCheckedChangeListener( new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged( CompoundButton buttonView, boolean isChecked ) {
					prefs().setEnabled( isChecked );
				}
			} );
		}
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void updateActionBarEnabled() {
		if ( Build.VERSION.SDK_INT >= 11 ) {
			// add the custom view to the action bar.
			ActionBar actionBar = getActionBar();
			View customView = actionBar.getCustomView();

			// Setup enabled switch.
			CompoundButton activatedSwitch = (CompoundButton) customView.findViewById( R.id.manage_gpsfilter_toggle );
			activatedSwitch.setChecked( prefs().isEnabled() );
		}
	}

	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );

		this.setContentView( R.layout.activity_manage_gpsfilter_areas );

		this.setupActionBar();

		this.set = SFApplication.get().getGPSSet();
		this.set.getMessageBus().subscribe( this );

		this.setAdapter = new GPSFilterAreaAdapter( this, this.set );

		this.setupSetView();
		
		
	}

	@Override
	protected void onResume() {
		super.onResume();

		this.updateActionBarEnabled();

		this.setupSplash();
	}

	/**
	 * Launches the splash information (help) layout, or hides it.
	 */
	private void setupSplash() {
		this.splashInfoContainer = (ViewGroup) this.findViewById( R.id.manage_gpsfilter_what_splash );

		// Fix text layout.
		TextView textView = (TextView) this.findViewById( R.id.manage_gpsfilter_what_splash_text );
		textView.setText( Html.fromHtml( this.getString( R.string.manage_gpsfilter_what_splash_text ) ) );
		
		textView.setMovementMethod(new ScrollingMovementMethod());

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

		final Button splashCloseButton = (Button)this.findViewById( R.id.manage_gpsfilter_what_splash_close );
		
		splashCloseButton.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick( View v ) {
				hideSplash();
			}
		});
		
		// In order to make the splash close button easier to click, we surround it with a container with invincible padding.
		// if the container is clicked, then also the button is clicked.
		View container = this.findViewById(R.id.manage_gpsfilter_what_splash_close_container);
		container.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				splashCloseButton.performClick();
			}
		});	

		// Hide if we got areas, user shouldn't need help.
		if ( !this.set.isEmpty() ) {
			this.splashInfoContainer.setVisibility( View.GONE );
		}
	}

	/**
	 * Launches the splash information (help) layout.
	 */
	private void launchSplash() {
		this.splashInfoContainer.setVisibility( View.VISIBLE );
	}

	/**
	 * Hides the help splash.
	 */
	private void hideSplash() {
		if ( this.splashInfoContainer.getVisibility() != View.VISIBLE ) {
			return;
		}

		Log.d( TAG, "hiding splash" );

		this.splashInfoContainer.startAnimation( this.splashFadeOut );
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		// Release the applications reference to set.
		this.set = null;
		SFApplication.get().releaseGPSSet();
	}

	/**
	 * Sets up the view of the set (list) of areas.
	 */
	private void setupSetView() {
		ListView listView = (ListView) findViewById( R.id.manage_gpsfilter_areas_list );
		listView.setAdapter( this.setAdapter );
		listView.setOnItemClickListener( listClickListener );

		// Register to get context menu events associated with listView
		this.registerForContextMenu( listView );
	}

	/**
	 * Handles a change in area by refreshing list.
	 * 
	 * @param event the event
	 */
	@Handler
	public void handleChange( GPSFilterArea.ChangeEvent evt ) {
		this.scheduleFix();

		// We're not interested in POLYGON change.
		if ( evt.getModifiedField() == Field.POLYGON ) {
			return;
		}

		// Refresh the list items
		this.setAdapter.notifyDataSetChanged();
	}

	/**
	 * Handles a change in the list of areas (the list itself, deletion, insertion, etc, not edits in an area).
	 *
	 * @param evt the event.
	 */
	@Handler
	public void handleListChange( GPSFilterAreaSet.Event evt ) {
		this.setAdapter.notifyDataSetChanged();

		this.scheduleFix();
	}

	/**
	 * Schedules a location fix if needed.
	 */
	private void scheduleFix() {
		// Schedule fix if needed.
		AlarmTimestamp at = SFApplication.get().getAlarms().getEarliestAlarm( new DateTime().getMillis() );
		if ( at == AlarmTimestamp.INVALID ) {
			return;
		}

		GPSFilterRefreshReceiver.scheduleFix( this, at.getMillis() );
	}

	/**
	 * Adds an area and sends user to edit activity.
	 */
	private void addArea() {
		GPSFilterArea area = new GPSFilterArea( null, true, GPSFilterMode.INCLUDE );

		this.set.add( area );
		this.editArea( area, false );
	}

	/**
	 * Sends user to edit activity.
	 *
	 * @param area the area to edit.
	 * @param isNew true if area is new, otherwise false.
	 */
	protected void editArea( GPSFilterArea area, boolean isNew ) {
		Intent i = new Intent( this, EditGPSFilterAreaActivity.class );
		i.putExtra( EditGPSFilterAreaActivity.EXTRAS_AREA_ID, area.getId() );
		i.putExtra( EditGPSFilterAreaActivity.EXTRAS_AREA_IS_NEW, isNew );
		this.startActivity( i );
	}

	/**
	 * Deletes the given area from set.
	 *
	 * @param selectedArea the area to delete.
	 */
	private void deleteArea( GPSFilterArea selectedArea ) {
		this.set.remove( selectedArea );
	}

	/**
	 * Clears all areas.
	 */
	private void clearAreas() {
		this.set.clear();
	}

	/**
	 * Moves the user to global options > location filter.
	 */
	private void gotoSettings() {
		Intent i = new Intent( this, GlobalSettingsActivity.class );
		this.startActivity( i );
	}

	private OnItemClickListener listClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			GPSFilterArea area = ManageGPSFilterAreasActivity.this.setAdapter.getItem( position );
			editArea( area, false );
		}
	};

	@Override
	public void onCreateContextMenu( ContextMenu menu, View v, ContextMenuInfo menuInfo ) {
		if ( v.getId() == R.id.manage_gpsfilter_areas_list ) {
			String[] menuItems = getResources().getStringArray( R.array.manage_gpsfilter_areas_context );
			for ( int i = 0; i < menuItems.length; ++i ) {
				menu.add( 0, i, i, menuItems[i] );
			}
		}
	}

	@Override
	public boolean onContextItemSelected( MenuItem item ) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
		GPSFilterArea selectedArea = (this.setAdapter.getItem( info.position ));

		switch (item.getOrder()) {
			case 0:
				this.editArea( selectedArea, false );
				return true;

			case 1:
				this.deleteArea( selectedArea );
				return true;

			default:
				return false;

		}
	}

	@Override
	public boolean onOptionsItemSelected( MenuItem item ) {
		switch ( item.getItemId() ) {
		case R.id.action_manage_gpsfilter_areas_add:
			this.addArea();
			return true;

		case R.id.action_manage_gpsfilter_help:
			this.launchSplash();
			return true;

		case R.id.action_manage_gpsfilter_areas_clear:
			this.clearAreas();
			return true;

		case R.id.action_gpsfilter_settings:
			this.gotoSettings();
			return true;
			
		case android.R.id.home:
			finish();
			return true;
		default:
			return super.onOptionsItemSelected( item );
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.manage_gpsfilter_areas, menu);
		return true;
	}
}