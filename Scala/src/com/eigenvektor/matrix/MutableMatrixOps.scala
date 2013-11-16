/*
 *  Scala ops for mutable matrix.
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

package com.eigenvektor.matrix

/** A wrapper for a matrix that scala-izes the basic operations. */
final class MutableMatrixOps(private val mat:MutableMatrix) extends MatrixOps(mat) {
  
  /** Override + to specialize return type. */
  override def +(m:Matrix) = mat.add(m).asInstanceOf[MutableMatrix]
  
  /** Override - to specialize return type. */
  override def -(m:Matrix) = mat.subtract(m).asInstanceOf[MutableMatrix]
  
  /** Override * to specialize return type. */
  override def *(m:Matrix) = mat.multiply(m).asInstanceOf[MutableMatrix]
  
  /** In place addition */
  def +=(m:Matrix) = mat.inPlaceAdd(m)
  
  /** In place subtraction */
  def -=(m:Matrix) = mat.inPlaceSubtract(m)
  
  /** In place multiplication by scalar */
  def *=(d:Double) = mat.inPlaceMultiply(d)
  
  /** Update a single element of the matrix */
  def update(i:Int, j:Int, d:Double) = mat.set(i, j, d);
}