package com.eigenvektor.amq;

import java.util.BitSet;

/**
 * Implementation of a quotient filter.
 * 
 * @param <T> The type of entry.
 */
public final class QuotientFilter<T> implements ApproxMemQuery<T>
{
	// The largest size of filter for which it makes sense to print out
	// the entire contents of the table in toString();
	private static final int TOSTRING_GIVE_UP = 8;
	
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
	
	// The number of occupied records;
	private int numOccupied = 0;
	
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
	 * Gets the number of occupied slots.
	 * 
	 * @return the number of occupied slots.
	 */
	public int getNumOccupied()
	{
		return this.numOccupied;
	}
	
	/**
	 * Gets the next slot given a slot.
	 * 
	 * @param slot The slot.
	 * @return the next slot.
	 */
	int nextSlot(int slot)
	{
		int next = slot + 1;
		if (next == nSlots) { next = 0;	}
		return next;
	}
	
	/**
	 * Gets the previous slot given a slot.
	 * 
	 * @param slot The slot.
	 * @return the previous slot.
	 */
	int prevSlot(int slot)
	{
		int prev = slot - 1;
		if (prev == -1) { prev = nSlots - 1; }
		return prev;
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
	 * Sets the occupied flag for a slot.
	 * 
	 * @param slot The slot.
	 * @param value The value to set the occupied flag to.
	 */
	private void setOccupied(int slot, boolean value)
	{
		bits.set(this.recBits * slot, value);
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
	 * Sets the continuation bit for a slot.
	 * 
	 * @param slot The slot to set.
	 * @param val The value to set the continuation bit to.
	 */
	private void setContinuation(int slot, boolean val)
	{
		bits.set(this.recBits * slot + 1, val);
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
	 * Sets the shifted bit for a slot.
	 * 
	 * @param slot The slot to set.
	 * @param val The value to set the shifted bit to.
	 */
	private void setShifted(int slot, boolean val)
	{
		bits.set(this.recBits * slot + 2, val);
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
	 * Copies the data from one slot to another.  This method does not change
	 * any isOccupied flags since they are associated with the slot, not the
	 * data in it.
	 * 
	 * @param from The slot to copy from.
	 * @param to The slot to copy to.  Any data already in this slot (other than
	 * the isOccupied flag) is clobbered.
	 */
	private void copySlot(int from, int to)
	{
		int fromStart = this.recBits * from;
		int toStart = this.recBits * to;
		
		for (int j = 1 ; j < recBits ; ++j)
		{
			bits.set(toStart+j, bits.get(fromStart+j));
		}
	}
	
	/**
	 * Inserts some data into a slot, shifting all of the data up until the next empty slot to the right.
	 * 
	 * @param slot The slot to insert into.
	 * @param isContinuation The isContinuation flag to be inserted.
	 * @param isShifted The isShifted flag to be inserted.
	 * @param remainder The remainder in the slot to be inserted.
	 */
	private void insertIntoSlot(
			int slot, 
			boolean isContinuation, 
			boolean isShifted, 
			int remainder)
	{
		// Find the first empty slot at or past slot.
		int emptySlot = slot;
		while (!isEmpty(emptySlot))
		{
			emptySlot++;
			if (emptySlot == nSlots) { emptySlot = 0; }
		}
		
		// Shift everything between here and there up one
		int toSlot = emptySlot;
		while (emptySlot != slot)
		{
			int fromSlot = toSlot - 1;
			if (fromSlot < 0) { fromSlot += nSlots; }
			copySlot(fromSlot, toSlot);
			setShifted(toSlot, true); // record the shift
			toSlot = fromSlot;
		}
		
		// The isOccupied flag for the slot shouldn't change.
		boolean isOccupied = isOccupied(slot);
		
		// Fill the slot with the new stuff.
		fillSlot(slot, isOccupied, isContinuation, isShifted, remainder);
	}
	
	/**
	 * Inserts a remainder into a run.
	 * 
	 * @param startOfRun The start of the run.
	 * @param remainder The remainder to insert.
	 * @param isShifted <code>true</code> iff this run is shifted from the canonical slot.
	 * @return <code>true</code> if the insertion was done or <code>false</code> if that
	 * remainder was already there.
	 */
	private boolean insertIntoRun(
			int startOfRun,
			int remainder,
			boolean isShifted)
	{
		// Special case.  We are inserting at the start of the run.
		int startRemainder = getRemainder(startOfRun);
		if (startRemainder == remainder)
		{
			return false;
		}
		if (startRemainder > remainder)
		{
			// The start slot is now a continuation.
			setContinuation(startOfRun, true);
			
			// Insert the new remainder here.
			insertIntoSlot(startOfRun, false, isShifted, remainder);
			
			return true;
		}
		
		// Find the position in the run to insert.
		int slot = nextSlot(startOfRun);
		int curRemainder = getRemainder(slot);
		while (isContinuation(slot) && curRemainder <= remainder)
		{
			if (curRemainder == remainder) { return false; }
			
			slot = nextSlot(slot);
			curRemainder = getRemainder(slot);
		}
		
		// insert the new remainder here.
		insertIntoSlot(slot, true, true, remainder);
		return true;
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
			// First, find the start of its cluster.
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
		if (isEmpty(quotient))
		{
			// If the canonical slot is empty, use it directly.
			fillSlot(quotient, true, false, false, remainder);
			this.numOccupied++;
		}
		else if (!isOccupied(quotient))
		{
			// There is no previous element with this quotient, so we are
			// starting a new, already shifted run.
			
			// Set the occupied flag for this slot.
			setOccupied(quotient, true);
			
			// Now, when we find the run, it will find the start of 
			// the next run after where our run should be.
			int startOfCluster = findStartOfCluster(quotient);
			int startOfRun = findRun(startOfCluster, quotient);
			
			// Insert our new remainder here.
			// It is shifted, but not a continuation of anything because
			// this is a new run.
			insertIntoSlot(startOfRun, false, true, remainder);
			
			this.numOccupied++;
		}
		else
		{
			// There is already a run for this quotient, find it
			// and insert into it.
			
			// Now, when we find the run, it will find the start of 
			// the next run after where our run should be.
			int startOfCluster = findStartOfCluster(quotient);
			int startOfRun = findRun(startOfCluster, quotient);
			boolean inserted = insertIntoRun(startOfRun, remainder, startOfRun == quotient);
			if (inserted) { this.numOccupied++; }  // increment the counter only if it was actually added,
													// as opposed to just finding out it was already there.
		}
	}
	
	@Override
	public String toString()
	{
		if (this.nSlots > TOSTRING_GIVE_UP)
		{
			StringBuilder sb = new StringBuilder("QuotientFilter<nSlots=");
			sb.append(this.nSlots);
			sb.append(",nOccupied=");
			sb.append(this.numOccupied);
			sb.append(">");
			return sb.toString();
		}
		else
		{
			StringBuilder sb = new StringBuilder("QuotientFilter<\n");
			for (int j = 0 ; j < this.nSlots ; ++j)
			{
				sb.append(j);
				sb.append(": ");
				appendSlot(sb, j);
				sb.append("\n");
			}
			sb.append(">");
			return sb.toString();
		}
	}

	/**
	 * Appends a representation of a slot to a string builder.
	 * 
	 * @param sb the builder to add to.
	 * @param idx The index of the slot.
	 */
	private void appendSlot(StringBuilder sb, int idx)
	{
		sb.append("[o=");
		sb.append(isOccupied(idx)?1:0);
		sb.append(",c=");
		sb.append(isContinuation(idx)?1:0);
		sb.append(",s=");
		sb.append(isShifted(idx)?1:0);
		sb.append(": ");
		sb.append(getRemainder(idx));
		sb.append("]");
	}

}
