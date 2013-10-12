package com.eigenvektor.amq;

/**
 * Specification for an approximate membership query.
 * 
 * @param <T> The type of element.
 */
public interface ApproxMemQuery<T>
{

	/**
	 * Adds an element to the AMQ.
	 * 
	 * @param x The element to add.
	 */
	public void add(T x);

	/**
	 * Tells whether an object has been added.  There is a chance of a false positive
	 * result, but not a false negative result.
	 * 
	 * @param x The element to query.
	 * @return <code>true</code> if <code>x</code> is in this set.  Very likely <code>false</code> otherwise.
	 */
	public boolean contains(T x);

}