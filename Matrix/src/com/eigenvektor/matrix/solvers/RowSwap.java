package com.eigenvektor.matrix.solvers;

import com.eigenvektor.matrix.MutableMatrix;

/**
 * A matrix operation that does a row swap.
 */
final class RowSwap implements MatrixOperation
{
	final private int from;
	final private int to;
	
	/**
	 * Creates an operation that swaps two rows
	 * 
	 * @param from the first row.
	 * @param to the second row.
	 */
	public RowSwap(int from, int to)
	{
		this.from = from;
		this.to = to;
	}

	@Override
	public void apply(MutableMatrix m)
	{
		m.swapRows(from, to);
	}

	@Override
	public void applyInverse(MutableMatrix m)
	{
		// Swaps are idempotent.
		m.swapRows(from, to);
	}

}
