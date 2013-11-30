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

package com.eigenvektor.priorityqueue

import org.scalatest.FlatSpec

final class TestHeap extends FlatSpec {

  "Heap" should "construct with implicit ordering" in {
    var hInt:Heap[Int] = Heap[Int]
    var hString:Heap[String] = Heap[String]
    
    for (j:Int <- 1 to 10) {
      hInt = hInt + j;
      hString = hString + ("Trout"+j)
    }
    
    assert(hInt.size == 10)
    assert(hInt.min == 1)
    
    assert(hString.size == 10)
    assert(hString.min == "Trout1")
  }
  
}