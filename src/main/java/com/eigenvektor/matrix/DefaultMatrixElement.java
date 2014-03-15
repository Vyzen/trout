/*
 *  Default implementation of Matrix.Element
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
 * Simple implementation of the MatrixElement interface.
 */
final class DefaultMatrixElement implements Matrix.Element
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
	public boolean equals(final Object o)
	{
		if (!(o instanceof Matrix.Element)) { return false; }
		Matrix.Element e = (Matrix.Element) o;
		return e.getRow() == this.row && e.getCol() == this.col && e.getValue() == this.value;
	}
	
	@Override
	public int hashCode()
	{
		return row ^ col ^ Double.valueOf(value).hashCode();
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
