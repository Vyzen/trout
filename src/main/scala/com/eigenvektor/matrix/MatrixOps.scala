/*
 *  Scala ops for immutable matrix.
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
class MatrixOps(private val mat:Matrix) {

  /** Add another matrix to this */
  def +(m:Matrix) = mat.add(m)
  
  /** Subtract another matrix from this */
  def -(m:Matrix) = mat.subtract(m);
  
  /** multiply this by another matrix. */
  def *(m:Matrix) = mat.multiply(m);
  
  /** Extract an element of this matrix */
  def apply(j:Int, k:Int) = mat.get(j,k)
  
}