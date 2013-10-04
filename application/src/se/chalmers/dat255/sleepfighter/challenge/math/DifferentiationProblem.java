package se.chalmers.dat255.sleepfighter.challenge.math;

import java.util.Random;

import se.chalmers.dat255.sleepfighter.utils.debug.Debug;

/*
 * Challenge: compute the value of a derivative. 
 */
public class DifferentiationProblem implements MathProblem {
	
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

	
	public void newProblem() {
			
	}
	
	public DifferentiationProblem() {
		
	}
	
}
