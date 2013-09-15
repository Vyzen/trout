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
