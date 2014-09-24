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

import android.content.Context;
import android.util.Log;

import java.util.Random;

import se.toxbee.commons.math.RandomMath;
import se.toxbee.sleepfighter.R;

/*
 * Challenge: Compute the greatest common divisor of two numbers. 
 */
public class GCDProblem implements MathProblem {
	private static final String TAG = GCDProblem.class.getSimpleName();
	private final Context context;
	
	// ranges of the numbers to compute the gcd of. 
	private final static int MIN = 1000;
	private final static int MAX = 7000;
	
	private int solution;
	private int number1;
	private int number2;
	
	private String renderedString;
	
	private Random rng = new Random();

	public String render() {
		return this.renderedString;
	}
	
	public int solution() {
		Log.d( TAG, "solution is " + solution );
		return this.solution;
	}

	
	private void createNumbers() {
		this.number1 = 1;
		this.number2 = 1;
		
		
		// ensure that the gcd isn't too small, otherwise the generated problem may be too easy. 
		while(gcd(number1, number2) == 1 || gcd(number1, number2) == 2 || gcd(number1, number2) == 3) {
			this.number1 = RandomMath.nextRandomRanged(rng, MIN, MAX);
			this.number2 = RandomMath.nextRandomRanged(rng, MIN, MAX);
		}
		
		
		this.solution = gcd(number1, number2);

		
	}
	
	// compute the gcd of two numbers using Euclid's algorithm
	private int gcd(int a, int b) {
		int t;
		while(b != 0) {
			t = b;
			b = a % t;
			a = t;
		}
		return a;
	}
	
	public void newProblem() {
		
		createNumbers();
		
		String format =  context.getResources().getString(R.string.gdc_challenge_desc);
		this.renderedString =String.format(format, "$" + number1 + "$",  "$" + number2 + "$");
		Log.d( TAG, solution + "");
	}
	
	
	public GCDProblem(final Context context) {
		this.context = context;
	}
	
}
