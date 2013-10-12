package com.eigenvektor.amq;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestHashExtender
{
	private HashExtender<String> he = new HashExtender<String>();
	
	private String t1 = "Test String 1";
	private String t2 = "Test String 2";
	private String t3 = "Test String 3";

	@Before
	public void setUp() throws Exception
	{
	}

	@After
	public void tearDown() throws Exception
	{
	}

	@Test
	public void test()
	{
		int[] hashes1 = he.getHashes(t1, 5);
		int[] hashes2 = he.getHashes(t2, 10);
		int[] hashes3 = he.getHashes(t3, 15);
		
		assertTrue(hashes1[0] == t1.hashCode());
		assertTrue(hashes2[0] == t2.hashCode());
		assertTrue(hashes3[0] == t3.hashCode());
		
		assertTrue(hashes1.length == 5);
		assertTrue(hashes2.length == 10);
		assertTrue(hashes3.length == 15);
		
		for (int j = 1 ; j < 15 ; ++j)
		{
			assertTrue(hashes3[j] != hashes3[0]);
		}
	}

}
