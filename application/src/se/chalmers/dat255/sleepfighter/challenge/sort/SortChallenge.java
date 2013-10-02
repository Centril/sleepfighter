package se.chalmers.dat255.sleepfighter.challenge.sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import se.chalmers.dat255.sleepfighter.R;
import se.chalmers.dat255.sleepfighter.activity.ChallengeActivity;
import se.chalmers.dat255.sleepfighter.challenge.Challenge;
import se.chalmers.dat255.sleepfighter.challenge.sort.SortModel.Order;
import se.chalmers.dat255.sleepfighter.utils.math.RandomMath;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.TextView;

import com.google.common.collect.Lists;

public class SortChallenge implements Challenge {
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

	private Random rng;

	@Override
	public void start( ChallengeActivity activity ) {
		this.activity = activity;
		this.activity.setContentView( R.layout.challenge_sort );

		this.bindButtons();

		this.setupModel();

		this.description = (TextView) this.activity.findViewById( R.id.challenge_sort_description );

		this.setNumbers();
	}

	/**
	 * Sets up the model.
	 */
	private void setupModel() {
		this.rng = new Random();

		ClusteredGaussianListGenerator gen = new ClusteredGaussianListGenerator();

		this.model = new SortModel();
		this.model.setSize( NUMBERS_COUNT );
		this.model.setGenerator( gen );
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
	private void setNumbers() {
		this.model.generateList( rng );

		this.updateDescription();

		int[] shuffledNumbers = this.model.getShuffledList();

		this.currentHues = this.selectHues( shuffledNumbers.length );

		for ( int i = 0; i < shuffledNumbers.length; ++i ) {
			Button button = this.buttons.get( i );
			button.setEnabled( true );

			button.setText( Integer.toString( shuffledNumbers[i]) );
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
			this.setNumbers();
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
		View button_container = this.activity.findViewById( R.id.challenge_sort_button_container );
		ArrayList<View> touchables = button_container.getTouchables();

		this.buttons = Lists.newArrayListWithCapacity( touchables.size() );

		for ( View view : touchables ) {
			Button button = (Button) view;
			this.buttons.add( button );

			button.setOnClickListener( this.onButtonClickListener );
			button.setOnTouchListener( this.onButtonPressListener );
		}
	}
}