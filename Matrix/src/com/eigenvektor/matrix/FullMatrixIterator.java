package com.eigenvektor.matrix;

import java.util.Iterator;
import java.util.NoSuchElementException;

import com.eigenvektor.matrix.Matrix.Element;

/**
 * An iterator that iterates through the full matrix making no assumptions about how it is stored.
 */
class FullMatrixIterator implements Iterator<Matrix.Element>
{
	final Matrix m;
	int row;
	int col;
	
	final int nRows;
	final int nCols;
	
	public FullMatrixIterator(Matrix m)
	{
		this.m = m;
		this.row = 0;
		this.col = 0;
		
		this.nRows = m.getNRows();
		this.nCols = m.getNCols();
	}

	@Override
	public boolean hasNext()
	{
		return row != nRows;
	}

	@Override
	public Element next()
	{
		if (row == nRows) { throw new NoSuchElementException("Iterator is done."); }
		
		// Get the element.
		Element ret = new DefaultMatrixElement(row, col, m.get(row, col));
		
		// If at the end of the row, advance to the next row, otherwise advance along the row.
		if (col == nCols - 1)
		{
			col = 0;
			row++;
		}
		else
		{
			col++;
		}
		
		return ret;
	}

	@Override
	public void remove()
	{
		throw new UnsupportedOperationException("Remove not supported.");
	}

}
