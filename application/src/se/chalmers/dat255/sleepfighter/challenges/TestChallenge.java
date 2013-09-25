package se.chalmers.dat255.sleepfighter.challenges;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import se.chalmers.dat255.sleepfighter.R;
import se.chalmers.dat255.sleepfighter.activities.ChallengeActivity;
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
