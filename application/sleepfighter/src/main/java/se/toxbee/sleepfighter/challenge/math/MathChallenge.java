/*
 * Copyright 2014 toxbee.se
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package se.toxbee.sleepfighter.challenge.math;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import java.util.Random;

import se.toxbee.commons.math.RandomMath;
import se.toxbee.sleepfighter.R;
import se.toxbee.sleepfighter.android.component.web.CustomWebView;
import se.toxbee.sleepfighter.challenge.BaseChallenge;
import se.toxbee.sleepfighter.challenge.ChallengePrototypeDefinition;
import se.toxbee.sleepfighter.challenge.ChallengeResolvedParams;
import se.toxbee.sleepfighter.model.challenge.ChallengeType;

/**
 * A Class for randomly generating simple arithmetic challenges.
 * 
 * @author Laszlo Sall Vesselenyi, Danny Lam, Johan Hasselqvist, Eric Arnebäck
 */
public class MathChallenge extends BaseChallenge {
	private static final String TAG = MathChallenge.class.getSimpleName();

	/**
	 * PrototypeDefinition for MathChallenge.
	 *
	 * @version 1.0
	 * @since Oct 5, 2013
	 */
	public static class PrototypeDefinition extends ChallengePrototypeDefinition {{
		setType( ChallengeType.MATH );
		
		
		add( "hard_problems", PrimitiveValueType.BOOLEAN, false );
	}}

	//MathProblem problem;
	private ProblemType problemType;
	
	// The problem in string format. Uses jqmath to represent math formulas. 
	private String problemString;
	private int problemSolution;

	private Random rng = new Random();

	public boolean getHardProblemsSetting(ChallengeResolvedParams params) {
		return params.getBoolean("hard_problems");
	}
	
	private void runChallenge(Activity activity) {
		// create challenge object
		
		MathProblem problem = null;
		
		if(problemType == ProblemType.differentiation) {
			problem = new DifferentiationProblem(activity);			
		} else if(problemType == ProblemType.gcd) {
			problem = new GCDProblem(activity);			
		} else if( problemType == ProblemType.prime) {
			problem = new PrimeFactorizationProblem(activity, new Random());			
		}else if(problemType == ProblemType.simple) {
			problem = new SimpleProblem(activity);			
		}else if(problemType == ProblemType.matrix) {
			problem = new MatrixProblem(activity);			
		}else if(problemType == ProblemType.linear_equation) {
			problem = new LinearEquationProblem(activity);			
		}  

		
		problem.newProblem();
		this.problemString = problem.render();
		this.problemSolution = problem.solution();
	}
	
	@Override
	public void start(final Activity activity, ChallengeResolvedParams params) {
	
		boolean hardProblems =  getHardProblemsSetting(params);
		Log.d( TAG, "hard problems: " + hardProblems );

		if(!hardProblems) {
			this.problemType = ProblemType.simple;
		} else {
			// we want a hard challenge
			do {
				this.problemType = RandomMath.randomEnum(rng, ProblemType.class);		
			}while(this.problemType == ProblemType.simple);
		}

		// create a new challenge
		runChallenge(activity);
		
		commonStart(activity, params);
	}
	
	private void commonStart(final Activity activity, ChallengeResolvedParams params ) {
		super.start( activity, params );

		activity.setContentView(R.layout.challenge_math);

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

	
		setupWebview(activity);
		renderMathProblem(activity);
		
	}
	
	private final static String PROBLEM_STRING = "problem_string";
	private final static String PROBLEM_SOLUTION = "problem_solution";
	private final static String PROBLEM_TYPE = "problem_type";
	
	@Override
	public void start( Activity activity, ChallengeResolvedParams params, Bundle state ) {
		this.problemString = state.getString(PROBLEM_STRING);
		this.problemSolution = state.getInt(PROBLEM_SOLUTION);
		this.problemType = (ProblemType) state.getSerializable(PROBLEM_TYPE);
		
		commonStart(activity, params);
	}

	@Override
	public Bundle savedState() {
		Bundle outState = new Bundle();

		outState.putString(PROBLEM_STRING, this.problemString);
		outState.putInt(PROBLEM_SOLUTION, this.problemSolution);
		outState.putSerializable(PROBLEM_TYPE, this.problemType);
		
		return outState;
	}
	
	@SuppressLint({ "SetJavaScriptEnabled", "NewApi", "InlinedApi" })
	private void setupWebview(final Activity activity) {
		final CustomWebView w = (CustomWebView)  activity.findViewById(R.id.math_webview);
		
		w.setWebViewClient(new WebViewClient() {
			public void onPageFinished(WebView view, String url) {
				Log.d( TAG, "page finished");
				
				EditText t = (EditText)MathChallenge.this.activity().findViewById(R.id.answerField);
				t.requestFocus();
				
				// make the keyboard appear once the problem has been rendered. 
				// We'll only do this for the simple problem type.
				// For the harder math problems, the keyboard takes up too much space
				//  and gets in the way. 
				if(problemType == ProblemType.simple) {
					Log.d( TAG, "show keyboard");
					InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
				}
				
				// we only need to do it after it has finished loading for the first time.
				w.setWebViewClient(null);

			}
		});
		
		
		w.getSettings().setJavaScriptEnabled(true);
		
		w.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
		
		if(Build.VERSION.SDK_INT >= 11) {
			w.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		}
	}
	
	private String getStyleSheet() {
		
		String fontSize;
		
		if(this.problemType == ProblemType.simple) {
			// use a larger font for the simple math challenge. 
			fontSize = "font-size: 350%;";
		} else {
			fontSize = "";
		}
		
		return 
				"<style type=\"text/css\">" +
		"body {color: white;" + fontSize + "}" +
		"div {text-align: center; }" +
		"</style>";
	}
	
	/*
	 *We use jqmath to render math formulas. jqmath is a javascript library,
	 *so therefore we need to use a WebVIew to render the formulas.  
	 */
	private void renderMathProblem(final Activity activity) {
		
		final String open_html =
				"<!DOCTYPE html><html lang=\"en\" xmlns:m=\"http://www.w3.org/1998/Math/MathML\"><head><meta charset=\"utf-8\"><link rel=\"stylesheet\" href=\"file:///android_asset/jqmath-0.4.0.css\"><script src=\"file:///android_asset/jquery-1.4.3.min.js\"></script><script src=\"file:///android_asset/jqmath-etc-0.4.0.min.js\"></script></head><html>";
		final String close_html = "</html>";
		
		final CustomWebView w = (CustomWebView)  activity.findViewById(R.id.math_webview);
		
		String problem = "<p style=\"text-align: center;\">" + this.problemString + "</p>";
		
		String html = new StringBuilder().append(open_html).append(this.getStyleSheet()).append(problem).append(close_html).toString();
		
		w.loadDataWithBaseURL("file:///android_asset", html, "text/html", "utf-8", "");	
		w.setBackgroundColor(0x00000000);
		
		
	}
		
	/**
	 * Handles what will happen when you answer
	 */
	private void handleAnswer(final EditText editText, final Activity activity) {
		boolean correctAnswer = false;
		try {
			int guess = Integer.parseInt(editText.getText().toString());
			int solution = this.problemSolution;
			Log.d( TAG, guess + "");
			Log.d( TAG, solution + "");
			
			if (guess == solution) {
				complete();
				correctAnswer = true;
			}
		} catch (NumberFormatException e) {
			// Handles exception when the user answer with empty strings
		}
		if (!correctAnswer) {
			// somehow reload here. 
			runChallenge(activity);
		
			renderMathProblem(activity);
			editText.setText("");
		}
	}

	@Override
	public void onDestroy() {
		CustomWebView w = (CustomWebView) activity().findViewById(R.id.math_webview);
		if (w != null) {
			w.destroy();
		}
	}
}