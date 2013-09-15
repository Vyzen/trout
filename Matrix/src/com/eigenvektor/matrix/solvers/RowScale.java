package com.eigenvektor.matrix.solvers;

import com.eigenvektor.matrix.MutableMatrix;

/**
 * A matrix operation that scales a single row of a matrix.
 */
final class RowScale implements MatrixOperation
{
	final private int row;
	final private double scale;

	public RowScale(int row, double scale)
	{
		this.row = row;
		this.scale = scale;
	}
	
	@Override
	public void apply(MutableMatrix m)
	{
		m.scaleRow(row, scale);
	}

	@Override
	public void applyInverse(MutableMatrix m)
	{
		m.scaleRow(row, 1/scale);
	}

}
