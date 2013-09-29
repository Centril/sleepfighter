package se.chalmers.dat255.sleepfighter.challenge;

import se.chalmers.dat255.sleepfighter.R;
import se.chalmers.dat255.sleepfighter.activity.ChallengeActivity;
import se.chalmers.dat255.sleepfighter.adapter.MemoryAdapter;
import se.chalmers.dat255.sleepfighter.model.Memory;
import se.chalmers.dat255.sleepfighter.utils.debug.Debug;
import android.os.Handler;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
/**
 * Example implementation of Challenge.
 */
public class MemoryChallenge implements Challenge, OnItemClickListener {

	private ChallengeActivity act;
	
	private Memory mem;
	
	private MemoryCardView flippedCard = null;
	
	private final static int COLS = 2;

	private final static int ROWS = 3;
	
	private int remainingPairs;

	private Handler handler = new Handler();
	
	// true when you are waiting for the cards the user has selected to flip over and become hidden again
	// when the user has selected two cards that are different, the user is given
	// some seconds to view the cards. 
	private boolean waitingForCardsToFlipOver = false;
	
	
	private void fadeOutRemove(View v) {
		// fade out and remove
        v.startAnimation(AnimationUtils.loadAnimation(act, android.R.anim.fade_out));
        v.setVisibility(View.INVISIBLE);
        
	}
	
	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
       // Toast.makeText(act, "" + position, Toast.LENGTH_SHORT).show();
      
		
		if(waitingForCardsToFlipOver) {
			// we are still waiting, so you can't pick any cards now.
			return;
        }
		
        final MemoryCardView card = (MemoryCardView)v;

        if(flippedCard == card) {
        	// you can't flip a card, and then flip over the same card again.
        	// you must pick two separate cards. 
        	return;
        }
       
        card.flip();
        
        if(flippedCard != null ) {
       
        	if(mem.getCard(card.getPosition())  == mem.getCard(flippedCard.getPosition())) {
        		// remove cards
        		
        		
        		
        		fadeOutRemove(card);
        		fadeOutRemove(flippedCard);
        		flippedCard = null;
        		--this.remainingPairs;
        		if(this.remainingPairs == 0) {
        			Toast.makeText(act.getBaseContext(), "You won!",
    						Toast.LENGTH_SHORT).show();
        			// TODO: now we should return to the start menu. 
        		}
        	} else {
        	   	waitingForCardsToFlipOver = true;
        		handler.postDelayed(new Runnable() {
                	// wait one second before flipping over
                    public void run() {
                		flippedCard.flip();
                		card.flip();
                		flippedCard = null; 
                		waitingForCardsToFlipOver = false;
                    }
                }, 1000);
     
        	}
        		 
        } else 
        	flippedCard = card;
        
        
    }
	
	@Override
	public void start(final ChallengeActivity act) {
		this.act = act;
	
		act.setContentView(R.layout.activity_alarm_challenge_memory);
		
		GridView gridview = (GridView) act.findViewById(R.id.memory_gridview);
		
		mem = new Memory(ROWS, COLS);
		Debug.d(mem.toString());
		gridview.setAdapter(new MemoryAdapter(act, mem));
		this.remainingPairs = mem.getNumPairs();

		gridview.setOnItemClickListener(this);
	}

}

