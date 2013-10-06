package com.eigenvektor.matrix;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestFullMatrix
{

	private FullMatrix m1 = new FullMatrix(3,5);
	private FullMatrix m2 = new FullMatrix(1,5);
	private FullMatrix m3 = new FullMatrix(5,1);
	
	@Before
	public void setUp() throws Exception
	{
		for (int j = 0 ; j < 3 ; ++j)
		{
			for (int k = 0 ; k < 5 ; ++k)
			{
				m1.set(j, k, j + 5*k);
			}
		}
		
		for (int j = 0 ; j < 5 ; ++j)
		{
			m2.set(0, j, j*3);
			m3.set(j, 0, j*5-1);
		}
	}

	@After
	public void tearDown() throws Exception
	{
	}

	@Test
	public void test()
	{
		// Size tests.
		assertTrue(m1.getNRows() == 3);
		assertTrue(m1.getNCols() == 5);
		assertTrue(m2.getNRows() == 1);
		assertTrue(m2.getNCols() == 5);
		assertTrue(m3.getNRows() == 5);
		assertTrue(m3.getNCols() == 1);
		
		// Test a copy.
		FullMatrix m1Copy = new FullMatrix(m1);
		assertTrue(m1Copy.equals(m1));
		assertTrue(m1Copy.hashCode() == m1.hashCode());
		
		// Test a scalar multiply.
		Matrix m2Times5 = m2.multiply(5);
		assertTrue(m2Times5.getNRows() == 1);
		assertTrue(m2Times5.getNCols() == 5);
		for (int j = 0 ; j < 5 ; ++j)
		{
			assertTrue(m2Times5.get(0, j) == m2.get(0, j) * 5.0);
		}
		
		// Test a matrix multiply.
		Matrix p = m1.multiply(m3);
		assertTrue(p.getNRows() == 3);
		assertTrue(p.getNCols() == 1);

		// Do them out manually as a sanity check.
		assertTrue(p.get(0, 0) ==
				m1.get(0, 0) * m3.get(0, 0) +
				m1.get(0, 1) * m3.get(1, 0) +
				m1.get(0, 2) * m3.get(2, 0) +
				m1.get(0, 3) * m3.get(3, 0) +
				m1.get(0, 4) * m3.get(4, 0));
		assertTrue(p.get(1, 0) ==
				m1.get(1, 0) * m3.get(0, 0) +
				m1.get(1, 1) * m3.get(1, 0) +
				m1.get(1, 2) * m3.get(2, 0) +
				m1.get(1, 3) * m3.get(3, 0) +
				m1.get(1, 4) * m3.get(4, 0));
		assertTrue(p.get(2, 0) ==
				m1.get(2, 0) * m3.get(0, 0) +
				m1.get(2, 1) * m3.get(1, 0) +
				m1.get(2, 2) * m3.get(2, 0) +
				m1.get(2, 3) * m3.get(3, 0) +
				m1.get(2, 4) * m3.get(4, 0));
	}

	@Test
	public void testIterator()
	{
		List<Matrix.Element> l = new ArrayList<Matrix.Element>();
		for (Matrix.Element x : m1)
		{
			l.add(x);
		}
		assertTrue(l.size() == 15);
	}

	@Test
	public void testRowOperations()
	{
		// Make a copy of m1 to play with.
		FullMatrix m = new FullMatrix(m1);
		// Try a scaling.
		m.scaleRow(1, 7);
		for (int j = 0 ; j < m.getNCols() ; ++j)
		{
			assertTrue(m.get(0, j) == m1.get(0, j));
			assertTrue(m.get(1, j) == m1.get(1, j)*7.0);
			assertTrue(m.get(2, j) == m1.get(2, j));
		}
		
		// Now try swapping rows.
		m = new FullMatrix(m1);
		m.swapRows(1, 2);
		for (int j = 0 ; j < m.getNCols() ; ++j)
		{
			assertTrue(m.get(0, j) == m1.get(0, j));
			assertTrue(m.get(1, j) == m1.get(2, j));
			assertTrue(m.get(2, j) == m1.get(1, j));
		}
		
		// Now try doing a row operation.
		m = new FullMatrix(m1);
		m.rowOperation(1, 2, 5);
		for (int j = 0 ; j < m.getNCols() ; ++j)
		{
			assertTrue(m.get(0, j) == m1.get(0, j));
			assertTrue(m.get(1, j) == m1.get(1, j));
			assertTrue(m.get(2, j) == m1.get(2, j) + m1.get(1, j)*5);
		}
	}
}
