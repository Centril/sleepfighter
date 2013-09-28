package se.chalmers.dat255.sleepfighter.challenge;

import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import se.chalmers.dat255.sleepfighter.R;
import se.chalmers.dat255.sleepfighter.activity.ChallengeActivity;
import se.chalmers.dat255.sleepfighter.activity.MemoryActivity;
import se.chalmers.dat255.sleepfighter.adapter.MemoryAdapter;
import se.chalmers.dat255.sleepfighter.utils.debug.Debug;
/**
 * Example implementation of Challenge.
 */
public class MemoryChallenge implements Challenge, OnItemClickListener {

	ChallengeActivity act;
	
	boolean flag = true;
	
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
		
		gridview.setAdapter(new MemoryAdapter(act));

		gridview.setOnItemClickListener(this);
	}

}

