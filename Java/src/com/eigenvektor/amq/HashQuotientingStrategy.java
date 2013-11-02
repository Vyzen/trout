package com.eigenvektor.amq;

/**
 * A quotienting strategy that takes a number of high order bits of the hash code
 * as the quotient and a number of low order bits of the hash code as the remainder.
 * 
 * @param <T> The type to use.
 */
public final class HashQuotientingStrategy<T> implements QuotientingStrategy<T>
{
	// The number of quotient and remainder bits.
	private final int nQBits;
	private final int nRBits;
	
	/**
	 * Creates a new HashQuotientingStrategy.
	 * 
	 * @param nQBits The number of quotient bits.
	 * @param nRBits The number of remainder bits.
	 */
	public HashQuotientingStrategy(int nQBits, int nRBits)
	{
		if (nQBits <= 0) { throw new IllegalArgumentException("nQBits must be positive."); }
		if (nRBits <= 0) { throw new IllegalArgumentException("nRBits must be positive."); }
		if (nQBits + nRBits > 32) { throw new IllegalArgumentException(
				"Number of quotient plus remainder bits must be 32 or less.");
		}
		
		this.nQBits = nQBits;
		this.nRBits = nRBits;
	}
	
	/**
	 * Creates a new HashQuotientingStrategy that uses any bit not in the quotient as part of the remainder.
	 * 
	 * @param nQBits The number of quotient bits.
	 */
	public HashQuotientingStrategy(int nQBits)
	{
		this(nQBits, 32-nQBits);
	}
	
	@Override
	public int getQuotientBits()
	{
		return this.nQBits;
	}

	@Override
	public int getRemainderBits()
	{
		return this.nRBits;
	}

	@Override
	public com.eigenvektor.amq.QuotientingStrategy.QuotientAndRemainder getQuotientAndRemainder(
			T x)
	{
		int hash = x.hashCode();
		
		int quotient = hash >>> (32-this.nQBits);
		int remainder = (hash << (32 - this.nRBits)) >>> (32- this.nRBits);
		
		return new QuotientAndRemainder(quotient, remainder);
	}

	@Override
	public QuotientingStrategy<T> getDoubledStrategy(int numDoublings)
	{
		if (this.nQBits + this.nRBits == 32)
		{
			// if all of the bits in the hash are used, we can do a simple recount.
			return new HashQuotientingStrategy<T>(this.nQBits + numDoublings, this.nRBits - numDoublings);
		}
		else
		{
			// Otherwise, use the double.
			return new DoublingQuotientingStrategy<T>(this, numDoublings);
		}
	}
	
	@Override
	public boolean equals(final Object o)
	{
		if (!(o instanceof HashQuotientingStrategy)) { return false; }
		@SuppressWarnings("unchecked")
		HashQuotientingStrategy<T> hqs = (HashQuotientingStrategy<T>) o;
		return hqs.nQBits == this.nQBits && hqs.nRBits == this.nRBits;
	}
	
	@Override
	public int hashCode()
	{
		return this.nQBits ^ (this.nRBits + 255);
	}

}
