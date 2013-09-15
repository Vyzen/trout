package com.eigenvektor.matrix;

/**
 * A static class with some useful factory methods.
 */
public final class Matrices
{

	/**
	 * Private constructor.
	 */
	private Matrices() { throw new UnsupportedOperationException("Can't Instantiate"); }
	
	/**
	 * Returns a full matrix version of the identity matrix.
	 * 
	 * @param size The size of the identity to create.
	 * @return A full identity matrix of that size.
	 */
	public static FullMatrix fullIdentity(int size)
	{
		if (size < 0) { throw new IllegalArgumentException("size must be non-negative."); }
		
		FullMatrix ret = new FullMatrix(size, size);
		for (int j = 0 ; j < size ; ++j) { ret.set(j, j, 1); }
		return ret;
	}
	
	/**
	 * Returns a sparse matrix version of the identity matrix.
	 * 
	 * @param size The size of the identity to create.
	 * @return A sparse identity matrix of that size.
	 */
	public static SparseMatrix sparseIdentity(int size)
	{
		if (size < 0) { throw new IllegalArgumentException("size must be non-negative."); }
		
		SparseMatrix ret = new SparseMatrix(size, size);
		for (int j = 0 ; j < size ; ++j) { ret.set(j, j, 1); }
		return ret;
	}

	/**
	 * Tells of two matrices have entries that are close to each other.
	 * 
	 * @param m1  The first matrix.
	 * @param m2  The second matrix
	 * @param tol The tolerance.
	 * @return <code>true</code> iff the m1 and m2 are the same size, and 
	 * the entries of m1 and m2 are within tol of each other.
	 */
	public static boolean areClose(final Matrix m1, final Matrix m2, double tol)
	{
		if (m1 == null || m2 == null)
		{
			throw new NullPointerException("m1 and m2 may not be null.");
		}
		
		if (tol < 0.0)
		{
			throw new IllegalArgumentException("tol must be non-negative.");
		}
		
		if (m1.getNRows() != m2.getNRows() || m1.getNCols() != m2.getNCols())
		{
			return false;
		}
		
		for (int j = 0 ; j < m1.getNRows() ; ++j)
		{
			for (int k = 0 ; k < m2.getNCols() ; ++k)
			{
				double diff = Math.abs(m1.get(j, k) - m2.get(j,  k));
				if (diff > tol) { return false; } 
			}
		}
		
		return true;
	}
	
}
