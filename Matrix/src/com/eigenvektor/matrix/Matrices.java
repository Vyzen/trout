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
	static FullMatrix fullIdentity(int size)
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
	static SparseMatrix sparseIdentity(int size)
	{
		if (size < 0) { throw new IllegalArgumentException("size must be non-negative."); }
		
		SparseMatrix ret = new SparseMatrix(size, size);
		for (int j = 0 ; j < size ; ++j) { ret.set(j, j, 1); }
		return ret;
	}
}
