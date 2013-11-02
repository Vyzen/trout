package com.eigenvektor.amq;

/**
 * Specification for a class that takes an object, generates a fingerprint,
 * and splits that fingerprint into a quotient and remainder.
 * 
 * Implementations should be immutable and override <code>equals()</code> 
 * and <code>hashCode()</code> so identical strategies are matched.
 * 
 * @param <T> The class this acts on.
 */
public interface QuotientingStrategy<T>
{
	public static class QuotientAndRemainder
	{
		private final int quotient;
		private final int remainder;
		
		/**
		 * Creates a new instance with a given quotient and remainder.
		 * 
		 * @param quotient the quotient.
		 * @param remainder the remainder.
		 */
		public QuotientAndRemainder(int quotient, int remainder)
		{
			this.quotient = quotient;
			this.remainder = remainder;
		}

		/**
		 * Gets the quotient.
		 * 
		 * @return the quotient
		 */
		public int getQuotient()
		{
			return quotient;
		}

		/**
		 * Gets the remainder.
		 * 
		 * @return the remainder
		 */
		public int getRemainder()
		{
			return remainder;
		}
	}
	
	/**
	 * Gets the number of bits in the quotient.
	 * 
	 * @return the number of bits the quotient.
	 */
	public int getQuotientBits();
	
	/**
	 * Gets the number of bits in the remainder.
	 * 
	 * @return the number of bits in the remainder.
	 */
	public int getRemainderBits();
	

	/**
	 * Gets the quotient and remainder for a given object.
	 * 
	 * @param x The object to use.
	 * @return The quotient and remainder for that object.
	 */
	public QuotientAndRemainder getQuotientAndRemainder(T x);
	
	/**
	 * Gets a quotienting strategy with the number of quotients doubled
	 * (and the number of remainders halved) a certain number of times.
	 * 
	 * This can be implemented simply using the <code>DoublingQuotientingStrategy</code>
	 * class applied to <code>this</code> but may be more efficient to implement in
	 * another way.
	 * 
	 * @param numDoublings
	 * @return
	 */
	public QuotientingStrategy<T> getDoubledStrategy(int numDoublings);
}
