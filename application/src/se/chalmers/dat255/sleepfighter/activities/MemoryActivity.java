package se.chalmers.dat255.sleepfighter.activities;

import se.chalmers.dat255.sleepfighter.R;
import se.chalmers.dat255.sleepfighter.challenges.MemoryChallenge;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;
import android.view.View;;

public class MemoryActivity extends Activity {

	private MemoryChallenge mc;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alarm_challenge_memory);
	
		GridView gridview = (GridView) findViewById(R.id.memory_gridview);
		
		gridview.setAdapter(new MemoryAdapter(this));

		    gridview.setOnItemClickListener(new OnItemClickListener() {
		        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		            Toast.makeText(MemoryActivity.this, "" + position, Toast.LENGTH_SHORT).show();
		        }
		    });

		
		
		
		
		mc = new MemoryChallenge();
	}

	private void next() {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
	}
}
