package se.chalmers.dat255.sleepfighter.utils.math;

import java.util.Random;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

public class MatrixUtil {
	
	private MatrixUtil() {
		
	}
	
	// compute the determinant of a 3x3 matrix. 
	public static double computeDeterminant(RealMatrix m) {
		
		double[][] data = m.getData();
		
		double a = data[0][0];
		double b = data[0][1];
		double c = data[0][2];
		
		double d = data[1][0];
		double e = data[1][1];
		double f = data[1][2];
		
		double g = data[2][0];
		double h = data[2][1];
		double i = data[2][2];
		
		return a*e*i+b*f*g+c*d*h-c*e*g-b*d*i-a*f*h;
	}
	
	// create a random quadratic matrix, with random integers. MIN_INT and MAX_INT is the range of these random integers. 
	public static RealMatrix createRandomMatrix(final Random rng, final int size, final int MIN_INT, final int MAX_INT) {

		double[][] matrixData = new double[size][size];
		
		for(int i = 0; i < size; ++i) {
			for(int j = 0; j < size; ++j) {
				matrixData[i][j] =   RandomMath.nextRandomRanged(rng, MIN_INT, MAX_INT);
			}
		}
		
		return MatrixUtils.createRealMatrix(matrixData);
	}
	
	// Create a random 3d vector
	public static RealVector createRandomVector(final Random rng, final int size, final int MIN_INT, final int MAX_INT) {
		return new ArrayRealVector(new double[]{
				RandomMath.nextRandomRanged(rng, MIN_INT, MAX_INT),
				RandomMath.nextRandomRanged(rng, MIN_INT, MAX_INT),
				RandomMath.nextRandomRanged(rng, MIN_INT, MAX_INT)
		});
	}
	
	public static RealVector solveLinearEquationSystem(final RealMatrix coefficients, RealVector constants) {
		return solveLinearEquationSystem(new LUDecomposition(coefficients).getSolver(), constants);
	}
	
	public static RealVector solveLinearEquationSystem(DecompositionSolver solver, RealVector constants) {
		return solver.solve(constants);
	}
}
