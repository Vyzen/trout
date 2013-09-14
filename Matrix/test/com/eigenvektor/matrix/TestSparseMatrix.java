package com.eigenvektor.matrix;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestSparseMatrix
{

	private SparseMatrix s1 = new SparseMatrix(9, 7);
	private SparseMatrix s2 = new SparseMatrix(7, 3);
	private FullMatrix f1 = new FullMatrix(7, 3);
	
	@Before
	public void setUp() throws Exception
	{
		s1.set(4, 4, 3);
		s1.set(6, 1, 7);
		
		s2.set(4, 1, 5);
		s2.set(5, 2, 1);
		s2.set(1, 0, 33);
		
		f1.set(4, 1, 5);
		f1.set(5, 2, 1);
		f1.set(1, 0, 33);
	}

	@After
	public void tearDown() throws Exception
	{
	}

	@Test
	public void test()
	{
		assertTrue(s1.getNRows() == 9);
		assertTrue(s1.getNCols() == 7);
		
		// Test a copy.
		SparseMatrix s1Copy = new SparseMatrix(s1);
		assertTrue(s1.equals(s1Copy));
		assertTrue(s1.hashCode() == s1Copy.hashCode());
		
		// Test a scalar multiply.
		Matrix s1Times6 = s1.multiply(6);
		assertTrue(s1Times6.getNRows() == s1.getNRows());
		assertTrue(s1Times6.getNCols() == s1.getNCols());
		for (int j = 0 ; j < s1.getNRows() ; ++j)
		{
			for (int k = 0 ; k < s1.getNCols() ; ++k)
			{
				assertTrue(s1Times6.get(j,  k) == s1.get(j, k) * 6.0);
			}
		}
		
		// Test a matrix multiply.
		Matrix p1 = s1.multiply(s2);
		Matrix p2 = s1.multiply(f1);
		
		assertTrue(p1.getNRows() == 9);
		assertTrue(p1.getNCols() == 3);
		// s2 and f1 are semantically equal, so their products should be the same.
		assertTrue(p1.equals(p2));
	}
	
	@Test
	public void testIterator()
	{
		List<Matrix.Element> l = new ArrayList<Matrix.Element>();
		for (Matrix.Element x : s1)
		{
			l.add(x);
		}
		assertTrue(l.size() == 2);
	}

}
