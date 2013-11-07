package com.eigenvektor.matrix

/**
 * A wrapper for a matrix that scala-izes the basic operations.
 */
class MatrixOps(private val mat:Matrix) {

  def +(m:Matrix) = mat.add(m)
  def -(m:Matrix) = mat.subtract(m);
  def *(m:Matrix) = mat.multiply(m);
  
}