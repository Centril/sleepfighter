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
package se.chalmers.dat255.sleepfighter.challenge.sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import se.chalmers.dat255.sleepfighter.R;
import se.chalmers.dat255.sleepfighter.activity.ChallengeActivity;
import se.chalmers.dat255.sleepfighter.challenge.Challenge;
import se.chalmers.dat255.sleepfighter.challenge.ChallengePrototypeDefinition;
import se.chalmers.dat255.sleepfighter.challenge.ChallengeResolvedParams;
import se.chalmers.dat255.sleepfighter.challenge.sort.SortModel.Order;
import se.chalmers.dat255.sleepfighter.model.challenge.ChallengeType;
import se.chalmers.dat255.sleepfighter.utils.math.RandomMath;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.TextView;

import com.google.common.collect.Lists;

public class SortChallenge implements Challenge {
	/**
	 * PrototypeDefinition for SortChallenge.
	 *
	 * @version 1.0
	 * @since Oct 5, 2013
	 */
	public static class PrototypeDefinition extends ChallengePrototypeDefinition {{
		setType( ChallengeType.SORT );
		add( "color_confusion", PrimitiveValueType.BOOLEAN, true );
	}}

	private static final String STATE_HUES = "hues";
	private static final String STATE_SHUFFLED_NUMBERS = "numbers";
	private static final String STATE_MODEL = "model";

	private static final int HSV_MAX_HUE = 360;
	private static final int HSV_MIN_HUE = 0;
	private static final float HSV_SATURATION = 0.20f;
	private static final float HSV_VALUE = 1f;

	// Unfortunately, colors must be hard-coded since it is dynamic.
	private static final int COLOR_PRESS = Color.BLACK;
	private static final int COLOR_ANSWERED = Color.WHITE;

	private static final int NUMBERS_COUNT = 9;

	private ChallengeActivity activity;

	private TextView description;

	private List<Button> buttons;

	private SortModel model;

	private int[] currentHues;

	private int[] shuffledNumbers;

	private Random rng;

	@Override
	public void start( ChallengeActivity activity, ChallengeResolvedParams params ) {
		this.startCommon( activity );

		this.setupModel();

		this.updateNumbers();
	}

	@Override
	public void start( ChallengeActivity activity, ChallengeResolvedParams params, Bundle state ) {
		this.startCommon( activity );

		// Read stuff from state.
		this.model = state.getParcelable( STATE_MODEL );
		this.shuffledNumbers = state.getIntArray( STATE_SHUFFLED_NUMBERS );
		this.currentHues = state.getIntArray( STATE_HUES );

		this.setNumbers();
	}

	/**
	 * Performs common startup stuff.
	 *
	 * @param activity the ChallengeActivity.
	 */
	private void startCommon( ChallengeActivity activity ) {
		this.activity = activity;
		this.activity.setContentView( R.layout.challenge_sort );

		this.description = (TextView) this.activity.findViewById( R.id.challenge_sort_description );

		this.bindButtons();

		this.rng = new Random();
	}

	@Override
	public Bundle savedState() {
		Bundle outState = new Bundle();

		outState.putParcelable( STATE_MODEL, this.model );
		outState.putIntArray( STATE_SHUFFLED_NUMBERS, this.shuffledNumbers );
		outState.putIntArray( STATE_HUES, this.currentHues );

		return outState;
	}

	/**
	 * Sets up the model.
	 */
	private void setupModel() {
		this.model = new SortModel();
		this.model.setSize( NUMBERS_COUNT );
		this.model.setGenerator( this.makeGenerator() );
	}

	/**
	 * Makes and returns a NumberListGenerator to use.
	 *
	 * @return the made generator.
	 */
	private NumberListGenerator makeGenerator() {
		return new ClusteredGaussianListGenerator();
	}

	/**
	 * Randomizes an array of hues (in HSV) with size.
	 *
	 * @param size the size of array.
	 * @return the array of hues of size.
	 */
	private int[] selectHues( int size ) {
		int[] hues = new int[size];

		for ( int i = 0; i < hues.length; ++i ) {
			hues[i] = RandomMath.nextRandomRanged( this.rng, HSV_MIN_HUE, HSV_MAX_HUE / 36 ) * 36;
		}

		return hues;
	}

	/**
	 * Completes a hue with hard coded saturation & value making an ARGB color.
	 *
	 * @param hue the hue to use in color.
	 * @return the color.
	 */
	private int computeHSVWithHue( int hue ) {
		return Color.HSVToColor( new float[] { hue, HSV_SATURATION, HSV_VALUE } );
	}

	/**
	 * Generates a list of numbers from model and sets a shuffled version of them.
	 */
	private void updateNumbers() {
		this.model.generateList( rng );
		this.shuffledNumbers = this.model.getShuffledList();
		this.currentHues = this.selectHues( this.shuffledNumbers.length );

		this.setNumbers();
	}

	/**
	 * Sets the numbers stored in {@link #shuffledNumbers} and updates the description from model.
	 */
	private void setNumbers() {
		this.updateDescription();

		for ( int i = 0; i < this.shuffledNumbers.length; ++i ) {
			Button button = this.buttons.get( i );
			button.setEnabled( true );

			button.setText( Integer.toString( this.shuffledNumbers[i]) );
			button.setBackgroundColor( this.computeHSVWithHue( this.currentHues[i] ) );
		}
	}

	/**
	 * Updates the description text according to model.
	 */
	private void updateDescription() {
		int descriptionId = this.model.getSortOrder() == Order.ASCENDING ? R.string.challenge_sort_ascending : R.string.challenge_sort_descending;
		this.description.setText( this.activity.getString( descriptionId ) );
	}

	/**
	 * Called when the user has sorted correctly.
	 */
	private void challengeCompleted() {
		this.description.setText( this.activity.getString( R.string.challenge_sort_done ) );

		this.activity.complete();
	}

	/**
	 * Called when a number button was clicked.<br/>
	 * If number is next, advance step, else re-generate numbers.
	 *
	 * @param button the button.
	 */
	private void numberClicked( Button button ) {
		int number = Integer.parseInt( (String) button.getText() );

		if ( this.model.isNextNumber( number ) ) {
			this.model.advanceStep( number );

			button.setEnabled( false );
			button.setBackgroundColor( COLOR_ANSWERED );

			if ( this.model.isFinished() ) {
				this.challengeCompleted();
			}
		} else {
			this.updateNumbers();
		}
	}

	private OnClickListener onButtonClickListener = new OnClickListener() {
		@Override
		public void onClick( View v ) {
			numberClicked( (Button) v );
		}
	};

	private OnTouchListener onButtonPressListener = new OnTouchListener() {
		@Override
		public boolean onTouch( View v, MotionEvent event ) {
			switch ( event.getAction() ) {
			case MotionEvent.ACTION_DOWN:
				v.setBackgroundColor( COLOR_PRESS );
				return false;

			default:
				return false;
			}
		}
	};

	/**
	 * Called when a number button is clicked.
	 */
	private void bindButtons() {
		View buttonContainer = this.activity.findViewById( R.id.challenge_sort_button_container );
		ArrayList<View> touchables = buttonContainer.getTouchables();

		this.buttons = Lists.newArrayListWithCapacity( touchables.size() );

		for ( View view : touchables ) {
			Button button = (Button) view;
			this.buttons.add( button );

			button.setOnClickListener( this.onButtonClickListener );
			button.setOnTouchListener( this.onButtonPressListener );
		}
	}

	@Override
	public void onPause() {
	}

	@Override
	public void onResume() {
	}

	@Override
	public void onDestroy() {
	}

}