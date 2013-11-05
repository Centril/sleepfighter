package se.toxbee.sleepfighter.utils.math;

import java.util.Random;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

public class MatrixUtil {
	
	private MatrixUtil() {
		
	}

	/**
	 * Computes the determinant of a quadratic RealMatrix m.
	 *
	 * @param m the matrix.
	 * @return the determinant.
	 */
	public static double computeDeterminant(RealMatrix m) {
		return new LUDecomposition( m ).getDeterminant();
	}

	/**
	 * Creates a random, invertible (see below) and quadratic matrix with random integers in the interval [min, max].
	 * And optionally, if noZeros is true, the 0 is not allowed as a value.
	 *
	 * It is very important that it is invertible, because otherwise the system of equations is unsolvable!
	 *
	 * @param rng the RNG.
	 * @param size the size of matrix side (n-cells = size^2)
	 * @param min minimum value.
	 * @param max maximum value.
	 * @param noZeros allow zeros?
	 * @return the generated matrix.
	 */
	public static RealMatrix createRandomMatrix(final Random rng, final int size, final int min, final int max, boolean noZeros) {
		RealMatrix m;
		
		do {
			double[][] matrixData = new double[size][size];

			for( int i = 0; i < size; ++i ) {
				for( int j = 0; j < size; ++j ) {
					matrixData[i][j] = RandomMath.nextRandomRanged( rng, min, max, noZeros );
				}
			}

			m =  MatrixUtils.createRealMatrix( matrixData );
		} while( isSingular( m ) );

		return m;
	}

	// A singular matrix is invertible.
	public static boolean isSingular(RealMatrix m) {
		return computeDeterminant(m) == 0;
	}

	/**
	 * Creates a random vector of size using rng with random values in range: [min, max].
	 *
	 * @param rng the RNG.
	 * @param size the size of vector.
	 * @param min the minimum value.
	 * @param max the maximum value.
	 * @param noZeros allow zeros?
	 * @return the random RealVector.
	 */
	public static RealVector createRandomVector( final Random rng, final int size, final int min, final int max, boolean noZeros ) {
		double[] val = new double[size];
		for ( int i = 0; i < size; ++i ) {
			val[i] = RandomMath.nextRandomRanged( rng, min, max, noZeros );
		}

		return new ArrayRealVector( val );
	}

	/**
	 * Component-multiplies the row of a RealMatrix by factor.
	 *
	 * @param mat the matrix.
	 * @param row the row to multiply.
	 * @param factor the factor.
	 */
	public static void multiplyRow( RealMatrix mat, int row, double factor ) {
		mat.setRowVector( row, mat.getRowVector( row ).mapMultiplyToSelf( factor ) );
	}

	/**
	 * Adds to the row of a RealMatrix from a different row.
	 *
	 * @param mat the matrix.
	 * @param row the row to add to.
	 * @param from the row to to use for values.
	 */
	public static void addToRow( RealMatrix mat, int row, int from ) {
		for ( int col = 0; col < mat.getColumnDimension(); ++col ) {
			mat.addToEntry( row, col, mat.getEntry( from, col ) );
		}
	}

	/**
	 * Swaps the values of row a with row b.
	 *
	 * @param mat the matrix.
	 * @param a row a.
	 * @param b row b.
	 */
	public static void swapRows( RealMatrix mat, int a, int b ) {
		double[] temp = mat.getRow( a );
		mat.setRow( a, mat.getRow( b ) );
		mat.setRow( b, temp );
	}

	/**
	 * Gaussian eliminates a RealMatrix mat.
	 *
	 * @param mat the matrix.
	 */
	public static void gaussianEliminate( RealMatrix mat ) {
		// m => totalRows
		int totalRows = mat.getRowDimension();

		if ( totalRows == 0 ) {
			return;
		}

		// n => totalColumns
		int totalColumns = mat.getColumnDimension();

		// i => row
		int row = 0;
		// j => col
		int col = 0;
		while ( (row < totalRows) && (col < totalColumns) ) {
			int max_row = row;
			// k => currentRow
			for ( int currentRow = row + 1; currentRow < totalRows; ++currentRow ) {
				if ( Math.abs( mat.getEntry( currentRow, col ) ) > Math.abs( mat.getEntry( max_row, col ) ) ) {
					max_row = currentRow;
				}
			}

			if ( mat.getEntry( max_row, col ) != 0.0 ) {
				// Swap the rows around.
				if ( row != max_row ) {
					swapRows( mat, row, max_row );
				}

				multiplyRow( mat, row, 1.0 / mat.getEntry( row, col ) );

				// u => iterRow
				for ( int iterRow = row + 1; iterRow < totalRows; ++iterRow ) {
					double mulVal = -mat.getEntry( iterRow, col );
					if ( mulVal != 0.0 ) {
						multiplyRow( mat, row, mulVal );
						addToRow( mat, iterRow, row );
						multiplyRow( mat, row, 1.0 / mulVal );
					}
				}

				++row;
			}

			++col;
		}
	}
}
