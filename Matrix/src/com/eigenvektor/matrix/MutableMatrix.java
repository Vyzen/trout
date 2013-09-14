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

}
