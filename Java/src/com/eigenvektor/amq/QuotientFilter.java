package com.eigenvektor.amq;

import java.util.BitSet;

/**
 * Implementation of a quotient filter.
 * 
 * @param <T> The type of entry.
 */
public final class QuotientFilter<T> implements ApproxMemQuery<T>
{
	/**
	 * A simple class that splits an int between quotient and remainder.
	 */
	private static class SplitInt
	{
		/**
		 * Splits an int.
		 * 
		 * @param x The int to split.
		 * @param qBits The number of bits in the quotent.
		 */
		public SplitInt(int x, int qBits)
		{
			quotient = x >> 32-qBits;
			remainder = (x << qBits) >>> qBits;
		}
		
		public final int quotient;
		public final int remainder;
	}
	
	// The number of bits in the quotient.
	private final int qBits;
	
	// The number of bits in a record.
	private final int recBits;
	
	// The Bit set.
	private final BitSet bits;
	
	/**
	 * Creates a new Quotient filter.
	 * 
	 * @param nQuotientBits The number of bits to use in the quotient.
	 */
	public QuotientFilter(int nQuotientBits)
	{
		if (nQuotientBits < 1 || nQuotientBits > 31)
		{
			throw new IllegalArgumentException("Quotient must be between 1 and 31 bits.");
		}
		
		this.qBits = nQuotientBits;
		this.recBits = (32 - this.qBits) + 3; // Enough space for the remainder, plus the three control bits.
		
		// Create a bit
		int nRecords = 1 << this.qBits;
		bits = new BitSet(nRecords * recBits);
	}

	@Override
	public void add(T x)
	{
		// Add the hash code.
		final int hash = x.hashCode();
		this.addInt(hash);
	}

	@Override
	public boolean contains(T x)
	{
		// Look for the hash code.
		final int hash = x.hashCode();
		return containsInt(hash);
	}
	
	/**
	 * Tells if a particular slot is occupied.
	 * 
	 * @param slot The index of the  slot.
	 * @return <code>true</code> if the slot is occupied.
	 */
	private boolean isOccupied(int slot)
	{
		return bits.get(this.recBits * slot); // Bit 0 is the "occupied" bit.
	}
	
	/**
	 * Tells if a particular slot is a continuation.
	 * 
	 * @param slot The index of the slot.
	 * @return <code>true</code> if the slot is a continuation.
	 */
	private boolean isContinuation(int slot)
	{
		return bits.get(this.recBits * slot + 1);
	}
	
	/**
	 * Tells if a particular slot is shifted.
	 * 
	 * @param slot The index of the slot.
	 * @return <code>true</code> if the slot is shifted.
	 */
	private boolean isShifted(int slot)
	{
		return bits.get(this.recBits * slot + 2);
	}
	
	/**
	 * Gets the remainder from a slot.
	 * 
	 * @param slot The index of the slot.
	 * @return The remainder stored in the slot.
	 */
	private int getRemainder(int slot)
	{
		int slotStart = this.recBits * slot;
		int ret = 0;
		for (int j = slotStart + 3 ; j < slotStart + recBits ; ++j)
		{
			ret = ret << 1;
			if (bits.get(j))
			{
				ret |= 1;
			}
		}
		return ret;
	}
	
	/**
	 * Fills a slot with data.
	 * 
	 * @param slot The index of the slot.
	 * @param isOccupied The isOccupied flag.
	 * @param isContinuation The isContinuation flag.
	 * @param isShifted The isShifted flag.
	 * @param remainder The remainder in the slot.
	 */
	private void fillSlot(
			int slot, 
			boolean isOccupied, 
			boolean isContinuation, 
			boolean isShifted, 
			int remainder)
	{
		int slotStart = this.recBits * slot;
		bits.set(slotStart, isOccupied);
		bits.set(slotStart + 1, isContinuation);
		bits.set(slotStart + 2, isShifted);
		for (int j = slotStart + recBits - 1 ; j >= slotStart + 3 ; --j)
		{
			bits.set(j, (remainder & 1) == 1);
			remainder = remainder >> 1;
		}
	}
	
	/**
	 * Tells if the filter (probably) contains an integer.
	 * 
	 * @param x The integer to check for.
	 * @return <code>true</code> if x is contained in the filter.  Probably <code>false</code> otherwise.
	 */
	private boolean containsInt(int x)
	{
		// Split the integer.
		SplitInt split = new SplitInt(x, this.qBits);
		
		// Check its canonical slot.
		if (!isOccupied(split.quotient))
		{
			return false;
		}
		else
		{
			// TODO: handle the more complicated cases.
			return getRemainder(split.quotient) == split.remainder;
		}
	}

	/**
	 * Adds an integer to the set.
	 * 
	 * @param x The integer to add.
	 */
	private void addInt(int x)
	{
		// Split the integer.
		SplitInt split = new SplitInt(x, this.qBits);

		// Find its canonical slot.
		if (!isOccupied(split.quotient))
		{
			fillSlot(split.quotient, true, false, false, split.remainder);
		}
		else
		{
			// TODO: handle the more complicated cases.
		}
	}

}
