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
		
		int test2 = (3 << 29) + 66;
		assertTrue(!qf.contains(test2));
	}
	
	@Test
	public void testSingleRun()
	{
		// Create a single run of integers with the same quotient.
		ApproxMemQuery<Integer> qf = new QuotientFilter<Integer>(3); 
		
		int test1 = (3 << 29) + 63;
		int test2 = (3 << 29) + 66;
		int test3 = (3 << 29) + 65;
		int test4 = (3 << 29) + 61;
		
		qf.add(test1);
		qf.add(test2);
		qf.add(test3);
		qf.add(test4);
		
		assertTrue(qf.contains(test1));
		assertTrue(qf.contains(test2));
		assertTrue(qf.contains(test3));
		assertTrue(qf.contains(test4));
	}

}
