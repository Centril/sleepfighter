package se.chalmers.dat255.sleepfighter.challenge.sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import se.chalmers.dat255.sleepfighter.R;
import se.chalmers.dat255.sleepfighter.activity.ChallengeActivity;
import se.chalmers.dat255.sleepfighter.challenge.Challenge;
import se.chalmers.dat255.sleepfighter.challenge.sort.SortModel.Order;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.google.common.collect.Lists;

public class SortChallenge implements Challenge {
	private ChallengeActivity activity;

	private List<Button> buttons;

	private TextView description;

	private SortModel model;

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
		this.model.setSize( 9 );
		this.model.setGenerator( gen );
	}

	/**
	 * Generates a list of numbers from model and sets a shuffled version of them.
	 */
	private void setNumbers() {
		this.model.generateList( rng );

		this.updateDescription();

		int[] shuffledNumbers = this.model.getShuffledList();

		for ( int i = 0; i < shuffledNumbers.length; ++i ) {
			this.buttons.get( i ).setText( Integer.toString( shuffledNumbers[i]) );
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
		}
	}
}