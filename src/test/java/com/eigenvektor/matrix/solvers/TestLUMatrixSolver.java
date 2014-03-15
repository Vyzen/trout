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

public class TestLUMatrixSolver
{

	private static final int SIZE = 500;
	private static final int NUM_COLS = 3;
	
	FullMatrix a = Matrices.fullIdentity(SIZE);
	FullMatrix b = new FullMatrix(SIZE, NUM_COLS);
	
	@Before
	public void setUp() throws Exception
	{
		// Create a random matrix.
		Random r = new Random(8226371);
		for (int j = 0; j < SIZE; ++j)
		{
			for (int k = 0; k < SIZE; ++k)
			{
				a.set(j, k, r.nextDouble());
			}
		}
		
		for (int j = 0; j < SIZE; ++j)
		{
			for (int k = 0; k < NUM_COLS; ++k)
			{
				b.set(j, k, r.nextDouble());
			}
		}
	}

	@After
	public void tearDown() throws Exception
	{
	}

	@Test
	public void test()
	{
		MatrixSolver solver = new LUMatrixSolver(a);
		Matrix x = solver.solve(b);
		
		assertTrue(Matrices.areClose(a.multiply(x), b, 1e-9));
	}

}
