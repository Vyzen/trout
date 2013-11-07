package com.eigenvektor

package object matrix {
  
	// Implicit conversion of Matrix to MatrixOps
	implicit def matrixToMatrixOps(m:Matrix) = new MatrixOps(m)
	
	// Implict conversion of MutableMatrix to MutableMatrixOps
	implicit def mutableMatrixToMutableMatrixOps(m:MutableMatrix) = new MutableMatrixOps(m)
}