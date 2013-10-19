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
		
		// Split the hash.
		SplitInt split = new SplitInt(hash, this.qBits);
		
		this.addQR(split.quotient, split.remainder);
	}

	@Override
	public boolean contains(T x)
	{
		// Look for the hash code.
		final int hash = x.hashCode();
		
		// Split the hash.
		SplitInt split = new SplitInt(hash, this.qBits);
		
		return containsQR(split.quotient, split.remainder);
	}
	
	/**
	 * Tells if a given slot is empty.
	 * 
	 * @param slot The index of the slot.
	 * @return <code>true</code> iff this slot is completely empty.
	 */
	private boolean isEmpty(int slot)
	{
		return !isOccupied(slot) && !isContinuation(slot) && !isShifted(slot);
	}
	
	/**
	 * Tells if a particular slot is the canonical slot for some element in this filter.
	 * 
	 * @param slot The index of the  slot.
	 * @return <code>true</code> if the slot is the canonical slot for some element in this filter.
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
	 * Tells if the filter contains a quotient and remainder.
	 * 
	 * @param quotient The quotient to look for.
	 * @param remainder The remainder to look for.
	 * @return <code>true</code> iff that quotient and remainder are contained in the filter.
	 */
	private boolean containsQR(int quotient, int remainder)
	{
		// Check its canonical slot.
		if (isEmpty(quotient))
		{
			return false;
		}
		else
		{
			// TODO: handle the more complicated cases.
			return getRemainder(quotient) == remainder;
		}
	}

	/**
	 * Adds a quotient and remainder to the filter.
	 * 
	 * @param quotient The quotient to add.
	 * @param remainder The remainder to add.
	 */
	private void addQR(int quotient, int remainder)
	{
		// If the canonical slot is empty, use it directly.
		if (isEmpty(quotient))
		{
			fillSlot(quotient, true, false, false, remainder);
		}
		else
		{
			// TODO: handle the more complicated cases.
		}
	}

}
