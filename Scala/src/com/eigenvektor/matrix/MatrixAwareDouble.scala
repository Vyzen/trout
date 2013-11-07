/*
 *  A Double that is aware of matrices so we can write things like 5 * M without pain
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

/**
 * A simple double-wrapping class that can be multiplied on the right
 * by a matrix without complaining.
 */
final class MatrixAwareDouble(private val d:Double) {
  
	// Standard multiply.
	def *(m:Matrix) = m.multiply(d)
}