/*
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

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestQuotientingStrategy
{

	@Before
	public void setUp() throws Exception
	{
	}

	@After
	public void tearDown() throws Exception
	{
	}
	
	/**
	 * Counts the number of set bits in x.
	 * 
	 * @param x The int to consider.
	 * @return The number of bits in x that are set to 1.
	 */
	private static int countBits(int x)
	{
		int count = 0;
		while (x != 0)
		{
			count += (x & 1);
			x = x >>> 1;
		}
		return count;
	}

	@Test
	public void testHashingStrategy()
	{
		final int test = 0xFFFFFFFF;
		
		QuotientingStrategy<Integer> qs1 = new HashQuotientingStrategy<Integer>(7, 25);
		
		// Check that the original quotienting strat is working properly.
		QuotientingStrategy.QuotientAndRemainder qr1 = qs1.getQuotientAndRemainder(test);
		assertTrue(countBits(qr1.getQuotient()) == 7);
		assertTrue(countBits(qr1.getRemainder()) == 25);
		
		QuotientingStrategy<Integer> qs2 = new HashQuotientingStrategy<Integer>(1, 25);
		
		// Check that the original quotienting strat is working properly.
		QuotientingStrategy.QuotientAndRemainder qr2 = qs2.getQuotientAndRemainder(test);
		assertTrue(countBits(qr2.getQuotient()) == 1);
		assertTrue(countBits(qr2.getRemainder()) == 25);
	}
	
	@Test
	public void testDoublingStrategy()
	{
		final int test = 0xFFFFFFFF;
		
		QuotientingStrategy<Integer> qs = new HashQuotientingStrategy<Integer>(7, 25);
		
		// Double this quotientingStrategy once.
		QuotientingStrategy<Integer> d1 = new DoublingQuotientingStrategy<Integer>(qs, 1);
		QuotientingStrategy.QuotientAndRemainder qr1 = d1.getQuotientAndRemainder(test);
		assertTrue(countBits(qr1.getQuotient()) == 8);
		assertTrue(countBits(qr1.getRemainder()) == 24);
		
	}

}
