package se.chalmers.dat255.sleepfighter.activities;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import net.engio.mbassy.listener.Handler;
import se.chalmers.dat255.sleepfighter.R;
import se.chalmers.dat255.sleepfighter.debug.Debug;
import se.chalmers.dat255.sleepfighter.model.Alarm;
import se.chalmers.dat255.sleepfighter.model.Alarm.DateChangeEvent;
import se.chalmers.dat255.sleepfighter.model.AlarmsManager;
import se.chalmers.dat255.sleepfighter.model.AlarmsManager.EarliestInfo;
import se.chalmers.dat255.sleepfighter.utils.DateTextUtils;
import se.chalmers.dat255.sleepfighter.utils.message.Message;
import se.chalmers.dat255.sleepfighter.utils.message.MessageBus;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private AlarmsManager manager;
	private AlarmAdapter alarmAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Hard code in some sample alarms
		// TODO fetch from where actual alarms will be stored
		Calendar now = new GregorianCalendar();
		List<Alarm> alarms = new ArrayList<Alarm>();
		alarms.add(new Alarm(8, 30));
		alarms.add(new Alarm(7, 0));
		alarms.add(new Alarm(3, 0));
		Alarm namedAlarm = new Alarm(13, 37);
		namedAlarm.setName("Named alarm");
		alarms.add(namedAlarm);

		MessageBus<Message> bus = new MessageBus<Message>();
		bus.subscribe( this );

		this.manager = new AlarmsManager( alarms );
		this.manager.setMessageBus( bus );

		ListView listView = (ListView) findViewById(R.id.mainAlarmsList);

		this.alarmAdapter = new AlarmAdapter(this, alarms);
		listView.setAdapter(this.alarmAdapter);

		listView.setOnItemClickListener(listClickListener);

		// Register to get context menu events associated with listView
		registerForContextMenu(listView);

		this.updateEarliestText( now );
	}
	
	private OnItemClickListener listClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Alarm clickedAlarm = MainActivity.this.alarmAdapter.getItem(position);
			startAlarmEdit(clickedAlarm);
		}
	};

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		if (v.getId() == R.id.mainAlarmsList) {
			String[] menuItems = getResources().getStringArray(
					R.array.main_list_context_menu);
			for (int i = 0; i < menuItems.length; i++) {
				menu.add(0, i, i, menuItems[i]);
			}
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
				.getMenuInfo();
		Alarm selectedAlarm = (alarmAdapter.getItem(info.position));

		// TODO perhaps use something other than order
		switch (item.getOrder()) {
			case 0:
				startAlarmEdit(selectedAlarm);
				return true;
			case 1:
				deleteAlarm(selectedAlarm);
				return true;
			default:
				return false;
		}
	}

	private void startAlarmEdit(Alarm alarm) {
		Toast.makeText(this, "Not yet implemented", Toast.LENGTH_SHORT).show();
		// TODO Launch alarm edit intent
	}

	private void deleteAlarm(Alarm alarm) {
		Toast.makeText(this, "Not yet implemented", Toast.LENGTH_SHORT).show();
		// TODO Delete alarm
	}
	
	@Handler
	public void handleDateChange( DateChangeEvent evt ) {
		this.runOnUiThread( new Runnable() {
			@Override
			public void run() {
				MainActivity.this.updateEarliestText( new GregorianCalendar() );
			}
		});
	}

	/**
	 * Sets the earliest time text.
	 *
	 * @param now the current time.
	 */
	private void updateEarliestText( Calendar now ) {
		Debug.d( "updateEarliestText" );

		EarliestInfo earliestInfo = this.manager.getEarliestInfo( now );
		TextView earliestTimeText = (TextView) findViewById( R.id.earliestTimeText );
		earliestTimeText.setText( DateTextUtils.getEarliestText( getResources(), now, earliestInfo ) );
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
	    switch (item.getItemId()) {
	        case R.id.action_add:
	        	Intent intent = new Intent(this, AlarmSettingsActivity.class);
	    		startActivity(intent);
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

}
