/*
 *  Interface for a matrix solver.
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

package com.eigenvektor.matrix.solvers;

import com.eigenvektor.matrix.Matrix;

/**
 * Specification for classes that solve matrix equations of the form
 * Ax = b for x given b.  Typical implementations will take A in the
 * constructor and be able to solve for various bs.
 */
public interface MatrixSolver
{

	/**
	 * Solves for a given right hand side.
	 * 
	 * @param b  The right hand side to solve for.  Must have as many rows
	 * as there are in the original "A" matrix has columns.
	 * @return A matrix x, such that A.multiply(x) = b
	 */
	public Matrix solve(Matrix b);
	
}
