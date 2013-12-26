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
package se.toxbee.sleepfighter.challenge.math;

import java.util.Random;

import android.content.Context;

import se.toxbee.sleepfighter.R;
import se.toxbee.sleepfighter.utils.debug.Debug;
import se.toxbee.sleepfighter.utils.math.RandomMath;

/*
 * Challenge: Compute the largest prime factor of a number. 
 */
public class PrimeFactorizationProblem implements MathProblem {
	
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
		Debug.d("solution is " + solution);
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
		Debug.d(solution + "");
	}
	
	public PrimeFactorizationProblem(final Context context, final Random random) {
		this.context = context;
		this.rng =random;
	}
	
}
