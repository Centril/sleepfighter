package se.chalmers.dat255.sleepfighter.activity;

import se.chalmers.dat255.sleepfighter.R;
import se.chalmers.dat255.sleepfighter.adapter.MemoryAdapter;
import se.chalmers.dat255.sleepfighter.challenge.MemoryChallenge;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;
import android.view.View;
import android.view.animation.AnimationUtils;

public class MemoryActivity extends ChallengeActivity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		MemoryChallenge challenge = new MemoryChallenge();
		challenge.start(this);
		

	}
}
