package com.eigenvektor.matrix;

/**
 * An abstract implementation of the Matrix interface.  Provides
 * <code>equals()</code> and <code>hashCode()</code> and 
 * <code>toString()</code>.
 */
public abstract class AbstractMatrix implements Matrix
{
	/**
	 * Gets a string representation of this.
	 */
	@Override
	public String toString()
	{
		StringBuffer ret = new StringBuffer();
		
		int nRows = getNRows();
		int nCols = getNCols();
		
		if (nRows > 10 || nCols > 10)
		{
			// If the matrix is really big, just say the size.
			ret.append("[");
			ret.append(getNRows());
			ret.append("x");
			ret.append(getNCols());
			ret.append(" matrix]");
		}
		else
		{
			// Otherwise print out the matrix.
			ret.append("[");
			for (int j = 0 ; j < nRows ; ++j)
			{
				for (int k = 0 ; k < nCols ; ++k)
				{
					ret.append(get(j,k));
					if (k < nCols - 1)
					{
						ret.append(", ");
					}
					else if (j < nRows - 1)
					{
						ret.append("; ");
					}
				}
			}
			ret.append("[");
		}
		
		return ret.toString();
	}
	
	/**
	 * Tells if another object is equal to this.
	 * 
	 * @return <code>true</code> iff <code>o</code> is a matrix that is
	 * semantically equal to <code>this</code>
	 */
	@Override
	public boolean equals(final Object o)
	{
		// If it's not even a matrix, we're done.
		if (!(o instanceof Matrix))
		{
			return false;
		}
		
		Matrix m = (Matrix) o;
		
		// If it isn't the same size as this, we're done.
		final int nRows = this.getNRows();
		final int nCols = this.getNCols();
		if (m.getNRows() != nRows || m.getNCols() != nCols)
		{
			return false;
		}
		
		// Otherwise, do it element by element.
		for (int j = 0 ; j < nRows ; ++j)
		{
			for (int k = 0 ; k < nCols ; ++k)
			{
				if (get(j, k) != m.get(j, k)) { return false; }
			}
		}
		
		return true;
	}
	
	/**
	 * Gets a hash code for this object.
	 */
	@Override
	public int hashCode()
	{
		int hash = 0;
		
		final int nRows = this.getNRows();
		final int nCols = this.getNCols();
		for (int j = 0 ; j < nRows ; ++j)
		{
			for (int k = 0 ; k < nCols ; ++k)
			{
				hash ^= Double.valueOf(get(j, k)).hashCode();
			}
		}
		
		return hash;
	}
}
