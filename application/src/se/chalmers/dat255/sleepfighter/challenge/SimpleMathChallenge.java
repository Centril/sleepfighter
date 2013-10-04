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
package se.chalmers.dat255.sleepfighter.challenge;

import java.util.Random;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

import se.chalmers.dat255.sleepfighter.R;
import se.chalmers.dat255.sleepfighter.activity.ChallengeActivity;
import se.chalmers.dat255.sleepfighter.challenge.math.MathProblem;
import se.chalmers.dat255.sleepfighter.challenge.math.SimpleProblem;
import se.chalmers.dat255.sleepfighter.utils.debug.Debug;
import android.annotation.SuppressLint;
import android.os.Build;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebSettings.RenderPriority;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

/**
 * A Class for randomly generating simple arithmetic challenges.
 * 
 * @author Laszlo Sall Vesselenyi, Danny Lam, Johan Hasselqvist
 */

public class SimpleMathChallenge implements Challenge {
	
	MathProblem problem;
	

	public void runChallenge() {
		this.problem.newProblem();
	}

	/**
	 * An activity with the math layout; 
	 * TextView, EditText and an answer-Button.
	 * 
	 */
	
	@Override
	public void start(final ChallengeActivity activity) {
		
		// TODO: randomize math challenge
		problem = new SimpleProblem();
		
		activity.setContentView(R.layout.alarm_challenge_math);
		runChallenge();

		/*final TextView userText = (TextView) activity
				.findViewById(R.id.questionField);
		*/
		final EditText editText = (EditText) activity
				.findViewById(R.id.answerField);
		
		
		editText.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				boolean handled = false;
				if (actionId == EditorInfo.IME_ACTION_DONE) {
					handleAnswer(editText, activity);
					handled = true;
				}
				return handled;
			}
		});

		Button answerButton = (Button) activity.findViewById(R.id.btnAnswer);
		answerButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				handleAnswer(editText, activity);
			}
		});
		
		// make the keyboard appear.
	/*	InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
	*/
		setupWebview(activity);
		renderMathProblem(activity);
	}
	
	public static String open_html =
"<!DOCTYPE html><html lang=\"en\" xmlns:m=\"http://www.w3.org/1998/Math/MathML\"><head><meta charset=\"utf-8\"><link rel=\"stylesheet\" href=\"file:///android_asset/jqmath-0.4.0.css\"><script src=\"file:///android_asset/jquery-1.4.3.min.js\"></script><script src=\"file:///android_asset/jqmath-etc-0.4.0.min.js\"></script></head><html>";
public static String close_html = "</html>";
	
	@SuppressLint({ "SetJavaScriptEnabled", "NewApi", "InlinedApi" })
	public void setupWebview(final ChallengeActivity activity) {
		final WebView w = (WebView)  activity.findViewById(R.id.math_webview);
		w.getSettings().setJavaScriptEnabled(true);
		
		// make rendering faster.
		w.getSettings().setRenderPriority(RenderPriority.HIGH);
		w.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
		
		if(Build.VERSION.SDK_INT >= 11)
			w.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
	}
	
	public void renderMathProblem(final ChallengeActivity activity) {
		final WebView w = (WebView)  activity.findViewById(R.id.math_webview);
		
		String problem = "$" + this.problem.render() + "$";
		
		String html = new StringBuilder().append(open_html).append(problem).append(close_html).toString();
		
		w.loadDataWithBaseURL("file:///android_asset", html, "text/html", "utf-8", "");
		
		double[][] matrixData = { {1d,2d,3d}, {2d,5d,3d}};
		RealMatrix m = MatrixUtils.createRealMatrix(matrixData);
	}
	
	
	/**
	 * Handles what will happen when you answer
	 */

	private void handleAnswer(final EditText editText,
			final ChallengeActivity activity) {
		boolean correctAnswer = false;
		try {
			int guess = Integer.parseInt(editText.getText().toString());
			int solution = this.problem.solution();
			Debug.d(guess + "");
			Debug.d(solution + "");
			
			if (guess == solution) {
				activity.complete();
				correctAnswer = true;
				Toast.makeText(activity.getBaseContext(), "Alarm deactivated!",
						Toast.LENGTH_SHORT).show();
			}
		} catch (NumberFormatException e) {
			// Handles exception when the user answer with empty strings
		}
		if (!correctAnswer) {
			// somehow reload here. 
			Toast.makeText(activity.getBaseContext(), "Wrong answer!",
					Toast.LENGTH_SHORT).show();
			runChallenge();
		
			renderMathProblem(activity);
			editText.setText("");
		}
	}
}
