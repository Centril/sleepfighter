/*
 * Copyright 2014 toxbee.se
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package se.toxbee.sleepfighter.challenge.memory;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

import se.toxbee.sleepfighter.R;
import se.toxbee.sleepfighter.challenge.BaseChallenge;
import se.toxbee.sleepfighter.challenge.ChallengePrototypeDefinition;
import se.toxbee.sleepfighter.challenge.ChallengeResolvedParams;
import se.toxbee.sleepfighter.model.challenge.ChallengeType;

/**
 * Example implementation of Challenge.
 *
 * @author Eric Arnebäck
 * @version 1.0
 * @since Sep 28, 2013
 */
public class MemoryChallenge extends BaseChallenge implements View.OnClickListener {
	private static final String TAG = MemoryChallenge.class.getSimpleName();

	/**
	 * PrototypeDefinition for MemoryChallenge.
	 *
	 * @version 1.0
	 * @since Oct 5, 2013
	 */
	public static class PrototypeDefinition extends ChallengePrototypeDefinition {{
		setType( ChallengeType.MEMORY );
	}}

	private Memory mem;
	
	private List<MemoryCardView> cards;
	
	private MemoryCardView flippedCard = null;

	private final static int COLS = 2;

	private final static int ROWS = 4;

	private Handler handler = new Handler();

	MemoryCardImageDatabase database;
	
	// true when you are waiting for the cards the user has selected to flip over and become hidden again
	// when the user has selected two cards that are different, the user is given
	// some seconds to view the cards. 
	private boolean waitingForCardsToFlipOver = false;
	private boolean waitingForLastCardsToBeRemoved = false;

	private void fadeOutRemove(View v) {
		// fade out and remove
        v.startAnimation(AnimationUtils.loadAnimation(activity(), android.R.anim.fade_out));
        v.setVisibility(View.INVISIBLE);
        
	}

	public void onClick(View v) {
		Log.d( TAG, "button click" );
		
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
        		
        		// remove form model
        		mem.matchPair(card.getPosition(), flippedCard.getPosition());
        		
        		// remove from the gui
        		fadeOutRemove(card);
        		fadeOutRemove(flippedCard);
        		flippedCard = null;
        		if(this.mem.isGameOver()) {
        			
        			// we will wait some seconds before finishing the game,
        			// otherwise we won't get to see the cool fade out animation 
        			// one last time :-)
        			
        			waitingForLastCardsToBeRemoved = true;
            		handler.postDelayed(new Runnable() {
                        public void run() {
                    		// return to start menu.
                			complete();	
                        }
                    }, 600);
        			
        	
        		}
        	} else {
        	   	waitingForCardsToFlipOver = true;
        	   	MemoryChallenge.this.makeAllCardsUnclickable();
        		handler.postDelayed(new Runnable() {
                	// wait one second before flipping over
                    public void run() {
                		flippedCard.flip();
                		card.flip();
                		flippedCard = null; 
                		waitingForCardsToFlipOver = false;
                		MemoryChallenge.this.makeAllCardsClickable();
                    }
                }, 400);
     
        	}
        		 
        } else 
        	flippedCard = card;
        
        
    }

	private void makeAllCardsUnclickable() {
		for(MemoryCardView card : this.cards) {
			card.setClickable(false);
		}
	}
	
	private void makeAllCardsClickable() {
		for(MemoryCardView card : this.cards) {
			card.setClickable(true);
		}
	}

	// assign the buttons their listeners. 
	private void commonStart(int flippedCardPosition) {
		activity().setContentView( R.layout.challenge_memory );

		this.bindButtons();
		
		if(flippedCardPosition != -1) {
			this.flippedCard = cards.get(flippedCardPosition);
			this.flippedCard.show();
		}
	}

	/**
	 * Binds all memory cards/buttons.
	 */
	private void bindButtons() {
		View buttonContainer = this.activity().findViewById( R.id.challenge_memory_button_container );
		ArrayList<View> touchables = buttonContainer.getTouchables();

		this.cards = Lists.newArrayListWithCapacity( touchables.size() );

		for ( int pos = 0; pos < touchables.size(); ++pos ) {
			MemoryCardView card = (MemoryCardView) touchables.get( pos );
			this.cards.add( card );

			if ( mem.isUnoccupied( pos ) ) {
				// Already removed cards should not be shown.
				card.setVisibility( View.INVISIBLE );
			} else {
				card.setOnClickListener( this );
				card.setPosition( pos );

				String image = database.getImage( mem.getCard( pos ) );
				card.setImage( image );
			}
		}
	}
	
	private static final String MEMORY = "memory";
	private static final String MEMORY_CARD_IMAGE_DATABASE = "memory_card_image_database";
	
	private static final String FLIPPED_CARD_POSITION = "flipped_card_position";

	@Override
	public void start(final Activity activity, ChallengeResolvedParams params) {
		super.start( activity, params );

		mem = new Memory(ROWS, COLS);
		Log.d( TAG, mem.toString());
		this.database = new MemoryCardImageDatabase(mem);

		commonStart(-1);
	}

	@Override
	public void start( Activity activity, ChallengeResolvedParams params, Bundle state ) {
		super.start( activity, params );

		this.mem = state.getParcelable(MEMORY);
		this.database = state.getParcelable(MEMORY_CARD_IMAGE_DATABASE);
		
		commonStart(state.getInt(FLIPPED_CARD_POSITION));
	}

	@Override
	public Bundle savedState() {
		Bundle outState = new Bundle();

		outState.putParcelable(MEMORY, mem);
		outState.putParcelable(MEMORY_CARD_IMAGE_DATABASE, this.database);
			
		// if one card is flipped over when we rotate, then that card should obviously also be visible after
		// the rotation is over.
		if(this.flippedCard != null && !waitingForCardsToFlipOver) {
			outState.putInt(FLIPPED_CARD_POSITION, this.flippedCard.getPosition());
		} else {
			outState.putInt(FLIPPED_CARD_POSITION, -1);
		}
		
		return outState;
	}
}