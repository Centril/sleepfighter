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
package se.toxbee.sleepfighter.challenge.sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import se.toxbee.sleepfighter.R;
import se.toxbee.sleepfighter.challenge.BaseChallenge;
import se.toxbee.sleepfighter.challenge.ChallengePrototypeDefinition;
import se.toxbee.sleepfighter.challenge.ChallengeResolvedParams;
import se.toxbee.sleepfighter.challenge.sort.SortModel.Order;
import se.toxbee.sleepfighter.model.challenge.ChallengeType;
import se.toxbee.sleepfighter.utils.collect.PrimitiveArrays;
import se.toxbee.sleepfighter.utils.math.RandomMath;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.TextView;

import com.google.common.collect.Lists;

/**
 * SortChallenge is a challenge where user sorts generated numbers ASC/DESC.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Oct 6, 2013
 */
public class SortChallenge extends BaseChallenge {
	/**
	 * PrototypeDefinition for SortChallenge.
	 *
	 * @version 1.0
	 * @since Oct 5, 2013
	 */
	public static class PrototypeDefinition extends ChallengePrototypeDefinition {{
		setType( ChallengeType.SORT );
		add( "color_confusion", PrimitiveValueType.BOOLEAN, true, Lists.newArrayList( "color_saturation_confusion" ) );
		add( "color_saturation_confusion", PrimitiveValueType.BOOLEAN, true );
	}}

	private static final String STATE_MODEL = "model";
	private static final String STATE_COLORS = "hues";
	private static final String STATE_SHUFFLED_NUMBERS = "numbers";
	private static final String STATE_BUTTONS_STATE = "buttons_state";

	private static final int HSV_MAX_HUE = 360;
	private static final int HSV_MIN_HUE = 0;
	private static final float HSV_SATURATION_MIN = 0.20f;
	private static final float HSV_VALUE = 1f;

	// These are "constant".
	private int colorPressed;
	private int colorAnswered;
	private int colorDefault;

	private TextView description;

	private List<Button> buttons;

	private Random rng;

	// Model states.
	private SortModel model;

	private int[] currentColors;

	private int[] shuffledNumbers;

	// Config param variables:
	private boolean colorConfusion;
	private boolean saturationConfusion;

	@Override
	public void start( Activity activity, ChallengeResolvedParams params ) {
		this.startCommon( activity, params );

		this.setupModel();

		this.updateNumbers();
	}

	@Override
	public void start( Activity activity, ChallengeResolvedParams params, Bundle state ) {
		this.startCommon( activity, params );

		// Read stuff from state.
		this.model = state.getParcelable( STATE_MODEL );
		this.shuffledNumbers = state.getIntArray( STATE_SHUFFLED_NUMBERS );

		if ( this.colorConfusion ) {
			this.currentColors = state.getIntArray( STATE_COLORS );
		}

		// Fix issue #11 Remember the state of buttons.
		this.setNumbers( state.getBooleanArray( STATE_BUTTONS_STATE ));
	}

	/**
	 * Performs common startup stuff.
	 *
	 * @param activity the ChallengeActivity.
	 * @param params the resolved config params.
	 */
	private void startCommon( Activity activity, ChallengeResolvedParams params ) {
		super.start( activity, params );

		// Read all colors from xml.
		this.colorPressed = this.getColor( R.color.challenge_sort_press );
		this.colorAnswered = this.getColor( R.color.challenge_sort_answered );
		this.colorDefault = this.getColor( R.color.challenge_sort_default );

		// Store all interesting params.
		this.colorConfusion = this.params().getBoolean( "color_confusion" );
		this.saturationConfusion = this.params().getBoolean( "color_saturation_confusion" );

		// Init activity, buttons, etc.
		this.activity().setContentView( R.layout.challenge_sort );

		this.description = (TextView) this.activity().findViewById( R.id.challenge_sort_description );

		this.bindButtons();

		this.rng = new Random();
	}

	private int getColor( int id ) {
		return activity().getResources().getColor( id );
	}

	@Override
	public Bundle savedState() {
		Bundle outState = new Bundle();

		outState.putParcelable( STATE_MODEL, this.model );
		outState.putIntArray( STATE_SHUFFLED_NUMBERS, this.shuffledNumbers );

		// Fix issue #11 Remember the state of buttons.
		outState.putBooleanArray( STATE_BUTTONS_STATE, this.getButtonsState() );

		if ( this.colorConfusion ) {
			outState.putIntArray( STATE_COLORS, this.currentColors );
		}

		return outState;
	}

	/**
	 * Returns the state of the buttons as boolean array (enabled/disabled).
	 *
	 * @return the states.
	 */
	private boolean[] getButtonsState() {
		boolean[] buttonsState = new boolean[this.buttons.size()];
		for ( int i = 0; i < this.buttons.size(); ++i ) {
			buttonsState[i] = this.buttons.get( i ).isEnabled();
		}
		return buttonsState;
	}

	/**
	 * Sets up the model.
	 */
	private void setupModel() {
		this.model = new SortModel();
		this.model.setSize( this.buttons.size() );
	}

	/**
	 * Makes and returns a NumberListGenerator to use.
	 *
	 * @return the made generator.
	 */
	private NumberListGenerator makeGenerator() {
		return this.rng.nextBoolean() ? new ClusteredGaussianListGenerator() : new PermutatingListGenerator();
	}

	/**
	 * Randomizes an array of colors (in HSV) with size.
	 *
	 * @param size the size of array.
	 * @return the array of colors of size.
	 */
	private int[] selectColors( int size ) {
		int[] colors = new int[size];

		for ( int i = 0; i < colors.length; ++i ) {
			// Generate hue.
			int hue = RandomMath.nextRandomRanged( this.rng, HSV_MIN_HUE, HSV_MAX_HUE / 36 ) * 36;

			// Generate saturation.
			float saturation;
			if ( this.saturationConfusion ) {
				int minSat = (int) (HSV_SATURATION_MIN * 100);
				saturation = RandomMath.nextRandomRanged( this.rng, minSat, 100 - minSat ) / 100f;
			} else {
				saturation = HSV_SATURATION_MIN;
			}

			colors[i] = this.computeHSVWithHue( hue, saturation );
		}

		return colors;
	}

	/**
	 * Completes a hue and saturation with hard coded value making an ARGB color.
	 *
	 * @param hue the hue to use in color.
	 * @param saturation the saturation to use in color.
	 * @return the color.
	 */
	private int computeHSVWithHue( int hue, float saturation ) {
		return Color.HSVToColor( new float[] { hue, saturation, HSV_VALUE } );
	}

	/**
	 * Generates a list of numbers from model and sets a shuffled version of them.
	 */
	private void updateNumbers() {
		this.model.setGenerator( this.makeGenerator() );
		this.model.generateList( rng );
		this.shuffledNumbers = this.model.getShuffledList();

		if ( this.colorConfusion ) {
			this.currentColors = this.selectColors( this.shuffledNumbers.length );
		}

		// Fix issue #11 Remember the state of buttons.
		this.setNumbers( PrimitiveArrays.filled( true, this.buttons.size() ));
	}

	/**
	 * Sets the numbers stored in {@link #shuffledNumbers} and updates the description from model.
	 */
	private void setNumbers( boolean[] enabledNumbers ) {
		this.updateDescription();

		for ( int i = 0; i < this.shuffledNumbers.length; ++i ) {
			boolean enabled = enabledNumbers[i];

			Button button = this.buttons.get( i );
			button.setEnabled( enabled );

			button.setText( Integer.toString( this.shuffledNumbers[i]) );

			if ( enabled ) {
				button.setBackgroundColor( this.colorConfusion ? this.currentColors[i] : this.colorDefault );
			}
		}
	}

	/**
	 * Updates the description text according to model.
	 */
	private void updateDescription() {
		int descriptionId = this.model.getSortOrder() == Order.ASCENDING ? R.string.challenge_sort_ascending : R.string.challenge_sort_descending;
		this.description.setText( this.activity().getString( descriptionId ) );
	}

	/**
	 * Called when a number button was clicked.<br/>
	 * If number is next, advance step, else re-generate numbers.
	 *
	 * @param button the button.
	 */
	private void numberClicked( Button button ) {
		int index = this.buttons.indexOf( button );
		int number = this.shuffledNumbers[index];

		if ( this.model.isNextNumber( number ) ) {
			this.advanceStep( number, button );

			if ( this.model.isFinished() ) {
				this.complete();
			}
		} else {
			this.updateNumbers();
		}
	}

	private void advanceStep( int number, Button button ) {
		this.model.advanceStep( number );

		button.setEnabled( false );
		button.setBackgroundColor( colorAnswered );
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
				v.setBackgroundColor( colorPressed );
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
		View buttonContainer = this.activity().findViewById( R.id.challenge_sort_button_container );
		ArrayList<View> touchables = buttonContainer.getTouchables();

		this.buttons = Lists.newArrayListWithCapacity( touchables.size() );

		for ( View view : touchables ) {
			Button button = (Button) view;
			this.buttons.add( button );

			button.setOnClickListener( this.onButtonClickListener );
			button.setOnTouchListener( this.onButtonPressListener );
		}
	}
}