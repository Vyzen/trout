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

}
