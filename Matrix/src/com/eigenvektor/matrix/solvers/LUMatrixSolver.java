package com.eigenvektor.matrix.solvers;

import com.eigenvektor.matrix.FullMatrix;
import com.eigenvektor.matrix.Matrix;
import com.eigenvektor.matrix.MutableMatrix;

public final class LUMatrixSolver implements MatrixSolver
{
	
	private LUDecomposer lu;
	
	/**
	 * Creates a new solver.
	 * 
	 * @param m The matrix to do the solving with.
	 */
	public LUMatrixSolver(final Matrix m)
	{
		if (m == null) { throw new NullPointerException("m may not be null."); }
		
		lu = new LUDecomposer(new FullMatrix(m));
	}

	@Override
	public Matrix solve(Matrix b)
	{
		if (b == null)
		{
			throw new NullPointerException("b may not be null.");
		}
		
		// Check the size of b.
		if (b.getNRows() != lu.getL().getNCols())
		{
			throw new IllegalArgumentException("Incompatible size for b.");
		}
		
		// Create an output.
		MutableMatrix ret = new FullMatrix(b.getNRows(), b.getNCols());
		
		// Do each column separately.
		for (int j = 0 ; j < b.getNCols() ; ++j)
		{
			double[] y = solveLYEqB(lu.getL(), b, j);
			solveUXEqY(lu.getU(), y, ret, j);
		}
		
		return ret;
	}

	/**
	 * Solves the equation U*x = y and places it into the column of a given matrix.
	 * 
	 * @param u The upper-triangular matrix.
	 * @param y the y vector.
	 * @param ret The output matrix.
	 * @param j The column of the output matrix to fill.
	 */
	private void solveUXEqY(final Matrix u, final double[] y, final MutableMatrix ret, final int col)
	{
		final int nRows = y.length;
		final int nCols = u.getNCols(); // Normally the same as nRows.
		
		// Iterate backwards through the rows.
		for (int j = nRows - 1 ; j >= 0 ; j--)
		{
			double val = y[j];
			for (int k = j + 1 ; k < nCols ; ++k)
			{
				val -= u.get(j, k) * ret.get(k, col);
			}
			val /= u.get(j, j);
			ret.set(j, col, val);
		}
	}

	/**
	 * Solves L*y = b for b taken as the column of a given matrix.
	 * 
	 * @param l The lower-triangular matrix.
	 * @param b the matrix containing the target column.
	 * @param col the index of the target column.
	 * @return The values of y.
	 */
	private double[] solveLYEqB(final Matrix l, final Matrix b, final int col)
	{
		final int nRows = b.getNRows();
		double[] ret = new double[nRows];
		
		for (int j = 0 ; j < nRows ; ++j)
		{
			double val = b.get(j, col);
			for (int k = 0 ; k < j ; ++k)
			{
				val -= ret[k]*l.get(j, k);
			}
			ret[j] = val;
		}
		return ret;
	}

}
