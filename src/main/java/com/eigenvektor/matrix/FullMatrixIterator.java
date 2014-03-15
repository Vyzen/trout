/*
 *  Iterator that iterates over all the elements of a matrix.
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
import java.util.NoSuchElementException;

import com.eigenvektor.matrix.Matrix.Element;

/**
 * An iterator that iterates through the full matrix making no assumptions about how it is stored.
 */
final class FullMatrixIterator implements Iterator<Matrix.Element>
{
	private final Matrix m;
	private int row;
	private int col;
	
	private final int nRows;
	private final int nCols;
	
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
