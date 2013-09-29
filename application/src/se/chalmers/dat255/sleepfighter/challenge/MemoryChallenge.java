package se.chalmers.dat255.sleepfighter.challenge;

import se.chalmers.dat255.sleepfighter.R;
import se.chalmers.dat255.sleepfighter.activity.ChallengeActivity;
import se.chalmers.dat255.sleepfighter.adapter.MemoryAdapter;
import se.chalmers.dat255.sleepfighter.model.Memory;
import se.chalmers.dat255.sleepfighter.utils.debug.Debug;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;
/**
 * Example implementation of Challenge.
 */
public class MemoryChallenge implements Challenge, OnItemClickListener {

	private ChallengeActivity act;
	
	private Memory mem;
	
	private boolean flag = true;
	
	private final static int COLS = 2;

	private final static int ROWS = 3;
	
	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        Toast.makeText(act, "" + position, Toast.LENGTH_SHORT).show();
      
        ImageView view = (ImageView)v;
        
        // fade out and remove
        /*v.startAnimation(AnimationUtils.loadAnimation(act, android.R.anim.fade_out));
        v.setVisibility(View.INVISIBLE);*/
        if(flag)
        	view.setImageResource(android.R.color.white);
        else
        	view.setImageResource(R.drawable.a);
        
        flag = !flag;
    }
	
	@Override
	public void start(final ChallengeActivity act) {
		this.act = act;
	
		act.setContentView(R.layout.activity_alarm_challenge_memory);
		
		GridView gridview = (GridView) act.findViewById(R.id.memory_gridview);
		
		mem = new Memory(ROWS, COLS);
		Debug.d(mem.toString());
		gridview.setAdapter(new MemoryAdapter(act, mem));

		gridview.setOnItemClickListener(this);
	}

}

