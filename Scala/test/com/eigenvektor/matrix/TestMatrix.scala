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

/**
 * A test for the scala-fied matrix stuff.
 */
object TestMatrix {
 
  def main(args:Array[String]) = {
    val f1 = new FullMatrix(3,3)
    val f2 = new FullMatrix(3,3)
    
    for (j <- 0 to 2 ; k <- 0 to 2) 
    { 
      f1.set(j,k,j*k)
      f2.set(j,k,j+k) 
    }
    println(f1);
    println(f2);
    println(f1 + f2 + (f1*f2))
    println(f1 - f1)
    
    f1 += f2;
    println(f1)
    
    val f3  = 5.0 * f2
    println(f3)
    
    println(f3(1,1))
    
    f2(1,1) = 15
  }
}