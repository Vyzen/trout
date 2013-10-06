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
		s1.set(6, 2, 8);
		
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
		
		// But one should be sparse, and the other full.
		assertTrue(p1 instanceof SparseMatrix);
		assertTrue(p2 instanceof FullMatrix);
	}
	
	@Test
	public void testIterator()
	{
		List<Matrix.Element> l = new ArrayList<Matrix.Element>();
		for (Matrix.Element x : s1)
		{
			l.add(x);
		}
		assertTrue(l.size() == 3);
	}
	
	@Test
	public void testRowOperations()
	{
		// Make a copy of s1 to play with.
		SparseMatrix s = new SparseMatrix(s1);
		// Try a scaling.
		s.scaleRow(4, 7);
		for (int j = 0 ; j < s.getNCols() ; ++j)
		{
			assertTrue(s.get(0, j) == s1.get(0, j));
			assertTrue(s.get(1, j) == s1.get(1, j));
			assertTrue(s.get(2, j) == s1.get(2, j));
			assertTrue(s.get(3, j) == s1.get(3, j));
			assertTrue(s.get(5, j) == s1.get(5, j));
			assertTrue(s.get(6, j) == s1.get(6, j));
			assertTrue(s.get(7, j) == s1.get(7, j));
			assertTrue(s.get(8, j) == s1.get(8, j));

			assertTrue(s.get(4, j) == s1.get(4, j)*7.0);
		}
		
		// Now try swapping rows.
		s = new SparseMatrix(s1);
		s.swapRows(4, 6); // Both rows are represented.
		s.swapRows(1, 2); // Both are not.
		for (int j = 0 ; j < s.getNCols() ; ++j)
		{
			assertTrue(s.get(0, j) == s1.get(0, j));
			assertTrue(s.get(1, j) == s1.get(2, j));
			assertTrue(s.get(2, j) == s1.get(1, j));
			assertTrue(s.get(3, j) == s1.get(3, j));
			assertTrue(s.get(4, j) == s1.get(6, j));
			assertTrue(s.get(5, j) == s1.get(5, j));
			assertTrue(s.get(6, j) == s1.get(4, j));
			assertTrue(s.get(7, j) == s1.get(7, j));
			assertTrue(s.get(8, j) == s1.get(8, j));
		}
		
		s = new SparseMatrix(s1);
		s.swapRows(4, 2); // First row is represented, second is not.
		s.swapRows(1, 6); // Second row is represented, first is not.
		for (int j = 0 ; j < s.getNCols() ; ++j)
		{
			assertTrue(s.get(0, j) == s1.get(0, j));
			assertTrue(s.get(1, j) == s1.get(6, j));
			assertTrue(s.get(2, j) == s1.get(4, j));
			assertTrue(s.get(3, j) == s1.get(3, j));
			assertTrue(s.get(4, j) == s1.get(2, j));
			assertTrue(s.get(5, j) == s1.get(5, j));
			assertTrue(s.get(6, j) == s1.get(1, j));
			assertTrue(s.get(7, j) == s1.get(7, j));
			assertTrue(s.get(8, j) == s1.get(8, j));
		}
		
		// Now try doing a row operation.
		s = new SparseMatrix(s1);
		s.rowOperation(6, 4, 5);
		for (int j = 0 ; j < s.getNCols() ; ++j)
		{
			assertTrue(s.get(0, j) == s1.get(0, j));
			assertTrue(s.get(1, j) == s1.get(1, j));
			assertTrue(s.get(2, j) == s1.get(2, j));
			assertTrue(s.get(3, j) == s1.get(3, j));
			assertTrue(s.get(4, j) == s1.get(4, j) + s1.get(6, j) * 5);
			assertTrue(s.get(5, j) == s1.get(5, j));
			assertTrue(s.get(6, j) == s1.get(6, j));
			assertTrue(s.get(7, j) == s1.get(7, j));
			assertTrue(s.get(8, j) == s1.get(8, j));
		}
		
		// These operations should zero the matrix completely.
		s = new SparseMatrix(s1);
		s.rowOperation(4, 4, -1);
		s.rowOperation(6, 6, -1);
		Matrix zero = new SparseMatrix(9, 7);
		assertTrue(s.equals(zero));
		
		// Check that the elements have been removed from the representation.
		List<Matrix.Element> l = new ArrayList<>();
		for (Matrix.Element e : s)
		{
			l.add(e);
		}
		assertTrue(l.size() == 0);
		
		// Some operations to test the cases where one or the other or both
		// of the rows are not represented.
	}

}
