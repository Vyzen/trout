package com.eigenvektor.matrix.solvers;

import com.eigenvektor.matrix.MutableMatrix;

/**
 * Specification for a class that applies a matrix operation to a
 * <code>MutableMatrix</code> instance.
 */
public interface MatrixOperation
{

	/**
	 * Applies the operation to a matrix.
	 * 
	 * @param m The matrix to apply to.
	 */
	void apply(final MutableMatrix m);
	
	/**
	 * Applies the inverse of the operation to the matrix.
	 * 
	 * @param m The matrix to apply to.
	 */
	void applyInverse(final MutableMatrix m);
}
