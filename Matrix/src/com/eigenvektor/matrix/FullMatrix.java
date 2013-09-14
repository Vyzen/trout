package com.eigenvektor.matrix;

import java.util.Iterator;

/**
 * An implementation of a matrix that represents its data as one big array.
 */
public final class FullMatrix extends AbstractMatrix implements MutableMatrix
{
	final int nRows;
	final int nCols;
	final double[] data;
	
	/**
	 * Creates a new instance with a specified size full of zeros.
	 * 
	 * @param nRows the number of rows.
	 * @param nCols the number of columns.
	 */
	public FullMatrix(int nRows, int nCols)
	{
		if (nRows < 0 || nCols < 0)
		{
			throw new IllegalArgumentException("nRows and nCols must be non-negative.");
		}
		
		this.nRows = nRows;
		this.nCols = nCols;
		data = new double[nRows * nCols];
	}

	/**
	 * Creates a new instance that copies another matrix.
	 * 
	 * @param m The instance to copy.
	 */
	public FullMatrix(final Matrix m)
	{
		this(m.getNRows(), m.getNCols());
		
		// Copy the data.
		for (int j = 0 ; j < nRows ; j++)
		{
			for (int k = 0 ; k < nCols ; k++)
			{
				data[j*nCols + k] = m.get(j,  k);
			}
		}
	}
	
	@Override
	public int getNRows()
	{
		return nRows;
	}

	@Override
	public int getNCols()
	{
		return nCols;
	}

	@Override
	public double get(int row, int col)
	{
		checkIndices(row, col);
		return data[row*nCols + col];
	}

	@Override
	public Matrix multiply(double scalar)
	{
		// Copy the data and do the multiply in-place on the copy.
		FullMatrix ret = new FullMatrix(this);
		ret.inPlaceMultiply(scalar);
		return ret;
	}

	@Override
	public void set(int row, int col, double val)
	{
		checkIndices(row, col);
		data[row * nCols + col] = val;
	}

	@Override
	public void inPlaceMultiply(double scalar)
	{
		for (int j = 0 ; j < data.length ; ++j)
		{
			data[j] *= scalar;
		}
	}
	
	/**
	 * Checks that indices are valid, and throw if they are not.
	 */
	private void checkIndices(final int row, final int col)
	{
		if (row < 0 || col < 0)
		{
			throw new IllegalArgumentException("row and col must be non-negative.");
		}
		
		if (row >= nRows || col >= nCols)
		{
			throw new IllegalArgumentException("row or col outside of matrix range.");
		}
	}

	@Override
	public Iterator<Element> iterator()
	{
		return new FullMatrixIterator(this);
	}

}
