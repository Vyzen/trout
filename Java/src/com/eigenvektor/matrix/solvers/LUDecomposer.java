/*
 *  Basic LU decomposition class.
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

import com.eigenvektor.matrix.Matrices;
import com.eigenvektor.matrix.Matrix;
import com.eigenvektor.matrix.MutableMatrix;

/**
 * A class that does an LUP decomposition of a matrix.
 */
final class LUDecomposer
{
	private final MutableMatrix l;
	private final MutableMatrix u;
	private final MutableMatrix p;
	
	/**
	 * Creates a new LUDecomposer that decomposes the given matrix.
	 * 
	 * @param m The matrix to decompose.  The class assumes full control of m and will
	 * modify it.
	 */
	public LUDecomposer(final MutableMatrix m)
	{
		if (m == null) { throw new NullPointerException("m may not be null."); }
		
		if (m.getNCols() != m.getNRows()) { throw new IllegalArgumentException("m must be square."); }
		
		// m will become the upper matrix.
		this.u = m;
		
		// l starts as an identity.
		this.l = Matrices.fullIdentity(m.getNCols());
		
		// p starts as a sparse identity, and stays that way, because I haven't
		// implemented pivoting.
		this.p = Matrices.sparseIdentity(m.getNCols());
		
		// Do the operation.
		decompose();
	}
	
	/**
	 * Performs the decomposition, based on the precondition that this.u is the original matrix,
	 * and l and p are identity matrices.
	 */
	private void decompose()
	{
		for (int j = 0 ; j < u.getNCols() ; ++j)
		{
			clearColumn(j);
		}
	}
	
	/**
	 * Performs a single column of the decomposition, based on the precondition that
	 * all lower columns have already been cleared.
	 * 
	 * @param col the column to use.
	 */
	private void clearColumn(int col)
	{
		// Get the diagonal element.
		double diagElem = u.get(col, col);
		
		// Go through the elements below it.
		for (int row = col + 1 ; row < u.getNRows() ; ++row)
		{
			double elem = u.get(row, col);
			
			// Apply the operation that zeros out that element.
			u.rowOperation(col, row, -elem/diagElem);
			l.set(row, col, elem/diagElem);
		}
	}
	
	/**
	 * Gets the lower triangular matrix.
	 * 
	 * @return The lower triangular matrix.
	 */
	public Matrix getL() { return l; }
	
	/**
	 * Gets the upper triangular matrix.
	 * 
	 * @return The upper triangular matrix.
	 */
	public Matrix getU() { return u; }
	
	/**
	 * Gets the permutation matrix. 
	 * 
	 * @return The permutation matrix.
	 */
	public Matrix getP() { return p; }
	
}
