package com.eigenvektor.amq;

/**
 * A quotienting strategy that modifies another by transfering a number of
 * bits from the remainder to the quotient, doubling the number of quotients
 * that number of times.
 */
public class DoublingQuotientingStrategy<T> implements QuotientingStrategy<T>
{
	private final QuotientingStrategy<T> strat;
	private final int numBits;
	
	// The mask to use to adjust the remainder.
	private final int remainderMask;
	
	/**
	 * Creates a Quotienting strategy that transfers a number of bits from remainder to
	 * quotient.
	 * 
	 * @param strat The underlying strategy.
	 * @param numBits The number of bits to transfer.
	 */
	public DoublingQuotientingStrategy(final QuotientingStrategy<T> strat, int numBits)
	{
		if (strat == null) { throw new NullPointerException("strat may not be null."); }
		if (numBits <= 0) { throw new IllegalArgumentException("numBits must be strictly positive"); }
		if (strat.getRemainderBits() < numBits) { 
			throw new IllegalArgumentException("Not enough bits in remainder to double " + numBits + " times."); }
		if (strat.getQuotientBits() + numBits > 32) { throw new IllegalArgumentException(
				"Adding " + numBits + " bits to the quotient would pass the 32 bit quotient limit."); }
		

		this.strat = strat;
		this.numBits = numBits;
		
		remainderMask = createRemainderMask(numBits, strat.getRemainderBits());
	}

	private static int createRemainderMask(int numBits, int remainderBits)
	{
		int mask = 0;
		for (int j = 0 ; j < remainderBits - numBits ; ++j)
		{
			mask = mask << 1;
			mask |= 1;
		}
		
		return mask;
	}

	@Override
	public int getQuotientBits()
	{
		return strat.getQuotientBits() + numBits;
	}

	@Override
	public int getRemainderBits()
	{
		return strat.getRemainderBits() - numBits;
	}

	@Override
	public QuotientingStrategy.QuotientAndRemainder getQuotientAndRemainder(T x)
	{
		QuotientAndRemainder oldQR = strat.getQuotientAndRemainder(x);
		
		// Kill the top bits of the remainder.
		int oldRemainder = oldQR.getRemainder();
		int newR = oldRemainder & remainderMask;
		
		// Transfer staid bits to the quotient.
		int newQ = oldQR.getQuotient() << numBits;
		newQ |= (oldRemainder >>> (strat.getRemainderBits() - numBits));
		
		return new QuotientAndRemainder(newQ, newR);
	}

	@Override
	public QuotientingStrategy<T> getDoubledStrategy(int numDoublings)
	{
		return new DoublingQuotientingStrategy<T>(this.strat, this.numBits +  numDoublings);
	}
	
	@Override
	public boolean equals(final Object o)
	{
		if (!(o instanceof DoublingQuotientingStrategy)) { return false; }
		@SuppressWarnings("unchecked")
		DoublingQuotientingStrategy<T> dqs = (DoublingQuotientingStrategy<T>) o;
		return dqs.strat.equals(this.strat) && dqs.numBits == this.numBits;
	}
	
	@Override
	public int hashCode()
	{
		return this.strat.hashCode() ^ this.numBits;
	}

}
