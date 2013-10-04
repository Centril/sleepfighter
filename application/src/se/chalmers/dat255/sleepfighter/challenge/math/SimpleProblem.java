package se.chalmers.dat255.sleepfighter.challenge.math;

import java.util.Random;

public class SimpleProblem implements MathProblem {

	private int operand1 = 0;
	private int operand2 = 0;
	private int operation = 0;
	private int result = 0;

	private Random random = new Random();

	public SimpleProblem() {
	}

	public String render() {
		String rendered = "$";
		if (operation == 0) {
			rendered += operand1 + " + " + operand2;
		} else if (operation == 1) {
			rendered += operand1 + " - " + operand2;
		} else if (operation == 2) {
			rendered += operand1 + " * " + operand2;
		} else {
			rendered += operand1 + " / " + operand2;
		}
		
		rendered += "$";
		return rendered;
	}

	public int solution() {
		return result;
	}

	/**
	 * The random interval of the operands 
	 * case 0: addition 
	 * case 1: subtraction
	 * case 2: multiplication 
	 * case 3: division
	 */
	private void nextInts() {
		switch (operation) {
		case 0:
			operand1 = random.nextInt(99) + 1;
			operand2 = random.nextInt(99) + 1;
			result = operand1 + operand2;
			break;
		case 1:
			operand1 = random.nextInt(99) + 1;
			operand2 = random.nextInt(99) + 1;
			result = operand1 - operand2;
			break;
		case 2:
			operand1 = random.nextInt(8) + 2;
			operand2 = random.nextInt(8) + 2;
			result = operand1 * operand2;
			break;
		case 3:
			result = random.nextInt(8) + 2;
			operand2 = random.nextInt(8) + 2;
			operand1 = result * operand2;
			break;
		}
	}

	// What the next operation will be, add/sub/mul/div
	private void nextOp() {
		operation = random.nextInt(4);
	}

	@Override
	public void newProblem() {
		nextOp();
		nextInts();
	}

}
