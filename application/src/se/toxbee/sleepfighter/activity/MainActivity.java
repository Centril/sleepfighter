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
import se.toxbee.sleepfighter.android.utils.DialogUtils;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {
	private AlarmList alarmList;
	private AlarmAdapter alarmAdapter;

	private TextView earliestTimeText;
	private TextView challengePointText;

	public SFApplication app() {
		return SFApplication.get();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate( savedInstanceState );

		AlarmActivity.startIfRinging( this );

		// This is the main entry point to application GUI, so register planner.
		AlarmPlannerService.register();

		this.setContentView( R.layout.activity_main );

		this.alarmList = this.app().getAlarms();
		this.alarmList.order( app().getPrefs().display.getSortMode() );

		this.alarmAdapter = new AlarmAdapter( this, this.alarmList );

		this.app().getBus().subscribe( this );

		this.setupListView();
	}

	@Override
	protected void onResume() {
		super.onResume();

		AlarmActivity.startIfRinging( this );

		// Find all constant views.
		this.challengePointText = (TextView) findViewById( R.id.mainChallengePoints );
		this.earliestTimeText = (TextView) findViewById( R.id.earliestTimeText );

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
		ListView listView = (ListView) findViewById(R.id.mainAlarmsList);
		listView.setAdapter(this.alarmAdapter);
		listView.setOnItemClickListener(listClickListener);

		// Register to get context menu events associated with listView
		this.registerForContextMenu(listView);
	}

	private void setupChallengeToggle() {
		final ChallengeGlobalPreferences cp = app().getPrefs().challenge;

		ImageView toggleImage = (ImageView) findViewById(R.id.challenge_toggle);
		ImageView pointImage = (ImageView) findViewById(R.id.challenge_points_icon);
		
		if ( cp.isActivated() ) {
			toggleImage.setImageResource(R.drawable.ic_action_challenge_toggled);
			pointImage.setImageResource(R.drawable.ic_sun_enabled);
		}
		else {
			toggleImage.setImageResource(R.drawable.ic_action_challenge_untoggled);
			pointImage.setImageResource(R.drawable.ic_sun_disabled);
			findViewById(R.id.mainChallengePoints).setEnabled(false);
		}
		
		toggleImage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				boolean wasActive = cp.isActivated();
				cp.setActivated( !wasActive );

				if ( wasActive ) {
					((ImageView) v).setImageResource(R.drawable.ic_action_challenge_untoggled);
					findViewById(R.id.mainChallengePoints).setEnabled(false);
					((ImageView) findViewById(R.id.challenge_points_icon)).setImageResource(R.drawable.ic_sun_disabled);
				} else {
					((ImageView) v).setImageResource(R.drawable.ic_action_challenge_toggled);
					findViewById(R.id.mainChallengePoints).setEnabled(true);
					((ImageView) findViewById(R.id.challenge_points_icon)).setImageResource(R.drawable.ic_sun_enabled);
				}
			}
			
		});
	}

	private OnItemClickListener listClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			startAlarmEdit( getAlarm( position ), false);
		}
	};

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		if (v.getId() == R.id.mainAlarmsList) {
			String[] menuItems = getResources().getStringArray( R.array.main_list_context_menu );
			for (int i = 0; i < menuItems.length; i++) {
				menu.add(0, i, i, menuItems[i]);
			}

			if (SFApplication.DEBUG) {
				// adds an extra context menu item for starting an alarm
				menu.add(0, menuItems.length, menuItems.length,
						"DEBUG: Start alarm");
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

	private void updateChallengePoints() {
		this.challengePointText.setText( this.app().getPrefs().challenge.getChallengePoints() + " " );
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

		// Config title-bar.
		View titleBar = this.getLayoutInflater().inflate( R.layout.dialog_titlebar_toggle, null );
		((TextView) titleBar.findViewById( R.id.title )).setText( R.string.sort_modes_dialog_title );
		((TextView) titleBar.findViewById( R.id.toggle_label )).setText( R.string.sort_modes_dialog_toggle_label );

		// Config toggle.
		final CheckBox toggler = (CheckBox) titleBar.findViewById( R.id.toggle );
		toggler.setChecked( !mode.direction() );
		titleBar.findViewById( R.id.toggle_bg ).setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick( View v ) {
				toggler.toggle();
			}
		} );

		// Build dialog.
		new AlertDialog.Builder( this )
			.setCustomTitle( titleBar )
			.setSingleChoiceItems( R.array.sort_modes_dialog_strings, currField, new OnClickListener() {
				@Override
				public void onClick( DialogInterface dialog, int which ) {
					dialog.dismiss();

					sortModeSelected( SortMode.Field.valueOf( fields[which] ), !toggler.isChecked() );
				}
			} ).show();
	}

	private void sortModeSelected( SortMode.Field field, boolean direction ) {
		SortMode mode = new SortMode( field, direction );

		this.app().getPrefs().display.setSortMode( mode );
		this.alarmList.order( mode );

		this.alarmAdapter.notifyDataSetChanged();
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
