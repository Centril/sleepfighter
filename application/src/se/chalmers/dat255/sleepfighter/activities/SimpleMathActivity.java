package se.chalmers.dat255.sleepfighter.activities;

import se.chalmers.dat255.sleepfighter.R;
import se.chalmers.dat255.sleepfighter.challenges.SimpleMathChallenge;
import se.chalmers.dat255.sleepfighter.debug.Debug;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.OnEditorActionListener;

public class SimpleMathActivity extends Activity {

	private SimpleMathChallenge smc;
	private EditText editText;
	private TextView userText;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alarm_challenge_math);
		smc = new SimpleMathChallenge();
		editText = (EditText) findViewById(R.id.answerField);
		userText = (TextView) findViewById(R.id.questionField);
		
		userText.setText(smc.getCalculation());

		editText.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				boolean handled = false;
				if (actionId == EditorInfo.IME_ACTION_DONE) {
					if (Integer.parseInt(editText.getText().toString()) == smc
							.getResult()) {
						next();
						Toast.makeText(getBaseContext(),
								"Alarm deactivated", Toast.LENGTH_SHORT)
								.show();
					} else {
						Toast.makeText(getBaseContext(),
								"Sorry, wrong answer!", Toast.LENGTH_SHORT)
								.show();
						smc.runChallenge();
						userText.setText(smc.getCalculation());
						editText.setText("");
					}
					handled = true;
				}
				return handled;
			}
		});
	}

	public void buttonMath(View view) {
					if (Integer.parseInt(editText.getText().toString()) == smc
							.getResult()) {
						next();
						Toast.makeText(getBaseContext(),
								"Alarm deactivated", Toast.LENGTH_SHORT)
								.show();
					} else {
						Toast.makeText(getBaseContext(),
								"Sorry, wrong answer!", Toast.LENGTH_SHORT)
								.show();
						smc.runChallenge();
						userText.setText(smc.getCalculation());
						editText.setText("");
					}
	}

	private void next() {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
	}

}
