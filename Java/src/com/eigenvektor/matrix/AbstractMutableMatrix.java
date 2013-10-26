package com.eigenvektor.matrix;

/**
 * An abstract implementation for <code>MutableMatrix</code> that defines the row
 * and column operations in terms of the atomic get and set operations.  If an
 * subclass can provide a faster implementation of any of these, it should override.
 * 
 * Also provides default implementations of <code>inPlaceAdd()</code> and 
 * </code>inPlaceSubtract()</code>.
 */
public abstract class AbstractMutableMatrix extends AbstractMatrix implements
		MutableMatrix
{

	@Override
	public void inPlaceAdd(final Matrix m)
	{
		if (m == null) { throw new NullPointerException("m may not be null."); }
		
		if (m.getNRows() != this.getNRows() || m.getNCols() != this.getNCols())
		{
			throw new IllegalArgumentException("m not compatable for addition.");
		}
		
		for (Matrix.Element e : m)
		{
			final int row = e.getRow();
			final int col = e.getCol();
			this.set(row, col, this.get(row, col) + e.getValue());
		}
	}
	
	
	@Override
	public void inPlaceSubtract(final Matrix m)
	{
		if (m == null) { throw new NullPointerException("m may not be null."); }
		
		if (m.getNRows() != this.getNRows() || m.getNCols() != this.getNCols())
		{
			throw new IllegalArgumentException("m not compatable for subtraction.");
		}
		
		for (Matrix.Element e : m)
		{
			final int row = e.getRow();
			final int col = e.getCol();
			this.set(row, col, this.get(row, col) - e.getValue());
		}
	}
	
	
	@Override
	public void rowOperation(int from, int to, double c)
	{
		int nCols = this.getNCols();
		for (int j = 0 ; j < nCols ; ++j)
		{
			this.set(to, j, this.get(to, j) + this.get(from, j) * c);
		}
	}

	@Override
	public void swapRows(int from, int to)
	{
		int nCols = this.getNCols();
		for (int j = 0 ; j < nCols ; ++j)
		{
			double x = this.get(to, j);
			this.set(to, j, this.get(from, j));
			this.set(from, j, x);
		}
	}

	@Override
	public void scaleRow(int row, double c)
	{
		int nCols = this.getNCols();
		for (int j = 0 ; j < nCols ; ++j)
		{
			this.set(row, j, this.get(row, j) * c);
		}
	}

	@Override
	public void columnOperation(int from, int to, double c)
	{
		int nRows = this.getNCols();
		for (int j = 0 ; j < nRows ; ++j)
		{
			this.set(j, to, this.get(j, to) + this.get(j, from) * c);
		}
	}

	@Override
	public void swapColumns(int from, int to)
	{
		int nRows = this.getNRows();
		for (int j = 0 ; j < nRows ; ++j)
		{
			double x = this.get(j, to);
			this.set(j, to, this.get(j, from));
			this.set(j, from, x);
		}

	}

	@Override
	public void scaleColumn(int col, double c)
	{
		int nRows = this.getNRows();
		for (int j = 0 ; j < nRows ; ++j)
		{
			this.set(j, col, this.get(j, col) * c);
		}
	}

}
