package com.eigenvektor.matrix;

/**
 * Specification for a mutable matrix.  The size of the matrix can't change,
 * but the elements can.
 */
public interface MutableMatrix extends Matrix
{
	/**
	 * Sets the value at a position.
	 * 
	 * @param row The row index, from 0.
	 * @param col The column index, from 0.
	 * @param val The value to set that position to.
	 */
	public void set(int row, int col, double val);
	
	/**
	 * Multiplies this matrix by a scalar in-place.
	 * 
	 * @param scalar The scalar to multiply by.
	 */
	public void inPlaceMultiply(double scalar);
	
	/**
	 * Adds another matrix into this one.
	 * 
	 * @param m The matrix to add.
	 * @throws IllegalArgumentException if <code>m</code> is not compatible for addition.
	 */
	public void inPlaceAdd(Matrix m);
	
	
	/**
	 * Subtracts another matrix into this one.
	 * 
	 * @param m The matrix to subtract.
	 * @throws IllegalArgumentException if <code>m</code> is not compatible for subtraction.
	 */
	public void inPlaceSubtract(Matrix m);
	
	/**
	 * Performs a row operation on the matrix.  Adds one row to another,
	 * scaled by a coefficient.  this[to, :] += c*this[from, :].
	 * 
	 * @param from  The row to add from.
	 * @param to The row to add to
	 * @param c The coefficient.
	 */
	public void rowOperation(int from, int to, double c);
	
	/**
	 * Swaps two rows of the matrix.
	 * 
	 * @param from The first row to swap.
	 * @param to The second row to swap.
	 */
	public void swapRows(int from, int to);
	
	/**
	 * Scales a row by a coefficient.  this[row, :] = c*this[row, :]
	 * 
	 * @param row The row to scale.
	 * @param c
	 */
	public void scaleRow(int row, double c);
	
	/**
	 * Performs a column operation on the matrix.  Adds one column to another,
	 * scaled by a coefficient.  this[:, to] += c*this[:, from].
	 * 
	 * @param from  The column to add from.
	 * @param to The column to add to
	 * @param c The coefficient.
	 */
	public void columnOperation(int from, int to, double c);
	
	/**
	 * Swaps two columns of the matrix.
	 * 
	 * @param from The first column to swap.
	 * @param to The second column to swap.
	 */
	public void swapColumns(int from, int to);
	
	/**
	 * Scales a column by a coefficient.  this[:, col] = c*this[:, col]
	 * 
	 * @param col The column to scale.
	 * @param c
	 */
	public void scaleColumn(int col, double c);

}
