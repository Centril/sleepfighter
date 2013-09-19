package se.chalmers.dat255.sleepfighter.activities;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import se.chalmers.dat255.sleepfighter.R;
import se.chalmers.dat255.sleepfighter.model.Alarm;
import se.chalmers.dat255.sleepfighter.model.AlarmsManager;
import se.chalmers.dat255.sleepfighter.model.AlarmsManager.EarliestInfo;
import se.chalmers.dat255.sleepfighter.utils.DateTextUtils;
import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Resources res = getResources();

		// Hard code in some sample alarms
		// TODO fetch from where actual alarms will be stored
		Calendar now = new GregorianCalendar();
		List<Alarm> alarms = new ArrayList<Alarm>();
		alarms.add(new Alarm(8, 30));
		alarms.add(new Alarm(7, 0));
		alarms.add(new Alarm(3, 0));
		alarms.add(new Alarm(13, 37));

		AlarmsManager manager = new AlarmsManager();
		manager.set( alarms );

		ListView listView = (ListView) findViewById(R.id.mainAlarmsList);


		AlarmAdapter alarmAdapter = new AlarmAdapter(this, alarms);
		listView.setAdapter(alarmAdapter);

		// Set earliest time text.
		EarliestInfo earliestInfo = manager.getEarliestInfo( now );
		TextView earliestTimeText = (TextView) findViewById( R.id.earliestTimeText );
		earliestTimeText.setText( DateTextUtils.getEarliestText( res, now, earliestInfo ) );
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
