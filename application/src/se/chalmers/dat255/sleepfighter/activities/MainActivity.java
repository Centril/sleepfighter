package se.chalmers.dat255.sleepfighter.activities;

import java.util.ArrayList;
import java.util.List;

import se.chalmers.dat255.sleepfighter.R;
import se.chalmers.dat255.sleepfighter.model.Alarm;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ListView;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		ListView listView = (ListView) findViewById(R.id.listview);

		// Hard code in some sample alarms
		// TODO fetch from where actual alarms will be stored
		List<Alarm> alarms = new ArrayList<Alarm>();
		alarms.add(new Alarm(8, 30));
		alarms.add(new Alarm(7, 0));
		
		AlarmAdapter alarmAdapter = new AlarmAdapter(this, alarms);
		listView.setAdapter(alarmAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
