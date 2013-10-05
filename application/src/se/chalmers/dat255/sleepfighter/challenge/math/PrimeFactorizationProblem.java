package se.chalmers.dat255.sleepfighter.challenge.math;

import java.util.Random;

import se.chalmers.dat255.sleepfighter.utils.debug.Debug;
import se.chalmers.dat255.sleepfighter.utils.math.RandomMath;

/*
 * Challenge: Compute the largest prime factor of a number. 
 */
public class PrimeFactorizationProblem implements MathProblem {
	
	// we'll use these primes in the creation of the number.
	private static final int[] PRIMES = new int[]{2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59};

	
	private int solution;
	
	String renderedString;
	
	private Random rng = new Random();

	public String render() {
		return this.renderedString;
	}
	
	public int solution() {
		Debug.d("solution is " + solution);
		return this.solution;
	}
	
	// create the number to be prime factorized by the user.
	private int createNumber() {
		int n = 1;
		int primeFactors = RandomMath.nextRandomRanged(rng, 2, 3); 
		this.solution = 1;
		
		for(int i = 0; i < primeFactors; ++i) {
			int randomPrime = PRIMES[rng.nextInt(PRIMES.length)];
			n *= randomPrime;
			
			// we'll keep track of the larget prime factor(this is the solution to this problem
			if(randomPrime > solution) {
				this.solution = randomPrime;
			}
			
		}
		
		return n;
	}
	
	public void newProblem() {
		
		
		
		int n = createNumber();
		
		this.renderedString = "Find the largest prime factor of $" + n + "$";
		Debug.d(solution + "");
		
	}
	
	
	public PrimeFactorizationProblem() {
		
	}
	
}
