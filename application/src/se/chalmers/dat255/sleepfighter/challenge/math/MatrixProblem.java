package se.chalmers.dat255.sleepfighter.challenge.math;

import java.util.Random;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

import se.chalmers.dat255.sleepfighter.utils.debug.Debug;
import se.chalmers.dat255.sleepfighter.utils.math.MatrixUtil;

/*
 * Challenge: compute the matrix product of two 3x3 matrices. Then compute the determinant of the product.
 */
public class MatrixProblem implements MathProblem {
	
	
private static final int MAX_INT = 10;
	
	// we use 3x3 matrices.
	private static final int MATRIX_SIZE = 3;
	
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
	
	// for now determinant. 
	
	public int randomSmallInt() {
		// we don't want to big integers. 
		return rng.nextInt(MAX_INT);
	}
	
	public void newProblem() {
		double[][] matrixData = new double[MATRIX_SIZE][MATRIX_SIZE];
		
		for(int i = 0; i < MATRIX_SIZE; ++i) {
			for(int j = 0; j < MATRIX_SIZE; ++j) {
				matrixData[i][j] = this.randomSmallInt();
			}
		}
		
		RealMatrix m1 = MatrixUtils.createRealMatrix(matrixData);
			
		computeSolution(m1);
		doRender(m1);
	}
	
	public void computeSolution(RealMatrix m1) {
		this.solution = (int)MatrixUtil.computeDeterminant(m1);
	}
	
	public void doRender(RealMatrix m1) {
		
		double[][] matrixData = m1.getData();
		
		// begin table
		renderedString = "(\\table ";
		
		for(int i = 0; i < MATRIX_SIZE; ++i) {
			for(int j = 0; j < MATRIX_SIZE; ++j) {
				renderedString += Integer.toString((int) matrixData[i][j]) + " ";
				if(j != MATRIX_SIZE-1) {
					renderedString += " , ";
				}
			}
			
			// no ";" necessary for the last row. 
			if(i != MATRIX_SIZE-1)
				// start new row.  
				renderedString += " ; ";
		}
		
		// finnish table
		renderedString += ")";
		
		Debug.d("renderedString: " + renderedString);
		
	}
	
	public MatrixProblem() {
		
	}
	
}
