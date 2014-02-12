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
import android.graphics.Color;

import java.util.Random;

import se.toxbee.sleepfighter.R;

/**
 * Challenge: The user solves an simple arithmetic problem, such "3+2", "5*2", "6/2", and so on.
 * 
 */
public class SimpleProblem implements MathProblem {

	private int operand1 = 0;
	private int operand2 = 0;
	private int operation = 0;
	private int result = 0;
	private Context context;

	private Random random = new Random();

	public SimpleProblem(Context context) {
		this.context = context;
	}
	
	// color a string in html
	private String colorStr(String str, String color) {
		return "<span style='color: #"+color+";'>" + str + "</span>";
	}
	
	// convert to hex with padding
	private String toHex(int n) {
		String s = Integer.toHexString(n);
		if(s.length() == 1) {
			return "0" + s;
		} else {
			return s;
		}
	}
	
	// numbers are colored holo blue.
	private String formatNumber(int n) {
	
		int c = context.getResources().getColor(R.color.holo_blue_bright);
		String colorStr = toHex(Color.red(c))  + toHex(Color.green(c)) +
				toHex(Color.blue(c)); 
		
		return colorStr(Integer.toString(n), colorStr);
	}
	
	// operators are white
	private String formatOperator(String op) {
		return colorStr(op, "ffffff");
	}

	public String render() {
		String rendered = "";
		
		String operand1s = formatNumber(this.operand1);
		String operand2s = formatNumber(this.operand2);
		
		if (operation == 0) {
			rendered += operand1s + formatOperator(" + ") + operand2s;
		} else if (operation == 1) {
			rendered += operand1s + formatOperator(" - ") + operand2s;
		} else if (operation == 2) {
			rendered += operand1s + formatOperator(" * ") + operand2s;
		} else {
			rendered += operand1s +formatOperator(" / ") + operand2s;
		}
		
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
		default:
			throw new IllegalArgumentException("This should not happen");
				
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
