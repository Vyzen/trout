/*
 *  Implementation of a sparse matrix.
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

package com.eigenvektor.matrix;

import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import java.util.NoSuchElementException;

/**
 * A matrix backed by a sparse representation.
 */
public final class SparseMatrix extends AbstractMutableMatrix 
{

	private final int nRows;
	private final int nCols;
	
	// A map from row to column to value for the non-zero values.
	private final Map<Integer, Map<Integer, Double>> values = new HashMap<>();
	
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
			Map<Integer, Double> rowMap = new HashMap<>(e.getValue());
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

	/**
	 * Separate multiplication algorithm solely for sparse matrcies
	 * 
	 * @param s A sparse matrix.
	 * @return The product of this and s.
	 */
	private Matrix sparseMultiply(SparseMatrix s)
	{
		// Check that the matrix is compatible for multiply.
		if (s.getNRows() != this.getNCols())
		{
			throw new IllegalArgumentException("Argument not compatable for matrix multiply.");
		}

		SparseMatrix ret = new SparseMatrix(nRows, s.nCols);
		
		for (Map.Entry<Integer, Map<Integer, Double>> rowMap : values.entrySet())
		{
			int row = rowMap.getKey();
			for (Map.Entry<Integer, Double> colMap : rowMap.getValue().entrySet())
			{
				int col = colMap.getKey();
				double val = colMap.getValue();
				
				for (int j = 0 ; j < s.nCols ; ++j)
				{
					ret.addToElement(row, j, val * s.get(col, j));
				}
			}
		}
		
		return ret;
	}
	
	/**
	 * Adds to a single element.
	 * 
	 * @param row The row of the element.
	 * @param col The column of the element.
	 * @param val The amount to add to the value.
	 */
	private void addToElement(int row, int col, double val)
	{
		if (val == 0.0) { return; }
		
		Map<Integer, Double> rowMap = null;
		if (values.containsKey(row))
		{
			rowMap = values.get(row);
			if (rowMap.containsKey(col))
			{
				rowMap.put(col, rowMap.get(col) + val);
			}
			else
			{
				rowMap.put(col, val);
			}
		} 
		else
		{
			rowMap = new HashMap<Integer, Double>();
			rowMap.put(col, val);
			values.put(row, rowMap);
		}
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

	@Override
	public void rowOperation(int from, int to, double c)
	{
		if (from >= nRows || to >= nRows || from < 0 || to < 0)
		{
			throw new IllegalArgumentException("Invalid row.");
		}
		
		// If the coefficient is zero, there is nothing to do.
		if (c == 0) { return; }
		
		// If the from row doesn't exist, do nothing.
		if (!values.containsKey(from)) { return; }
		
		// If the "to" row doesn't exist, copy the "from" row and scale.
		if (!values.containsKey(to))
		{
			Map<Integer, Double> newRow = new HashMap<>(values.get(from));
			values.put(to, newRow);
			for (Map.Entry<Integer, Double> e : newRow.entrySet())
			{
				double newVal = c*e.getValue();
				e.setValue(newVal);
			}
			return;
		}
		
		// This has to be handled separately to prevent 
		// concurrent modification of the map.
		if (from == to)
		{
			// This amounts to a scaling of the value by 1+c.
			double scale = 1.0 + c;
			if (scale == 0.0)
			{
				// If we end up scaling by zero, kill the representation.
				values.remove(from);
				return;
			}
			
			Map<Integer, Double> fromMap = values.get(from);
			for (Map.Entry<Integer, Double> e : fromMap.entrySet())
			{
				double newVal = scale * e.getValue();
				e.setValue(newVal);
			}
			return;
		}
		
		// The general case.
		Map<Integer, Double> fromMap = values.get(from);
		Map<Integer, Double> toMap = values.get(to);
		for (Map.Entry<Integer, Double> e : fromMap.entrySet())
		{
			int col = e.getKey();
			double val = e.getValue();
			
			if (!toMap.containsKey(col))
			{
				// If the "to" row doesn't have that column, this is easy.
				toMap.put(col, c * val);
			}
			else
			{
				double newVal = toMap.get(col) + c * val;
				
				// I suspect that in practical problems, this results in zeros fairly often,
				// and they don't need to be represented.
				if (newVal == 0.0)
				{
					toMap.remove(col);
				}
				else
				{
					toMap.put(col, newVal);
				}
			}
		}
		
		// There is a very slight chance that we have emptied the "to" map.  If we have, remove it.
		if (toMap.isEmpty()) { values.remove(to); }
		
	}

	@Override
	public void swapRows(int from, int to)
	{
		if (from >= nRows || to >= nRows || from < 0 || to < 0)
		{
			throw new IllegalArgumentException("Invalid row.");
		}
		
		// If the from and to indices are the same, do nothing.
		if (from == to) { return; }
		
		if (values.containsKey(from))
		{
			if (values.containsKey(to))
			{
				// If both rows have maps, swap the maps.
				Map<Integer, Double> fromRow = values.get(from);
				values.put(from, values.get(to));
				values.put(to, fromRow);
			}
			else
			{
				// if only from exists, move to the "to" row,
				values.put(to, values.get(from));
				values.remove(from);
			}
		}
		else if (values.containsKey(to))
		{
			// if only "to" exists, move to the "from" row.
			values.put(from, values.get(to));
			values.remove(to);
		}
		
		// Otherwise, neither row exists, and we're done already.
		
	}

	@Override
	public void scaleRow(int row, double c)
	{
		if (row >= nRows || row < 0)
		{
			throw new IllegalArgumentException("Invalid row.");
		}
		
		// Zero times anything is still zero.
		if (!values.containsKey(row)) { return; }
		
		// Anything times zero is zero.
		if (c == 0.0)
		{
			values.remove(row);
		}
		
		Map<Integer, Double> rowMap = values.get(row);
		for (Map.Entry<Integer, Double> e : rowMap.entrySet())
		{
			double newVal = c*e.getValue();
			e.setValue(newVal);
		}
	}

}
