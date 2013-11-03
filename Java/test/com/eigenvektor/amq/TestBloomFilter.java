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

public class TestBloomFilter
{
	private ApproxMemQuery<String> bf = new BloomFilter<String>(10000, 5);
	
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
