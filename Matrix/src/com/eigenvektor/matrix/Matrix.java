package com.eigenvektor.matrix;

import java.util.Iterator;

/**
 * Specification for an immutable matrix.
 */
public interface Matrix
{

	/**
	 * Specification for an "element" interface.  This is used for
	 * iterating through the elements of a matrix.
	 */
	public interface Element
	{
		public int getRow();
		public int getCol();
		public double getValue();
	};
	
	/**
	 * Get an iterator for the elements of a matrix.
	 * 
	 * @return An iterator that iterates through the elements of this matrix.
	 * For sparse representations, it should not iterate through zero elements.
	 */
	Iterator<Element> getElements();
	
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
	Matrix multiply(double scalar);
	
	/**
	 * Multiplies this matrix by another matrix.
	 * 
	 * @param m The matrix to multiply by.
	 * @return A copy of <code>this</code> multiplied on the right by <code>m</code>
	 * @throws IllegalArgumentException if <code>m</code> is not compatible for multiplication.
	 */
	Matrix multiply(Matrix m);
}
