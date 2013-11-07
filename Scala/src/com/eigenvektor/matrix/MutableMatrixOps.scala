package com.eigenvektor.matrix

/**
 * A wrapper for a matrix that scala-izes the basic operations.
 */
final class MutableMatrixOps(private val mat:MutableMatrix) extends MatrixOps(mat) {
	 def +=(m:Matrix) = mat.inPlaceAdd(m)
	 def -=(m:Matrix) = mat.inPlaceSubtract(m)
	 def *=(d:Double) = mat.inPlaceMultiply(d)
}