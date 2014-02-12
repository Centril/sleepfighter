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

import org.apache.commons.math3.linear.RealMatrix;

import java.util.Random;

import se.toxbee.sleepfighter.R;
import se.toxbee.sleepfighter.utils.debug.Debug;
import se.toxbee.sleepfighter.utils.math.MatrixUtil;

/*
 * Challenge: compute the matrix product of two 3x3 matrices. Then compute the determinant of the product.
 */
public class MatrixProblem implements MathProblem {
	
	private final Context context;
	
	private static final int MAX_INT = 7;
	private static final int MIN_INT = -3;
	
	// we use 3x3 matrices.
	private static final int MATRIX_SIZE = 3;
	
	private int solution;
	
	private String renderedString;
	
	private Random rng = new Random();
	
	public String render() {
		return this.renderedString;
	}
	
	public int solution() {
		Debug.d("solution is " + solution);
		return this.solution;
	}
	
	RealMatrix createRandomMatrix() {
		return MatrixUtil.createRandomMatrix(rng, MATRIX_SIZE, MIN_INT, MAX_INT, true);
	}
	
	public void newProblem() {
			
		RealMatrix m1 = createRandomMatrix();
		RealMatrix m2 = createRandomMatrix();
		
		computeSolution(m1, m2);
		doRender(m1, m2);
	}
	
	private void computeSolution(RealMatrix m1, RealMatrix m2) {
		RealMatrix product = m1.multiply(m2);
		this.solution = (int)MatrixUtil.computeDeterminant(product);
	}
	
	private void doRender(RealMatrix m1, RealMatrix m2) {
		this.renderedString = "$A =";
		this.renderedString += renderMatrix(m1);
		this.renderedString += renderMatrix(m2);
		this.renderedString += "$";
		String format = context.getResources().getString(R.string.matrix_challenge_desc);
		this.renderedString += "<br>" + String.format(format, "$A$");
	
	}
	
	private String renderMatrix(RealMatrix m) {
		
		double[][] matrixData = m.getData();
		
		
		// begin table
		String str = "(\\table ";
		
		for(int i = 0; i < MATRIX_SIZE; ++i) {
			for(int j = 0; j < MATRIX_SIZE; ++j) {
				str += Integer.toString((int) matrixData[i][j]) + " ";
				if(j != MATRIX_SIZE-1) {
					str += " , ";
				}
			}
			
			// no ";" necessary for the last row. 
			if(i != MATRIX_SIZE-1)
				// start new row.  
				str += " ; ";
		}
		
		// finish table
		str += ")";
		
		return str;		
	}
	
	public MatrixProblem(final Context context) {
		this.context = context;
	}
	
}
