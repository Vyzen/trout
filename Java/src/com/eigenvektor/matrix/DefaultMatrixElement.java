package com.eigenvektor.matrix;

import com.eigenvektor.matrix.Matrix.Element;

/**
 * Simple implementation of the MatrixElement interface.
 */
final class DefaultMatrixElement implements Element
{

	private final int row;
	private final int col;
	private final double value;
	
	public DefaultMatrixElement(int row, int col, double value)
	{
		this.row = row;
		this.col = col;
		this.value = value;
	}
	
	@Override
	public int getRow()
	{
		return row;
	}

	@Override
	public int getCol()
	{
		return col;
	}

	@Override
	public double getValue()
	{
		return value;
	}
	
	@Override
	public String toString()
	{
		StringBuffer ret = new StringBuffer();
		ret.append("(");
		ret.append(row);
		ret.append(", ");
		ret.append(col);
		ret.append(", ");
		ret.append(value);
		ret.append(")");
		return ret.toString();
	}

}
