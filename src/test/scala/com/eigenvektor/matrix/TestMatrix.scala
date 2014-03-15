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

package com.eigenvektor.matrix

import org.scalatest.FlatSpec

/**
 * A test for the scala-fied matrix stuff.
 */
class TestMatrix extends FlatSpec {
 
    // Set up some matrices.
    val f1 = new FullMatrix(3,3)
    val f2 = new FullMatrix(3,3)
    
    for (j <- 0 to 2 ; k <- 0 to 2) 
    { 
      f1.set(j,k,j*k)
      f2.set(j,k,j+k) 
    }
    
    "Matrix" should "sum correctly" in {
      val sum = f1 + f2
      for (j <- 0 to 2 ; k <- 0 to 2)
      {
        assert(sum(j,k) === f1(j,k) + f2(j,k))
      }
    }
    
    it should "scalar multiply correctly" in
    {
      val f3 = 5.0 * f2
      for (j <- 0 to 2 ; k <- 0 to 2)
      {
        assert(f3(j,k) === 5.0 * f2(j,k))
      }
    }
    
    "MutableMatrix" should "assign elements correctly" in 
    {
      f2(1,1) = 15;
      assert(f2(1,1) === 15.0)
    }
}