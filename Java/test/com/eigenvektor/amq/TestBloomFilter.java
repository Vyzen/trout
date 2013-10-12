package com.eigenvektor.amq;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestBloomFilter
{
	private BloomFilter<String> bf = new BloomFilter<String>(10000, 5);
	
	private String ts1 = "Test String 1";
	private String ts2 = "Test String 2";
	private String ts3 = "Test String 3";
	private String ts4 = "Test String 4";
	private String ts5 = "Test String 5";
	private String ts6 = "Test String 6";
	private String ts7 = "Test String 7";
	private String ts8 = "Test String 8";
	private String ts9 = "Test String 9";

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
		bf.add(ts1);
		bf.add(ts2);
		bf.add(ts3);
		bf.add(ts4);
		
		assertTrue(bf.contains(ts1));
		assertTrue(bf.contains(ts2));
		assertTrue(bf.contains(ts3));
		assertTrue(bf.contains(ts4));
		assertTrue(!bf.contains(ts5));
		assertTrue(!bf.contains(ts6));
		assertTrue(!bf.contains(ts7));
		assertTrue(!bf.contains(ts8));
		assertTrue(!bf.contains(ts9));
	}

}
