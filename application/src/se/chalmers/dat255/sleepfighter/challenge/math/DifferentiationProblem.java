package se.chalmers.dat255.sleepfighter.challenge.math;

import java.util.Random;

import org.apache.commons.math3.analysis.differentiation.DerivativeStructure;

import se.chalmers.dat255.sleepfighter.utils.debug.Debug;
import se.chalmers.dat255.sleepfighter.utils.math.RandomMath;

/*
 * Challenge: compute the value of a derivative. 
 */
public class DifferentiationProblem implements MathProblem {
	
	// See the documentation of DerivativeStructure for details. 
	private static final int PARAMETERS = 1;
	private static final int INDEX = 0;
	
	// so we only ask for second derivatives. 
	private static final int ORDER = 1;
	
	
	private int solution;
	
	String renderedString;
	
	// the value for which to calculate to derivative for. So the solution becomes f'(x)
	private int x;
	
	private Random rng = new Random();
	
	// Used for storing a function and its string representatin.
	private class Function {
		
		public Function() {
			s = "";
			f = getZero();
		}
		
		public void addTerm(DerivativeStructure term, String termStr) {
			this.f = this.f.add(term);
			this.s = this.s + " + " + termStr;
		}
		
		
		public DerivativeStructure f;
		
		// the string represenation of f.
		public String s;
	}
	
	public String render() {
		return this.renderedString;
	}
	
	public int solution() {
		Debug.d("solution is " + solution);
		return this.solution;
	}
	
	public void newProblem() {
		
		this.x = 1; //RandomMath.nextRandomRanged(rng, -10, 10);
		Function f = randomPolynomial();
		this.solution = (int)f.f.getPartialDerivative(1);
		this.renderedString = "$f(x) = " + f.s + "$";
		this.renderedString += "<br>Find $f'(" + this.x + ")$";
	}
	
	// The identity function f(x) = x
	private DerivativeStructure getIdentity() {
		return new DerivativeStructure(PARAMETERS, ORDER, INDEX, this.x);
	}
	
	// A DerivativeStructure with value 0
	private static DerivativeStructure getZero() {
		return new DerivativeStructure(PARAMETERS, ORDER, 0);
	}
	
	private Function randomPolynomial() {
		Function ret = new Function();

		final int numberTerms = rng.nextInt(6);
		
		// generate an expression on the form ax^c
		for(int term = 0; term < numberTerms; ++term) {
			int c = rng.nextInt(5);
			int a = rng.nextInt(5);	
			
			// form the term
			DerivativeStructure termExpr = getIdentity().pow(c).multiply(a);
			String termStr = a + "x^" + c;
			
			ret.addTerm(termExpr, termStr);
		}
		
		return ret;
	}
	
	
	public DifferentiationProblem() {
		
	}
	
}
