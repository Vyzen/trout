package com.eigenvektor.matrix;

import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import java.util.NoSuchElementException;

/**
 * A matrix backed by a sparse representation.
 */
public final class SparseMatrix extends AbstractMatrix implements MutableMatrix
{

	private final int nRows;
	private final int nCols;
	
	// A map from row to column to value for the non-zero values.
	private final Map<Integer, Map<Integer, Double>> values = new HashMap<Integer, Map<Integer, Double>>();
	
	/**
	 * Creates a new, empty instance.
	 * 
	 * @param nRows The number of rows.
	 * @param nCols The number of columns.
	 */
	public SparseMatrix(final int nRows, final int nCols)
	{
		if (nRows < 0 || nCols < 0)
		{
			throw new IllegalArgumentException("nRows and nCols must be non-negative.");
		}
		
		this.nRows = nRows;
		this.nCols = nCols;
	}
	
	/**
	 * Copies another matrix.
	 * @param m The matrix to copy.
	 */
	public SparseMatrix(final Matrix m)
	{
		this(m.getNRows(), m.getNCols());
		
		// If m is a sparse matrix, just copy its internal state.
		if (m instanceof SparseMatrix)
		{
			copyInternalState((SparseMatrix) m);
		}
		else
		{
			for (int j = 0 ; j < m.getNRows() ; ++j)
			{
				for (int k = 0 ; k < m.getNCols() ; ++k)
				{
					double val = m.get(j, k);
					if (val != 0.0)
					{
						this.set(j, k, val);
					}
				}
			}
		}
	}
	
	/**
	 * Copies the internal state of another sparse matrix into this.
	 * 
	 * @param s the other sparse matrix.
	 */
	private void copyInternalState(final SparseMatrix s)
	{
		values.clear();
		
		for (Map.Entry<Integer, Map<Integer, Double>> e : s.values.entrySet())
		{
			// Copy the row maps.
			Map<Integer, Double> rowMap = new HashMap<Integer, Double>(e.getValue());
			this.values.put(e.getKey(), rowMap);
		}
	}
	
	@Override
	public int getNRows()
	{
		return this.nRows;
	}

	@Override
	public int getNCols()
	{
		return this.nCols;
	}

	@Override
	public double get(int row, int col)
	{
		checkIndices(row, col);
		
		//Search first by row, and then by column.
		if (values.containsKey(row))
		{
			Map<Integer, Double> rowMap = values.get(row);
			if (rowMap.containsKey(col))
			{
				return rowMap.get(col);
			}
			else
			{
				return 0.0;
			}
		}
		else
		{
			return 0.0;
		}
	}

	@Override
	public Matrix multiply(double scalar)
	{
		// Copy and then apply in place.
		SparseMatrix ret = new SparseMatrix(this);
		ret.inPlaceMultiply(scalar);
		return ret;
	}

	@Override
	public Matrix multiply(Matrix m)
	{
		if (m instanceof SparseMatrix)
		{
			return sparseMultiply((SparseMatrix) m);
		}
		else
		{
			return super.multiply(m);
		}
	}

	private Matrix sparseMultiply(SparseMatrix s)
	{
		// TODO implement a quicker sparse matrix multiplication algorithm.
		return super.multiply(s);
	}
	

	@Override
	public void set(int row, int col, double val)
	{
		checkIndices(row, col);
		
		// If there already is a row map for this row, use it.  Otherwise create one.
		Map<Integer, Double> rowMap = null;
		if (values.containsKey(row))
		{
			rowMap = values.get(row);
		}
		else
		{
			rowMap = new HashMap<Integer, Double>();
			values.put(row, rowMap);
		}

		// Insert the value.
		rowMap.put(col, val);
	}

	@Override
	public void inPlaceMultiply(double scalar)
	{
		// Go through all of the maps.
		for (Map<Integer, Double> rowMap : values.values())
		{
			for (Map.Entry<Integer, Double> e : rowMap.entrySet())
			{
				e.setValue(e.getValue() * scalar);
			}
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
		return new SparseMatrixIterator();
	}
	
	/**
	 * An iterator through just the represented entries of the matrix.
	 */
	private class SparseMatrixIterator implements Iterator<Element>
	{
		private Iterator<Map.Entry<Integer, Map<Integer, Double>>> rowIter;
		private Iterator<Map.Entry<Integer, Double>> colIter;
		
		private int curRow;
		private int curCol;
		private double curValue;
		
		private boolean done = false;

		public SparseMatrixIterator()
		{
			// Get an iterator through the rows.
			rowIter = values.entrySet().iterator();
			
			// If it's empty, we're done here.
			if (!rowIter.hasNext()) 
			{ 
				done = true;
				return;
			}
			
			// Set to the first column.
			Map.Entry<Integer, Map<Integer, Double>> first = rowIter.next();
			curRow = first.getKey();
			colIter = first.getValue().entrySet().iterator();
			
			// Get the first column.
			Map.Entry<Integer, Double> firstCol = colIter.next();
			curCol = firstCol.getKey();
			curValue = firstCol.getValue();
		}

		@Override
		public boolean hasNext()
		{
			return !done;
		}

		@Override
		public Element next()
		{
			if (done) { throw new NoSuchElementException("Iterator is done."); }
			
			DefaultMatrixElement ret = new DefaultMatrixElement(curRow, curCol, curValue);
			
			if (colIter.hasNext())
			{
				// If we're not at the end of a row, advance the column.
				Map.Entry<Integer, Double> firstCol = colIter.next();
				curCol = firstCol.getKey();
				curValue = firstCol.getValue();
			}
			else if (rowIter.hasNext())
			{
				// If we are at the end of the column, but there are more rows, advance the row...
				Map.Entry<Integer, Map<Integer, Double>> nextRow = rowIter.next();
				curRow = nextRow.getKey();
				colIter = nextRow.getValue().entrySet().iterator();
				
				/// ... and then reset the column.
				Map.Entry<Integer, Double> firstCol = colIter.next();
				curCol = firstCol.getKey();
				curValue = firstCol.getValue();
			}
			else
			{
				// No more columns, no more rows, we're done.
				done = true;
			}
			
			return ret;
		}

		@Override
		public void remove()
		{
			throw new UnsupportedOperationException("Remove not supported through this iterator.");
		}
		
	}

}
