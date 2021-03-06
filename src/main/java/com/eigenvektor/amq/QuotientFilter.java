/*
 *  Implementation of a Quotient Filter
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

import java.util.BitSet;
import java.util.Iterator;

import com.eigenvektor.amq.QuotientingStrategy.QuotientAndRemainder;

/**
 * Implementation of a quotient filter.
 * 
 * @param <T> The type of entry.
 */
public final class QuotientFilter<T> implements ApproxMemQuery<T>, 
		Iterable<QuotientingStrategy.QuotientAndRemainder>
{
	// The largest size of filter for which it makes sense to print out
	// the entire contents of the table in toString();
	private static final int TOSTRING_GIVE_UP = 8;
	
	private final QuotientingStrategy<T> quot;
	
	// The number of bits in the quotient.
	private final int qBits;
	
	// The number of bits in a record.
	private final int recBits;
	
	// The number of records (equals 2^recBits).
	private final int nSlots;
	
	// The Bit set.
	private final BitSet bits;
	
	// The number of occupied records;
	private int nOccupied = 0;
	
	/**
	 * Copy constructor.
	 * 
	 * @param other The filter to copy.
	 */
	public QuotientFilter(final QuotientFilter<T> other)
	{
		if (other == null) { throw new NullPointerException("other may not be null."); }
		
		this.quot = other.quot; // Assumed to be immutable, so fine to share.
		this.qBits = other.qBits;
		this.recBits = other.recBits;
		this.nSlots = other.nSlots;
		this.bits = (BitSet) other.bits.clone();
		this.nOccupied = other.nOccupied;
	}
	
	/**
	 * Creates a QuotientFilter with a particular quotienting strategy.
	 * 
	 * @param quot The quotienting strategy to employ.
	 */
	public QuotientFilter(QuotientingStrategy<T> quot)
	{
		if (quot == null) { throw new IllegalArgumentException("quot may not be null."); }
		
		this.qBits = quot.getQuotientBits();
		this.recBits = quot.getRemainderBits() + 3; // Enough space for the remainder, plus the three control bits.
		
		// Create a bit set
		this.nSlots = 1 << this.qBits;
		this.bits = new BitSet(nSlots * recBits);
		
		this.quot = quot;
	}
	
	/**
	 * Creates a new Quotient filter.
	 * 
	 * @param nQuotientBits The number of bits to use in the quotient.
	 */
	public QuotientFilter(int nQuotientBits)
	{
		this(new HashQuotientingStrategy<T>(nQuotientBits));
	}

	@Override
	public void add(T x)
	{
		// If we are out of space, refuse to insert.
		if (nOccupied == nSlots) { throw new IllegalStateException("Quotient filter is full."); }
		
		// Ask the quotienting strategy to split the object.
		QuotientingStrategy.QuotientAndRemainder split = quot.getQuotientAndRemainder(x);
		
		this.addQR(split.getQuotient(), split.getRemainder());
	}

	@Override
	public boolean contains(T x)
	{
		// Ask the quotienting strategy to split the object.
		QuotientingStrategy.QuotientAndRemainder split = quot.getQuotientAndRemainder(x);
		
		return containsQR(split.getQuotient(), split.getRemainder());
	}
	
	/**
	 * Gets the number of occupied slots.
	 * 
	 * @return the number of occupied slots.
	 */
	public int getNumOccupied()
	{
		return this.nOccupied;
	}
	
	/**
	 * Gets the next slot given a slot.
	 * 
	 * @param slot The slot.
	 * @return the next slot.
	 */
	private int nextSlot(int slot)
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
	private int prevSlot(int slot)
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
			emptySlot = nextSlot(emptySlot);
		}
		
		// Shift everything between here and there up one
		int toSlot = emptySlot;
		while (toSlot != slot)
		{
			int fromSlot = prevSlot(toSlot);
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
			slot = prevSlot(slot);
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
			if (isOccupied(slot) && !passedCanonical) { numOccupied++; }
			if (!isContinuation(slot)) { numRunStarts++; }

			// If we've passed the canonical slot, we're no longer counting occupied slots.
			if (slot == canonicalSlot) { passedCanonical = true; }
			
			// If we're ahead of the canonical slot, and we have the same
			// number of run starts as seen occupied slots, this is our run start.
			if (passedCanonical && (numRunStarts == numOccupied))
			{
				return slot;
			}
			
			slot = this.nextSlot(slot);
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
		int slot = this.nextSlot(startOfRun);
		while (isContinuation(slot))
		{
			if (getRemainder(slot) == remainder) { return true; }
			slot = this.nextSlot(slot);
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
		if (!isOccupied(quotient))
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
			this.nOccupied++;
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
			
			this.nOccupied++;
		}
		else
		{
			// There is already a run for this quotient, find it
			// and insert into it.
			
			// Now, when we find the run, it will find the start of 
			// the next run after where our run should be.
			int startOfCluster = findStartOfCluster(quotient);
			int startOfRun = findRun(startOfCluster, quotient);
			boolean inserted = insertIntoRun(startOfRun, remainder, startOfRun != quotient);
			if (inserted) { this.nOccupied++; }  // increment the counter only if it was actually added,
													// as opposed to just finding out it was already there.
		}
	}
	
	@Override
	public Iterator<QuotientAndRemainder> iterator()
	{
		return getFingerprintIterator();
	}

	/**
	 * Gets an iterator that iterates through all of the fingerprints in the filter.
	 * 
	 * @return An iterator that iterates through all of the fingerprints in the filter.
	 */
	public Iterator<QuotientingStrategy.QuotientAndRemainder> getFingerprintIterator()
	{
		if (this.nOccupied == 0)
		{
			return new Iterator<QuotientingStrategy.QuotientAndRemainder>() {
				@Override
				public boolean hasNext() { return false; }
				@Override
				public QuotientAndRemainder next() { return null; }
				@Override
				public void remove() 
				{
					throw new UnsupportedOperationException("Remove not supported for this iterator.");
				}
			};
		}
		
		// Find the first non-empty slot.
		int first = findFirstNonEmpty(0);
		
		// Start at a slot that is at the start of a cluster.
		int startOfCluster = findStartOfCluster(first);
		
		// Create an iterator that starts there.
		return new FingerprintIterator(startOfCluster);
	}
	
	/**
	 * Finds the first non-empty slot at or after a given slot.
	 * 
	 * @param slot The index of the slot to search from.
	 * @return The first non-empty slot at or after <code>slot</code>.
	 */
	private int findFirstNonEmpty(int slot)
	{
		while (isEmpty(slot))
		{
			slot = nextSlot(slot);
		}
		return slot;
	}

	/**
	 * A fingerprint iterator.
	 */
	private class FingerprintIterator implements Iterator<QuotientingStrategy.QuotientAndRemainder>
	{
		// The quotient we started at.
		private int start;
		
		// The slot we are currently at.
		private int cur;
		
		// The current quotient.
		private int quotient;
		
		// The next thing we are going to return.
		private QuotientingStrategy.QuotientAndRemainder next = null;
		
		/**
		 * Creates a new Fingerprint iterator.
		 * 
		 * @param start The quotient to start at.  Must be the start of a cluster.
		 */
		public FingerprintIterator(final int start)
		{
			this.start = start;
			this.cur = start;
			this.quotient = start;
			
			assert(isOccupied(start));
			assert(!isContinuation(start));
			assert(!isShifted(start));
			
			advance();
		}
		
		private void advance()
		{
			// If we're done, say so.
			// The this.next != null skips the case of first initialization.
			if (this.cur == this.start && this.next != null)
			{
				this.next = null;
				return;
			}
			
			// If we're pointed at an empty slot, point to the next non-empty slot.
			while (isEmpty(this.cur))
			{
				this.cur = nextSlot(this.cur);
				if (this.cur == this.start)
				{
					// If we're back to the start, we're done.
					next = null;
					return;
				}

			}

			// The next output has the head of the queue for a quotient and this slot's remainder.
			int remainder = getRemainder(this.cur);
			this.next = new QuotientingStrategy.QuotientAndRemainder(this.quotient, remainder);
			
			this.cur = nextSlot(this.cur);
			
			// If the next slot is not a continuation, pop the head off the queue.
			if (!isContinuation(this.cur))
			{
				advanceQuotient();
			}

		}

		/**
		 * Advance the quotient pointer to the next canoncial slot.
		 */
		private void advanceQuotient()
		{
			do { this.quotient = nextSlot(this.quotient); } while (!isOccupied(quotient));
		}

		@Override
		public boolean hasNext()
		{
			return next != null;
		}

		@Override
		public QuotientAndRemainder next()
		{
			QuotientingStrategy.QuotientAndRemainder ret = next;
			advance();
			return ret;
		}

		@Override
		public void remove()
		{
			throw new UnsupportedOperationException("Remove not supported for this iterator.");
		}
		
	}
	
	/**
	 * Gets a quotient filter with a double the quotient capacity of this one with the same data in it.  
	 * This is done by transferring one bit of each fingerprint from the remainder to the quotient.
	 * 
	 * @return The doubled quotient filter.
	 */
	public QuotientFilter<T> getDoubled()
	{
		return this.getDoubled(1);
	}
	
	/**
	 * Gets a quotient filter with a power of two times the quotient capacity of 
	 * this one with the same data in it.  This is done by transferring a number of bits 
	 * of each fingerprint from the remainder to the quotient.
	 * 
	 * @param numDoublings The number of doublings to apply. (i.e. the new filter will have
	 * 2^numDoublings times more quotients than this.
	 * @return The doubled quotient filter.
	 */
	public QuotientFilter<T> getDoubled(final int numDoublings)
	{
		if (numDoublings <= 0) { throw new IllegalArgumentException("numDoublings must be strictly positive."); }
		if (numDoublings >= quot.getRemainderBits())
		{
			throw new IllegalArgumentException("Not enough remainder bits to do " + numDoublings + " doublings");
		}
		
		// Create a quotient filter with a doubled strategy.
		QuotientingStrategy<T> newStrat = this.quot.getDoubledStrategy(numDoublings);
		QuotientFilter<T> ret = new QuotientFilter<T>(newStrat);
		
		// Iterate through the fingerprints, transferring from here to there.
		for (final QuotientingStrategy.QuotientAndRemainder qr : this)
		{
			int newRemainder = qr.getRemainder(); // We don't have to chop of bits because they are ignored anyway.
			int newQuotient = qr.getQuotient() << numDoublings;
			newQuotient |= newRemainder >>> (quot.getRemainderBits() - numDoublings); 
			
			ret.addQR(newQuotient, newRemainder);
		}
		
		return ret;
	}
	
	/**
	 * Merges another quotient filter into this one.  The other filter must be the same
	 * size and have the same quotienting strategy.
	 * 
	 * @param other The other to merge.
	 */
	public void merge(final QuotientFilter<T> other)
	{
		if (other == null) { throw new NullPointerException("other may not be null."); }
		if (other.nSlots != this.nSlots) { throw new IllegalArgumentException("other must be the same size as this."); }
		if (!other.quot.equals(this.quot)) { 
			throw new IllegalArgumentException("other must have the same quotienting stategy as this."); }
		if (this.nOccupied + other.nOccupied > this.nSlots) {
			throw new IllegalArgumentException("Merge of other would overfill this.");
		}
		
		// Easy case, if we're merging into ourselves, do nothing.
		if (other == this) { return; }
		
		for (final QuotientingStrategy.QuotientAndRemainder qr : other)
		{
			this.addQR(qr.getQuotient(), qr.getRemainder());
		}
	}
	
	/**
	 * A class to represent summary statistics for this quotient filter.
	 */
	public static final class Stats
	{
		private final int nSlots;
		private final int nOccupied;
		private final int nCanonical;
		private final int nShifted;
		private final int nRuns;
		private final int nClusters;
		
		/**
		 * Creates a new set of summary statistics.
		 * 
		 * @param nSlots
		 * @param nOccupied
		 * @param nCanonical
		 * @param nShifted
		 * @param nRuns
		 * @param nClusters
		 */
		private Stats(int nSlots, int nOccupied, int nCanonical, int nShifted, int nRuns,
				int nClusters)
		{
			this.nSlots = nSlots;
			this.nOccupied = nOccupied;
			this.nCanonical = nCanonical;
			this.nShifted = nShifted;
			this.nRuns = nRuns;
			this.nClusters = nClusters;
		}
		
		/**
		 * Gets the number of slots.
		 * 
		 * @return the number of slots.
		 */
		public int getNumSlots()
		{
			return nSlots;
		}

		/**
		 * Gets the number of occupied slots.
		 * 
		 * @return the number of occupied slots.
		 */
		public int getNumOccupied()
		{
			return nOccupied;
		}

		/**
		 * Gets the number of canonical slots used.
		 * 
		 * @return the number of canonical slots used.
		 */
		public int getNumCanonical()
		{
			return nCanonical;
		}

		/**
		 * Gets the number of shifted slots.
		 * 
		 * @return the number of shifted slots.
		 */
		public int getNumShifted()
		{
			return nShifted;
		}
		
		/**
		 * Gets the number of runs.
		 * 
		 * @return the number of runs.
		 */
		public int getNumRuns()
		{
			return nRuns;
		}
		
		/**
		 * Gets the number of clusters.
		 * 
		 * @return the number of clusters.
		 */
		public int getNumClusters()
		{
			return nClusters;
		}
		
		public String toString()
		{
			StringBuilder sb = new StringBuilder("[nSlots=");
			sb.append(this.nSlots);
			sb.append(", nOccupied=");
			sb.append(this.nOccupied);
			sb.append(", nCanonical=");
			sb.append(this.nCanonical);
			sb.append(", nShifted=");
			sb.append(this.nShifted);
			sb.append(", nRuns=");
			sb.append(this.nRuns);
			sb.append(", nClusters=");
			sb.append(this.nClusters);
			sb.append("]");
			return sb.toString();
		}
	}
	
	/**
	 * Gets some summary statistics for this quotient filter.
	 * 
	 * @return some summary stats.
	 */
	public Stats getStats()
	{
		int nCanonical = 0;
		int nShifted = 0;
		int nRuns = 0;
		int nClusters = 0;
		
		for (int j =0 ; j < nSlots ; ++j)
		{
			if (isOccupied(j)) { nCanonical++; }
			if (isShifted(j)) { nShifted++; }
			
			// If the slot is canonical and not shifted it is the start of a cluster and a run.
			if (isOccupied(j) && !isShifted(j)) 
			{ 
				nClusters++;
				nRuns++;
			}
			
			// If it's shifted, but not a continuation, it's the start of a run that is not the
			// start of a cluster.
			if (isShifted(j) && !isContinuation(j))
			{
				nRuns++;
			}
		}
		
		return new Stats(this.nSlots, this.nOccupied, nCanonical, nShifted, nRuns, nClusters);
	}
	
	@Override
	public String toString()
	{
		if (this.nSlots > TOSTRING_GIVE_UP)
		{
			StringBuilder sb = new StringBuilder("QuotientFilter<nSlots=");
			sb.append(this.nSlots);
			sb.append(",nOccupied=");
			sb.append(this.nOccupied);
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
