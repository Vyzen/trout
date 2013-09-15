package com.eigenvektor.matrix.solvers;

import com.eigenvektor.matrix.MutableMatrix;

/**
 * A matrix operation that does a row -> row + c * otherRow operation.
 */
final class RowOperation implements MatrixOperation
{
	final private int from;
	final private int to;
	final private double c;
	
	public RowOperation(int from, int to, double c)
	{
		this.from = from;
		this.to = to;
		this.c = c;
	}

	@Override
	public void apply(MutableMatrix m)
	{
		m.rowOperation(from, to, c);
	}

	@Override
	public void applyInverse(MutableMatrix m)
	{
		m.rowOperation(from, to, -c);
	}

}
