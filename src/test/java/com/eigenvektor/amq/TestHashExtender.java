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
