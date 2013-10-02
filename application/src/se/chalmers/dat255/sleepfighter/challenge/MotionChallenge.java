package se.chalmers.dat255.sleepfighter.challenge;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import se.chalmers.dat255.sleepfighter.R;
import se.chalmers.dat255.sleepfighter.activity.ChallengeActivity;
import se.chalmers.dat255.sleepfighter.utils.debug.Debug;
import se.chalmers.dat255.sleepfighter.utils.motion.MotionControl;
import android.app.Activity;
import android.widget.TextView;

/**
 * A challenge that requires the player to move and rotate the device.
 */
public class MotionChallenge implements Challenge, PropertyChangeListener {

	private MotionControl mc;
	private Activity activity;
	private TextView tv;

	@Override
	public void start(ChallengeActivity activity) {
		Debug.d("Starting MotionChallenge");
		this.mc = new MotionControl(activity);
		this.mc.addListener(this);
		this.activity = activity;
		activity.setContentView(R.layout.alarm_challenge_motion);
		tv = (TextView) activity.findViewById(R.id.motionText);
	}

	@Override
	public void propertyChange(PropertyChangeEvent arg0) {
		// tv.setText((mc.getFloatList()).toString());
		tv.setText(Float.toString((mc.getZ())));
	}
}
