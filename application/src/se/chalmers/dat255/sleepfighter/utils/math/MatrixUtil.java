package se.chalmers.dat255.sleepfighter.utils.math;

import org.apache.commons.math3.linear.RealMatrix;

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
}
