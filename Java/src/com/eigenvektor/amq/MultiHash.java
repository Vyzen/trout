package com.eigenvektor.amq;

/**
 * Specification for a class that takes multiple hash codes of an object.  Ideally,
 * these would be independent hash codes of the object.
 * 
 * @param <T> The type of object to hash.
 */
public interface MultiHash<T>
{
	/**
	 * Gets a number of hash codes of the object.
	 * 
	 * @param obj The object to get hash codes of.
	 * @param num The number of hash codes to get.
	 * @return An array of <code>num</code> hash codes for <code>obj</code>
	 */
	public int[] getHashes(T obj, int num);
	
}
