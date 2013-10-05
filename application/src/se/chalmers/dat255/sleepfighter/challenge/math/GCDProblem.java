package se.chalmers.dat255.sleepfighter.challenge.math;

import java.util.Random;

import se.chalmers.dat255.sleepfighter.utils.debug.Debug;
import se.chalmers.dat255.sleepfighter.utils.math.RandomMath;

/*
 * Challenge: Compute the greatest common divisor of two numbers. 
 */
public class GCDProblem implements MathProblem {
	
	// ranges of the numbers to compute the gcd of. 
	final static int MIN = 100;
	final static int MAX = 5000;
	
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
		
		this.renderedString = "Find the greatest common divisor of $" + number1 + "$ and $" + number2 + "$";
		Debug.d(solution + "");
	}
	
	
	public GCDProblem() {
		
	}
	
}
