package com.eigenvektor.amq;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestQuotientFilter
{
	@Before
	public void setUp() throws Exception
	{
	}

	@After
	public void tearDown() throws Exception
	{
	}

	@Test
	public void testSimpleInsert()
	{
		ApproxMemQuery<Integer> qf = new QuotientFilter<Integer>(3);
		qf.add(5);
		assertTrue(qf.contains(5));
		
		int test = (3 << 29) + 63;
		qf.add(test);
		assertTrue(qf.contains(test));
	}

}
