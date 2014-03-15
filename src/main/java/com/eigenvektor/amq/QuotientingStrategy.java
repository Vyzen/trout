/*
 *  Interface for extracting quotient and remainder from an object.
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
	public static final class QuotientAndRemainder
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
		
		@Override
		public String toString()
		{
			StringBuilder sb = new StringBuilder();
			sb.append("[");
			sb.append(quotient);
			sb.append(",");
			sb.append(remainder);
			sb.append("]");
			return sb.toString();
		}
		
		@Override
		public boolean equals(final Object o)
		{
			if (!(o instanceof QuotientAndRemainder))
			{
				return false;
			}
			
			QuotientAndRemainder qr = (QuotientAndRemainder) o;
			return qr.quotient == this.quotient && qr.remainder == this.remainder;
		}
		
		@Override
		public int hashCode()
		{
			return quotient ^ remainder;
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
	 * @param numDoublings The number of doublings to apply.
	 * @return A strategy with <code>numDoublings</code> bits transferred from the remainder to
	 * the quotient.
	 */
	public QuotientingStrategy<T> getDoubledStrategy(int numDoublings);
}
