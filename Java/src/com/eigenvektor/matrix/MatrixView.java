/*
 *  Class that allows viewing a portion of another matrix as a matrix.
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

/**
 * A matrix that is actually a "view" onto another matrix, so you can process only a subsection
 * of a matrix.
 */
public final class MatrixView extends AbstractMatrix
{
	private final Matrix mat;
	private final int startRow;
	private final int endRow;
	private final int startCol;
	private final int endCol;
	
	/**
	 * Creates a new Matrix view.
	 * 
	 * @param mat The matrix to view.
	 * @param startRow The first row of the matrix to include in the view.
	 * @param endRow The row after the last row of the matrix to include in the view.
	 * @param startCol The first column of the matrix to include in the view.
	 * @param endCol The column after the last column of the matrix to include in the view.
	 */
	public MatrixView(Matrix mat, int startRow, int endRow, int startCol, int endCol)
	{
		this.mat = mat;
		this.startRow = startRow;
		this.endRow = endRow;
		this.startCol = startCol;
		this.endCol = endCol;
		
		// Do some checks.
		if (mat == null) { throw new NullPointerException("mat may not be null."); }
		if (startRow >= endRow || endRow > mat.getNRows())
		{
			throw new IllegalArgumentException("Invalid start and end rows.");
		}
		
		if (startCol >= endCol || endCol > mat.getNCols())
		{
			throw new IllegalArgumentException("Invalid start and end cols.");
		}
	}

	@Override
	public int getNRows()
	{
		return this.endRow - this.startRow;
	}

	@Override
	public int getNCols()
	{
		return this.endCol - this.startCol;
	}

	@Override
	public double get(int row, int col)
	{
		int resolvedRow = row + this.startRow;
		int resolvedCol = col + this.startCol;
		checkIndices(resolvedRow, resolvedCol);
		return this.mat.get(resolvedRow, resolvedCol);
	}
	
	/**
	 * Checks that indices are valid, and throw if they are not.
	 */
	private void checkIndices(final int row, final int col)
	{
		if (row < startRow || col < endCol)
		{
			throw new IllegalArgumentException("row and col must be non-negative.");
		}
		
		if (row >= endRow || col >= endCol)
		{
			throw new IllegalArgumentException("row or col outside of matrix range.");
		}
	}

	@Override
	public Matrix multiply(double scalar)
	{
		FullMatrix ret = new FullMatrix(getNRows(), getNCols());
		for (int row = startRow ; row < endRow ; ++row)
		{
			for (int col = startCol ; col < endCol ; ++col)
			{
				ret.set(row-startRow, col-startCol, mat.get(row, col));
			}
		}
		return ret;
	}

	@Override
	public Iterator<Element> iterator()
	{
		return new FullMatrixIterator(this);
	}

}
