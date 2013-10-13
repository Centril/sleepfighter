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
package se.chalmers.dat255.sleepfighter.challenge.math;

import java.util.Random;

import android.content.Context;

import se.chalmers.dat255.sleepfighter.R;
import se.chalmers.dat255.sleepfighter.utils.debug.Debug;
import se.chalmers.dat255.sleepfighter.utils.math.RandomMath;

/*
 * Challenge: Compute the greatest common divisor of two numbers. 
 */
public class GCDProblem implements MathProblem {
	final Context context;
	
	// ranges of the numbers to compute the gcd of. 
	final static int MIN = 1000;
	final static int MAX = 7000;
	
	private int solution;
	private int number1;
	private int number2;
	
	String renderedString;
	
	private Random rng = new Random();

	public String render() {
		return this.renderedString;
	}
	
	public int solution() {
		Debug.d("solution is " + solution);
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
		Debug.d(solution + "");
	}
	
	
	public GCDProblem(final Context context) {
		this.context = context;
	}
	
}
