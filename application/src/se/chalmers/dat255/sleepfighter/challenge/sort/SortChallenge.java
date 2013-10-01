package se.chalmers.dat255.sleepfighter.challenge.sort;

import java.util.Arrays;
import java.util.Random;

import android.util.Log;

import se.chalmers.dat255.sleepfighter.activity.ChallengeActivity;
import se.chalmers.dat255.sleepfighter.challenge.Challenge;

public class SortChallenge implements Challenge {
	private static final String TAG = SortChallenge.class.getSimpleName();

	private ChallengeActivity activity;

	private SortModel model;

	private Random rng;

	public SortChallenge() {
		this.setupModel();
	}

	private void setupModel() {
		this.rng = new Random();

		ClusteredGaussianListGenerator gen = new ClusteredGaussianListGenerator();

		this.model = new SortModel();
		this.model.setSize( 9 );
		this.model.setGenerator( gen );

		Log.d( TAG, "----- START	DUMPING GENERATED NUMBERS -----" );

		for ( int i = 0; i < 30; ++i ) {
			this.model.generateList( this.rng );
			Log.d( TAG, Arrays.toString( this.model.getListCopy() ) );
		}

		Log.d( TAG, "----- END		DUMPING GENERATED NUMBERS -----" );
	}

	@Override
	public void start( ChallengeActivity activity ) {
		this.activity = activity;
	}
}