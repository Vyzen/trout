/*
 *  Scala ops for implicit conversions for matrices.
 *  Copyright (C) 2013 Michael Thorsley
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see [http://www.gnu.org/licenses/].
 */

package com.eigenvektor

/** Implicit conversions form matrix objects.
 * 
 */
package object matrix {
	
	/** Implicit conversion of MutableMatrix to MutableMatrixOps */
	implicit def mutableMatrixToMutableMatrixOps(m:MutableMatrix) = new MutableMatrixOps(m)
  
	/** Implicit conversion of Matrix to MatrixOps */
	implicit def matrixToMatrixOps(m:Matrix) = new MatrixOps(m)
	
	/** Implcit conversion of Double to MatrixAwareDouble */
	implicit def doubleToMatAware(d:Double) = new MatrixAwareDouble(d)
}