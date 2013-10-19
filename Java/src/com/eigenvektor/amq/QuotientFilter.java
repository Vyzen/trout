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
	
	// The number of records (equals 2^recBits).
	private final int nSlots;
	
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
		
		// Create a bit set
		this.nSlots = 1 << this.qBits;
		this.bits = new BitSet(nSlots * recBits);
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
	 * Gets the start of the cluster that contains a given slot.
	 * 
	 * @param slot The slot to consider.
	 * @return The start of the cluster that contains <code>slot</code>
	 */
	private int findStartOfCluster(int slot)
	{
		// The start of the cluster is the first slot before at at this one that is not shifted.
		while (isShifted(slot))
		{
			slot--;
			
			// We might wrap around.
			if (slot < 0) { slot += this.nSlots; }
		}
		
		return slot;
	}
	
	/**
	 * Finds the run for a particular canonical slot.
	 * 
	 * @param startOfCluster The start of that slot's cluster.
	 * @param canonicalSlot The index of the canonical slot.
	 * @return The start of the run for elements that would canonically be in
	 * <code>canonicalSlot</code>
	 */
	private int findRun(int startOfCluster, int canonicalSlot)
	{
		// The number of canonically occupied slots found before the canonical slot.
		int numOccupied = 0;
		
		// The number of run starts found.
		int numRunStarts = 0;
		
		// Whether we have passed the canonical slot.
		// (we can't simply use slot <= canoncialSlot because of wrap-around)
		boolean passedCanonical = false;
		
		int slot = startOfCluster;
		while (true)
		{
			if (slot == canonicalSlot) { passedCanonical = true; }
			if (isOccupied(slot) && passedCanonical) { numOccupied++; }
			if (!isContinuation(slot)) { numRunStarts++; }
			
			// If we're ahead of the canonical slot, and we have the same
			// number of run starts as seen occupied slots, this is our run start.
			if (passedCanonical && (numRunStarts == numOccupied))
			{
				return slot;
			}
			
			slot++;
			if (slot == nSlots) { slot = 0; } // We might wrap around.
		}
	}
	
	/**
	 * Finds a remainder in a given run.
	 * 
	 * @param startOfRun The start of the run
	 * @param remainder The remainder to look for.
	 * @return <code>true</code> iff the remainder is in the run.
	 */
	private boolean findInRun(int startOfRun, int remainder)
	{
		// The first element of the run is not a continuation.
		if (getRemainder(startOfRun) == remainder) { return true; }
		
		// The rest of the run is a continuation.
		int slot = (startOfRun + 1) % nSlots;
		while (isContinuation(slot))
		{
			if (getRemainder(slot) == remainder) { return true; }
		}
		
		return false; // We didn't find it.
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
			// First, find the start of it's cluster.
			int startOfCluster = findStartOfCluster(quotient);
			
			// Then find its run within the cluster.
			int startOfRun = findRun(startOfCluster, quotient);
			
			// Then try to find the remainder in the run.
			return findInRun(startOfRun, remainder);
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
