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
package se.chalmers.dat255.sleepfighter.challenge.memory;

import java.util.ArrayList;
import java.util.List;

import se.chalmers.dat255.sleepfighter.R;
import se.chalmers.dat255.sleepfighter.activity.ChallengeActivity;
import se.chalmers.dat255.sleepfighter.challenge.Challenge;
import se.chalmers.dat255.sleepfighter.challenge.ChallengePrototypeDefinition;
import se.chalmers.dat255.sleepfighter.model.Memory;
import se.chalmers.dat255.sleepfighter.model.challenge.ChallengeType;
import se.chalmers.dat255.sleepfighter.utils.debug.Debug;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

/**
 * Example implementation of Challenge.
 *
 * @author Eric Arnebäck
 * @version 1.0
 * @since Sep 28, 2013
 */

public class MemoryChallenge implements Challenge, View.OnClickListener {
	/**
	 * PrototypeDefinition for MemoryChallenge.
	 *
	 * @version 1.0
	 * @since Oct 5, 2013
	 */
	public static class PrototypeDefinition extends ChallengePrototypeDefinition {{
		setType( ChallengeType.MEMORY );
	}}

	private ChallengeActivity act;
	
	private Memory mem;
	
	private MemoryCardView flippedCard = null;
	
	private final static int COLS = 2;

	private final static int ROWS = 4;
	
	private int remainingPairs;

	private Handler handler = new Handler();
	
	// true when you are waiting for the cards the user has selected to flip over and become hidden again
	// when the user has selected two cards that are different, the user is given
	// some seconds to view the cards. 
	private boolean waitingForCardsToFlipOver = false;
	private boolean waitingForLastCardsToBeRemoved = false;
	
	
	private void fadeOutRemove(View v) {
		// fade out and remove
        v.startAnimation(AnimationUtils.loadAnimation(act, android.R.anim.fade_out));
        v.setVisibility(View.INVISIBLE);
        
	}
	
	
	public void onClick(View v) {
       // Toast.makeText(act, "" + position, Toast.LENGTH_SHORT).show();
      
		Debug.d("button click");
		
		if(waitingForCardsToFlipOver || waitingForLastCardsToBeRemoved) {
			// we are still waiting, so you can't pick any cards now, so block all input
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
        			
        			// we will wait some seconds before finishing the game,
        			// otherwise we won't get to see the cool fade out animation 
        			// one last time :-)
        			
        			waitingForLastCardsToBeRemoved = true;
            		handler.postDelayed(new Runnable() {
                        public void run() {
                    		// return to start menu. 
                			Toast.makeText(act.getBaseContext(), "Alarm deactivated!",
            						Toast.LENGTH_SHORT).show();		
                			act.complete();	
                        }
                    }, 600);
        			
        	
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

	// assign the buttons their listeners. 
	private void setupCards() {
		List<MemoryCardView> cards = new ArrayList<MemoryCardView>();
		//R.id.challenge_memory_button_1;
		
		cards.add((MemoryCardView)this.act.findViewById( R.id.challenge_memory_button_1));
		cards.add((MemoryCardView)this.act.findViewById( R.id.challenge_memory_button_2));
		cards.add((MemoryCardView)this.act.findViewById( R.id.challenge_memory_button_3));
		cards.add((MemoryCardView)this.act.findViewById( R.id.challenge_memory_button_4));
		cards.add((MemoryCardView)this.act.findViewById( R.id.challenge_memory_button_5));
		cards.add((MemoryCardView)this.act.findViewById( R.id.challenge_memory_button_6));
		cards.add((MemoryCardView)this.act.findViewById( R.id.challenge_memory_button_7));
		cards.add((MemoryCardView)this.act.findViewById( R.id.challenge_memory_button_8));
		
		MemoryCardImageDatabase database = new MemoryCardImageDatabase(mem);
	
		int pos = 0;
		for ( MemoryCardView card : cards ) {
			card.setOnClickListener( this );
			
			card.setPosition(pos);
	        
			String image = database.getImage(pos);
	        card.setImage(image);
	        
	        ++pos;
		}
	}
	
	@Override
	public void start(final ChallengeActivity act) {
		this.act = act;
	
		act.setContentView(R.layout.activity_alarm_challenge_memory);
		
		// Lock to portrait orientation for now
		// TODO remove if landscape works
		act.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		mem = new Memory(ROWS, COLS);
		Debug.d(mem.toString());
		this.remainingPairs = mem.getNumPairs();
		
		
		setupCards();
	}

	@Override
	public void start( ChallengeActivity activity, Bundle state ) {
		Toast.makeText( activity, "TODO: IMPLEMENT START FROM SAVED STATE", Toast.LENGTH_LONG ).show();
		this.start( activity );
	}

	@Override
	public Bundle savedState() {
		return null;
	}
}