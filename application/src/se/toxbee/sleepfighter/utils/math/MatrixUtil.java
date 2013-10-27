package se.toxbee.sleepfighter.utils.math;

import java.util.Random;

import org.apache.commons.math3.linear.ArrayRealVector;
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
	
	private static int nextRandomRanged(Random rng, final int MIN_INT, final int MAX_INT, boolean noZeros) {
		return (noZeros ?
				RandomMath.nextRandomRanged(rng, MIN_INT, MAX_INT, 0) : 
			RandomMath.nextRandomRanged(rng, MIN_INT, MAX_INT));
	}
	
	// create a random, invertible(It is very important that it is invertible, because otherwise the system of equations is unsolvable!)
	//, quadratic matrix, with random integers. MIN_INT and MAX_INT is the range of these random integers. 
	// if noZeros is true, then there will be no elements with value zero. 
	public static RealMatrix createRandomMatrix(final Random rng, final int size, final int MIN_INT, final int MAX_INT,
			boolean noZeros) {
		
		RealMatrix m;
		
		do {

			double[][] matrixData = new double[size][size];

			for(int i = 0; i < size; ++i) {
				for(int j = 0; j < size; ++j) {

					matrixData[i][j] = MatrixUtil.nextRandomRanged(rng, MIN_INT, MAX_INT, noZeros);
				}
			}
			
			m =  MatrixUtils.createRealMatrix(matrixData);

		} while(isSingular(m));

		return m;
	}
	
	// A singular matrix is invertible.
	public static boolean isSingular(RealMatrix m) {
		return computeDeterminant(m) == 0;
	}
	
	// Create a random 3d vector
	public static RealVector createRandomVector(final Random rng, final int size, final int MIN_INT, final int MAX_INT,
			boolean noZeros) {
		return new ArrayRealVector(new double[]{
				MatrixUtil.nextRandomRanged(rng, MIN_INT, MAX_INT, noZeros),
				MatrixUtil.nextRandomRanged(rng, MIN_INT, MAX_INT, noZeros),
				MatrixUtil.nextRandomRanged(rng, MIN_INT, MAX_INT, noZeros)
		});
	}
}
