package se.chalmers.dat255.sleepfighter.challenges;

import java.util.Random;

import se.chalmers.dat255.sleepfighter.R;
import se.chalmers.dat255.sleepfighter.activities.ChallengeActivity;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
/**
 * Example implementation of Challenge.
 */
public class TestChallenge implements Challenge {

	@Override
	public void start(final ChallengeActivity activity) {
		activity.setContentView(R.layout.alarm_challenge_test);
		Button completeButton = (Button) activity
				.findViewById(R.id.btn_complete);
		Button failButton = (Button) activity.findViewById(R.id.btn_fail);
		
		Random random = new Random();
		int randomInt = random.nextInt(2);
		completeButton.setBackgroundColor((randomInt) == 0 ? Color.RED : Color.GREEN);
		failButton.setBackgroundColor((randomInt) == 0 ? Color.GREEN : Color.RED);
		
		completeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				activity.complete();
			}
		});

		failButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				activity.fail();
			}
		});
	}

}
