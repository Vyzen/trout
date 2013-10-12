package com.eigenvektor.amq;

import java.util.BitSet;

/**
 * Implementation of a Bloom Filter
 */
public final class BloomFilter<T> implements ApproxMemQuery<T>
{
	// The bits of the filter.
	private BitSet bits;
	
	// The number of bits and number of hashes.
	private int numBits;
	private int numHashes;
	
	// The hash code generator.
	private MultiHash<T> hasher;
	
	// The number of things that have been added to this bloom filter.
	private int numAdds = 0;
	
	/**
	 * Creates a new Bloom filter.
	 * 
	 * @param numBits The number of bits in the filter.
	 * @param numHashes The number of hashes to use per entry.
	 * @param hasher The hash generator.
	 */
	public BloomFilter(int numBits, int numHashes, MultiHash<T> hasher)
	{
		if (numBits <= 0) { throw new IllegalArgumentException("Number of bits must be positive."); }
		if (numHashes <= 0) { throw new IllegalArgumentException("Number of hashes must be positive."); }
		if (hasher == null) { throw new IllegalArgumentException("hasher must be non-null"); }
		
		this.numBits = numBits;
		this.numHashes = numHashes;
		this.hasher = hasher;
		
		this.bits = new BitSet(numBits);
	}
	
	/**
	 * Creates a new Bloom filter using the hash extender.
	 * 
	 * @param numBits The number of bits in the filter.
	 * @param numHashes The number of hashes to use per entry.
	 */
	public BloomFilter(int numBits, int numHashes)
	{
		this(numBits, numHashes, new HashExtender<T>());
	}
	
	/* (non-Javadoc)
	 * @see com.eigenvektor.amq.ApproxMemQuery#add(T)
	 */
	@Override
	public void add(T x)
	{
		if (x == null) { throw new IllegalArgumentException("x may not be null."); }
		
		// Get the hashes for x.
		int[] hashes = hasher.getHashes(x, numHashes);
		
		// If the bits for all those hashes are set, it's likely that the object is
		// already contained in the filter.  We don't need to do anything.
		if (containsHashes(hashes)) { return; }
		
		// insert the hashes.
		insertHashes(hashes);
		
		// Record that we've added something.
		numAdds++;
	}
	
	/* (non-Javadoc)
	 * @see com.eigenvektor.amq.ApproxMemQuery#contains(T)
	 */
	@Override
	public boolean contains(T x)
	{
		if (x == null) { throw new IllegalArgumentException("x may not be null."); }
		
		// Get the hashes for x.
		int[] hashes = hasher.getHashes(x, numHashes);
		
		// If the bits for all those hashes are set, it's likely that the object is
		// contained in the filter.
		return containsHashes(hashes);
	}
	
	/**
	 * Gets the number of independent adds.  This may be less than the number of times
	 * <code>add()</code> has been called if <code>add</code> was called with either the
	 * same object several times, or an object that generated a false positive for
	 * containment.
	 * 
	 * @return The number of independent adds.
	 */
	public int getNumAdds()
	{
		return numAdds;
	}
	
	/**
	 * Tells if all of a list of hashes are contained.
	 * 
	 * @param hashes the list of hashes.
	 * @return <code>true</code> iff all those hashes are contained.
	 */
	private boolean containsHashes(int[] hashes)
	{
		boolean ret = true;
		for (int hash : hashes)
		{
			ret &= bits.get((hash > 0) ? hash % numBits : hash % numBits + numBits);
		}
		return ret;
	}
	
	/**
	 * Inserts a bunch of hashes into the bitset.
	 * 
	 * @param hashes the list of hashes.
	 */
	private void insertHashes(int[] hashes)
	{
		for (int hash : hashes)
		{
			bits.set((hash > 0) ? hash % numBits : hash % numBits + numBits);
		}
	}
}
