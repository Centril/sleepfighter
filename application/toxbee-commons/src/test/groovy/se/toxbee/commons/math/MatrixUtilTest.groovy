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

package se.toxbee.commons.math

import org.apache.commons.math3.linear.Array2DRowRealMatrix
import org.apache.commons.math3.linear.MatrixUtils
import org.apache.commons.math3.linear.RealMatrix
import spock.lang.Specification

/**
 *
 * @author Centril < twingoow @ gmail.com >  / Mazdak Farrokhzad.
 * @version 1.0
 * @since Feb , 19, 2014
 */
class MatrixUtilTest extends Specification {
	def "ComputeDeterminant"() {
		given:
			double[][] matrixData = new double[3][3]
			matrixData[0][0] = -2
			matrixData[0][1] = 2
			matrixData[0][2] = 3

			matrixData[1][0] = -1
			matrixData[1][1] = 1
			matrixData[1][2] = 3

			matrixData[2][0] = 2
			matrixData[2][1] = 0
			matrixData[2][2] = -1

			RealMatrix m1 = MatrixUtils.createRealMatrix( matrixData );
		expect:
			6 == (int) MatrixUtil.computeDeterminant( m1 )
	}

	def "IsSingular"() {
		given:
			double[][] data = new double[3][3];
			data[0][0] = 1
			data[0][1] = 0
			data[0][2] = 0
			data[1][0] = -2
			data[1][1] = 0
			data[1][2] = 0
			data[2][0] = 4
			data[2][1] = 6
			data[2][2] = 1
			RealMatrix m = new Array2DRowRealMatrix( data )
		expect:
			MatrixUtil.isSingular( m )
	}

	def "CreateRandomVector"() {

	}

	def "CreateRandomMatrix"() {

	}
}
