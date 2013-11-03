/*
 *  Interface for an immutable Matrix.
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

/**
 * Specification for an immutable matrix.
 */
public interface Matrix extends Iterable<Matrix.Element>
{

	/**
	 * Specification for an "element" interface.  This is used for
	 * iterating through the elements of a matrix.
	 */
	public static interface Element
	{
		public int getRow();
		public int getCol();
		public double getValue();
	};
	
	/**
	 * Gets the number of rows.
	 * 
	 * @return The number of rows.
	 */
	public int getNRows();
	
	
	/**
	 * Gets the number of columns.
	 * 
	 * @return the number of columns.
	 */
	public int getNCols();
	
	/**
	 * Gets the element a a given row and column.
	 * 
	 * @param row The row index, from 0.
	 * @param col The column index, from 0.
	 * @return The element at that position.
	 */
	public double get(int row, int col);
	
	/**
	 * Multiplies this matrix by a scalar.
	 * 
	 * @param scalar The scalar to multiply by.
	 * @return A copy of <code>this</code> multiplied by <code>scalar</code>
	 */
	public Matrix multiply(double scalar);
	
	/**
	 * Multiplies this matrix by another matrix.
	 * 
	 * @param m The matrix to multiply by.
	 * @return A copy of <code>this</code> multiplied on the right by <code>m</code>
	 * @throws IllegalArgumentException if <code>m</code> is not compatible for multiplication.
	 */
	public Matrix multiply(Matrix m);
	
	/**
	 * Adds a matrix to this matrix.
	 * 
	 * @param m The matrix to add.
	 * @return A copy of <code>this</code> with <code>m</code> added to it.
	 * @throws IllegalArgumentException if <code>m</code> is not compatible for addition.
	 */
	public Matrix add(Matrix m);
	
	/**
	 * Subtracts a matrix from this matrix.
	 * 
	 * @param m The matrix to subtract.
	 * @return A copy of <code>this</code> with <code>m</code> added to it.
	 * @throws IllegalArgumentException if <code>m</code> is not compatible for subtraction.
	 */
	public Matrix subtract(Matrix m);
}
