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

package com.eigenvektor.matrix.solvers;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.eigenvektor.matrix.FullMatrix;
import com.eigenvektor.matrix.Matrices;
import com.eigenvektor.matrix.Matrix;
import com.eigenvektor.matrix.SparseMatrix;

public class TestLUDecomposer
{
	
	private static final int SIZE = 10;
	
	FullMatrix f1 = Matrices.fullIdentity(SIZE);
	SparseMatrix s1 = Matrices.sparseIdentity(SIZE);

	@Before
	public void setUp() throws Exception
	{
		// Create a random matrix.
		Random r = new Random(827885);
		for (int j = 0 ; j < SIZE ; ++j)
		{
			for (int k = 0 ; k < SIZE ; ++k)
			{
				f1.set(j,  k, r.nextDouble());
			}
		}
		
		// Throw some random entries in the sparse matrix.
		s1.set(1, 0, 0.5);
		s1.set(1, 2, 3.7);
		s1.set(0, 2, 0.1);
	}

	@After
	public void tearDown() throws Exception
	{
	}

	@Test
	public void testFull()
	{
		LUDecomposer lud = new LUDecomposer(new FullMatrix(f1));
		Matrix l = lud.getL();
		Matrix u = lud.getU();
		
		// Check that l is lower triangular.
		for (Matrix.Element e : l)
		{
			if (e.getRow() < e.getCol())
			{
				assertTrue(e.getValue() == 0);
			}
		}
		
		// Check that u is upper triangular.
		for (Matrix.Element e : u)
		{
			if (e.getRow() > e.getCol())
			{
				assertTrue(Math.abs(e.getValue()) < 1e-10); // Ok, small.
			}
		}
		
		// Check the product.
		Matrix m = l.multiply(u);
		assertTrue(Matrices.areClose(m, f1, 1e-10));
	}
	
	@Test
	public void testSparse()
	{
		LUDecomposer lud = new LUDecomposer(new SparseMatrix(s1));
		Matrix l = lud.getL();
		Matrix u = lud.getU();
		
		// Check that l is lower triangular.
		for (Matrix.Element e : l)
		{
			if (e.getRow() < e.getCol())
			{
				assertTrue(e.getValue() == 0);
			}
		}
		
		// Check that u is upper triangular.
		for (Matrix.Element e : u)
		{
			if (e.getRow() > e.getCol())
			{
				assertTrue(Math.abs(e.getValue()) < 1e-10); // Ok, small.
			}
		}
		
		// Check the product.
		Matrix m = l.multiply(u);
		assertTrue(Matrices.areClose(m, s1, 1e-10));
	}

}
