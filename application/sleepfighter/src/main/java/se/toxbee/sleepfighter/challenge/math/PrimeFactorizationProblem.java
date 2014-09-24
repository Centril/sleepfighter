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
 * Challenge: Compute the largest prime factor of a number. 
 */
public class PrimeFactorizationProblem implements MathProblem {
	private static final String TAG = PrimeFactorizationProblem.class.getSimpleName();
	
	// we'll use these primes in the creation of the number.
	private static final int[] PRIMES = new int[]{2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59};

	private static final int MIN_VALUE = 3000;
	private static final int MAX_VALUE = 40000;

	private final Context context;
	
	private int solution;
	
	private String renderedString;
	
	private Random rng;

	public String render() {
		return this.renderedString;
	}
	
	public int solution() {
		Log.d( TAG, "solution is " + solution );
		return this.solution;
	}
	
	// create the number to be prime factorized by the user.
	private int createNumber() {
		int n;
		
		do {
			
			this.solution = 1;
			n = 1;
			int primeFactors = RandomMath.nextRandomRanged(rng, 2, 7); 
			
			for(int i = 0; i < primeFactors; ++i) {
				int randomPrime = PRIMES[rng.nextInt(PRIMES.length)];
				n *= randomPrime;

				// we'll keep track of the larget prime factor(this is the solution to this problem
				if(randomPrime > solution) {
					this.solution = randomPrime;
				}

			}

		} while(n < MIN_VALUE || n > MAX_VALUE);
		
		
		return n;
	}
	
	public void newProblem() {
		
		int n = createNumber();
		
		String format =  context.getResources().getString(R.string.prime_factor_challenge_desc);
		
		this.renderedString = String.format(format, "$" + n + "$");
		Log.d( TAG, solution + "");
	}
	
	public PrimeFactorizationProblem(final Context context, final Random random) {
		this.context = context;
		this.rng =random;
	}
	
}
