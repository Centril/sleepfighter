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

import java.util.Arrays;

import net.engio.mbassy.listener.Handler;
import se.toxbee.sleepfighter.R;
import se.toxbee.sleepfighter.adapter.AlarmAdapter;
import se.toxbee.sleepfighter.android.component.dialog.TogglableTitleBar;
import se.toxbee.sleepfighter.android.utils.DialogUtils;
import se.toxbee.sleepfighter.android.utils.Toaster;
import se.toxbee.sleepfighter.android.utils.ViewGroupUtils;
import se.toxbee.sleepfighter.app.SFApplication;
import se.toxbee.sleepfighter.helper.AlarmIntentHelper;
import se.toxbee.sleepfighter.helper.AlarmTimeRefresher;
import se.toxbee.sleepfighter.helper.AlarmTimeRefresher.RefreshedEvent;
import se.toxbee.sleepfighter.model.Alarm;
import se.toxbee.sleepfighter.model.Alarm.AlarmEvent;
import se.toxbee.sleepfighter.model.Alarm.Field;
import se.toxbee.sleepfighter.model.Alarm.ScheduleChangeEvent;
import se.toxbee.sleepfighter.model.AlarmList;
import se.toxbee.sleepfighter.model.AlarmTimestamp;
import se.toxbee.sleepfighter.model.SortMode;
import se.toxbee.sleepfighter.preference.ChallengeGlobalPreferences;
import se.toxbee.sleepfighter.preference.DisplayPreferences;
import se.toxbee.sleepfighter.receiver.AlarmReceiver;
import se.toxbee.sleepfighter.service.AlarmPlannerService;
import se.toxbee.sleepfighter.text.DateTextUtils;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mobeta.android.dslv.DragSortListView;
import com.mobeta.android.dslv.DragSortListView.DropListener;

public class MainActivity extends Activity {
	private static final String EXTRAS_IS_REORDER_MODE = "IS_REORDER_MODE";

	private AlarmList alarmList;
	private AlarmAdapter alarmAdapter;

	private ListView listView;
	private ListView listViewShadow;

	private TextView earliestTimeText;

	private ImageView challengeToggler;
	private TextView challengePointsText;
	private ImageView challengePointsIcon;

	public SFApplication app() {
		return SFApplication.get();
	}

	@Override
	protected void onCreate( Bundle state ) {
		super.onCreate( state );

		AlarmActivity.startIfRinging( this );

		// This is the main entry point to application GUI, so register planner.
		AlarmPlannerService.register();

		this.setContentView( R.layout.activity_main );

		this.alarmList = this.app().getAlarms();
		this.alarmList.order( this.dprefs().getSortMode() );

		this.alarmAdapter = new AlarmAdapter( this, this.alarmList );

		this.app().getBus().subscribe( this );

		this.setupListView();

		this.tryEnterReorderMode( state );
	}

	@Override
	protected void onSaveInstanceState( Bundle outState ) {
		super.onSaveInstanceState( outState );
		this.saveReorderMode( outState );
	}


	@Override
	protected void onResume() {
		super.onResume();

		AlarmActivity.startIfRinging( this );

		// Find all constant views.
		this.earliestTimeText = (TextView) findViewById( R.id.earliestTimeText );
		this.challengeToggler = (ImageView) findViewById( R.id.challenge_toggle );
		this.challengePointsText = (TextView) findViewById( R.id.mainChallengePoints );
		this.challengePointsIcon = (ImageView) findViewById( R.id.challenge_points_icon );

		this.updateEarliestText();

		this.setupChallengeToggle();
		this.updateChallengePoints();

		this.initRefresher();
	}

	@Override
	protected void onPause() {
		super.onPause();

		this.clearRefresher();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		this.clearRefresher();
	}

	private AlarmTimeRefresher refresher;
	private void initRefresher() {
		this.refresher = new AlarmTimeRefresher( this.alarmList );
		this.refresher.start();
	}
	private void clearRefresher() {
		if ( this.refresher != null ) {
			this.refresher.stop();
			this.refresher = null;
		}
	}

	private void setupListView() {
		this.listView = (ListView) findViewById( R.id.mainAlarmsList );
		this.listView.setAdapter( this.alarmAdapter );
		this.listView.setOnItemClickListener( this.listClickListener );

		// Register to get context menu events associated with listView
		this.registerForContextMenu( this.listView );
	}

	private void replaceListView() {
		// Stop current.
		this.listView.setAdapter( null );
		this.listView.setOnItemClickListener( null );
		this.unregisterForContextMenu( this.listView );

		// Remember old.
		this.listViewShadow = this.listView;

		// Start new.
		this.setupListView();
	}

	private OnItemClickListener listClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			startAlarmEdit( getAlarm( position ), false);
		}
	};

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		if (v.getId() == R.id.mainAlarmsList) {
			String[] menuItems = getResources().getStringArray( R.array.main_list_context_menu );
			for ( int i = 0; i < menuItems.length; i++ ) {
				menu.add( 0, i, i, menuItems[i] );
			}

			if ( SFApplication.DEBUG ) {
				// adds an extra context menu item for starting an alarm
				menu.add( 0, menuItems.length, menuItems.length, "DEBUG: Start alarm" );
			}
		}
	}

	private void startAlarm( Alarm alarm ) {
		this.sendBroadcast( new AlarmIntentHelper( new Intent( this,
				AlarmReceiver.class ) ).setAlarm( alarm ).intent() );
	}

	@Override
	public boolean onContextItemSelected( MenuItem item ) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
		Alarm selectedAlarm = this.getAlarm( info.position );

		switch ( item.getOrder() ) {
		case 0:
			this.startAlarmEdit( selectedAlarm, false );
			break;

		case 1:
			this.deleteAlarm( selectedAlarm );
			break;

		case 2:
			this.copyAlarm( selectedAlarm );
			break;

		case 3:
			this.alarmAdapter.pickNormalTime( selectedAlarm );
			break;

		case 4:
			this.alarmAdapter.pickCountdownTime( selectedAlarm );
			break;

		case 5:
			this.startAlarm( selectedAlarm );
			break;

		default:
			return false;
		}

		return true;
	}

	private void startAlarmEdit( Alarm alarm, boolean isNew ) {
		Intent i = new Intent( this, AlarmSettingsActivity.class );
		i.putExtra( AlarmSettingsActivity.EXTRA_ALARM_IS_NEW, isNew );
		this.startActivity( new AlarmIntentHelper( i ).setAlarmId( alarm ).intent() );
	}

	private void startGlobalSettings() {
		Intent i = new Intent( this, GlobalSettingsActivity.class );
		this.startActivity( i );
	}

	private void deleteAlarm(final Alarm alarm) {
		String message = getString( R.string.confirm_delete );
		DialogUtils.showConfirmationDialog( message, this,
				new OnClickListener() {
					@Override
					public void onClick( DialogInterface dialog, int which ) {
						alarmList.remove( alarm );
					}
				} );
	}

	private void copyAlarm( Alarm alarm ) {
		this.alarmList.add( new Alarm( alarm ) );
	}

	private void newAlarm() {
		this.alarmList.add( app().getFromPresetFactory().createAlarm() );
	}

	private Alarm getAlarm( int position ) {
		return this.alarmAdapter.getItem( position );
	}

	/**
	 * Handles a change to an alarm.
	 *
	 * @param evt
	 */
	@Handler
	public void handleAlarmChange( AlarmEvent evt ) {
		boolean changed = this.alarmList.orderIfNeeded( evt );

		if ( evt instanceof ScheduleChangeEvent ) {
			this.updateEarliestText();
			changed = true;
		} else if ( evt.getModifiedField() == Field.NAME ) {
			changed = true;
		}

		if ( changed ) {
			this.alarmAdapter.notifyDataSetChanged();
		}
	}

	/**
	 * Handles a change in the list of alarms<br/>
	 * (the list itself, deletion, insertion, etc, not edits in an alarm).
	 * 
	 * @param evt the event.
	 */
	@Handler
	public void handleListChange( AlarmList.Event evt ) {
		this.updateEarliestUI();
	}

	/**
	 * Handles a refresh event.
	 *
	 * @param evt the event.
	 */
	@Handler
	public void handleRefreshed( RefreshedEvent evt ) {
		this.updateEarliestUI();
	}

	/**
	 * Performs {@link #updateEarliest()} on UI thread.
	 */
	private void updateEarliestUI() {
		this.runOnUiThread( new Runnable() {
			@Override
			public void run() {
				updateEarliest();
			}
		} );
	}

	/**
	 * Called when earliest alarm has changed.
	 */
	private void updateEarliest() {
		this.updateEarliestText();
		this.alarmAdapter.notifyDataSetChanged();
	}

	/**
	 * Updates the earliest time text.
	 */
	private void updateEarliestText() {
		long now = app().now();
		AlarmTimestamp stamp = this.alarmList.getEarliestAlarm( now );
		earliestTimeText.setText( DateTextUtils.printTime( now, stamp ) );
	}

	private void setupChallengeToggle() {
		final ChallengeGlobalPreferences cp = app().getPrefs().challenge;
		this.updateChallengeToggler( cp.isActivated() );
		this.challengeToggler.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				boolean nowActive = !cp.isActivated();
				cp.setActivated( nowActive );
				updateChallengeToggler( nowActive );
			}
		});
	}

	private void updateChallengeToggler( boolean active ) {
		this.challengeToggler.setImageResource( active ? R.drawable.ic_action_challenge_toggled : R.drawable.ic_action_challenge_untoggled );
		this.challengePointsIcon.setImageResource( active ? R.drawable.ic_sun_enabled : R.drawable.ic_sun_disabled );
		this.challengePointsText.setEnabled( active );
	}

	private void updateChallengePoints() {
		this.challengePointsText.setText( this.app().getPrefs().challenge.getChallengePoints() + " " );
	}

	/**
	 * Sends the user to the activity for editing GPSFilterArea:s.
	 */
	private void startGPSFilterAreaEdit() {
		Intent i = new Intent( this, ManageGPSFilterAreasActivity.class );
		this.startActivity( i );
	}

	private void sortModeEdit() {
		SortMode mode = this.alarmList.getSortMode();

		final String[] fields = getResources().getStringArray( R.array.sort_modes_dialog_fields );

		// Deduce current field "which".
		String name = mode.field().name();
		int currField = Arrays.asList( fields ).indexOf( name );

		final TogglableTitleBar titleBar = new TogglableTitleBar( this )
			.setAll( R.string.sort_modes_dialog_title, R.string.sort_modes_dialog_toggle_label, !mode.direction() );

		// Build dialog.
		new AlertDialog.Builder( this )
			.setCustomTitle( titleBar )
			.setSingleChoiceItems( R.array.sort_modes_dialog_strings, currField, new OnClickListener() {
				@Override
				public void onClick( DialogInterface dialog, int which ) {
					dialog.dismiss();

					sortModeSelected( new SortMode( SortMode.Field.valueOf( fields[which] ), !titleBar.isChecked() ) );
				}
			} ).show();
	}

	private void sortModeSelected( SortMode mode ) {
		boolean altered = this.alarmList.order( mode );

		if ( mode.field() == SortMode.Field.MANUAL ) {
			this.startReorderMode();
		}

		if ( altered ) {
			this.dprefs().setSortMode( mode );
			this.alarmAdapter.notifyDataSetChanged();
		}
	}

	private void startReorderMode() {
		Toaster.out( this, R.string.sort_modes_manual_toast );
		this.enterReorderMode();
	}

	private void enterReorderMode() {
		// Inflate & replace view.
		ViewGroup listContainer = (ViewGroup) this.findViewById( R.id.mainAlarmsListContainer );
		final View reorderView = this.getLayoutInflater().inflate( R.layout.reorder_mode_view, listContainer, false );
		ViewGroupUtils.replaceView( this.listView, reorderView );
		this.replaceListView();

		// Bind done button.
		Button button = (Button) reorderView.findViewById( R.id.reorder_mode_done_button );
		button.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick( View v ) {
				ViewGroupUtils.replaceView( reorderView, listViewShadow );
				replaceListView();
				listViewShadow = null;
			}
		} );

		// Bind DSLV stuff.
		DragSortListView dslv = (DragSortListView) this.listView;
		dslv.setDropListener( new DropListener() {
			@Override
			public void drop( int from, int to ) {
				getAlarm( from ).swapOrder( getAlarm( to ) );
			}
		} );
	}

	private void tryEnterReorderMode( Bundle state ) {
		if ( state != null && state.getBoolean( EXTRAS_IS_REORDER_MODE ) ) {
			this.enterReorderMode();
		}
	}

	private void saveReorderMode( Bundle out ) {
		out.putBoolean( EXTRAS_IS_REORDER_MODE, this.listView instanceof DragSortListView );
	}

	private DisplayPreferences dprefs() {
		return this.app().getPrefs().display;
	}

	@Override
	public boolean onCreateOptionsMenu( Menu menu ) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate( R.menu.main, menu );
		return true;
	}

	@Override
	public boolean onOptionsItemSelected( MenuItem item ) {
		// Handle item selection
		switch ( item.getItemId() ) {
		case R.id.action_add:
			this.newAlarm();
			return true;

		case R.id.action_settings:
			this.startGlobalSettings();
			return true;

		case R.id.action_gpsfilter_area_edit:
			this.startGPSFilterAreaEdit();
			return true;

		case R.id.action_sort_mode:
			this.sortModeEdit();
			return true;

		default:
			return super.onOptionsItemSelected( item );
		}
	}
}
