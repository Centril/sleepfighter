package se.chalmers.dat255.sleepfighter.activities;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

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
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {
	private AlarmsManager manager;

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

		AlarmAdapter alarmAdapter = new AlarmAdapter(this, alarms);
		listView.setAdapter(alarmAdapter);

		this.updateEarliestText( now );
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
