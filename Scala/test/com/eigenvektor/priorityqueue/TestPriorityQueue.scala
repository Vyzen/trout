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
import scala.collection.mutable.ListBuffer

final class TestPriorityQueue extends FlatSpec {

  "PriorityQueue" should "initailize to empty" in {
    val pq = new PriorityQueue[Any, Int](Ordering.Int)
    assert(pq.size == 0)
    assert(pq.isEmpty)
  }
  
  it should "accept a bunch of elements without failing" in {
    var pq = new PriorityQueue[Any, Int](Ordering.Int)
    for (j <- 0 to 1000) { pq = pq + ("Trout" + j, j) }
    assert (pq.size == 1001)
    assert (pq.nextElement == "Trout0")
    assert (pq.nextPriority == 0)
  }
  
  it should "remove a bunch of elements without failing" in {
    var pq = new PriorityQueue[Int, Int](Ordering.Int)
    for (j <- 0 to 1000) { pq = pq + (j + 1000,  j) }
    
    val elements = new ListBuffer[Int]
    val priorities  = new ListBuffer[Int]
    for (j <- 0 to 1000) {
      elements += pq.nextElement
      priorities += pq.nextPriority
      pq = pq.nextQueue
    }
    
    assert(pq.size == 0)
    assert(pq.isEmpty)
    assert (priorities.toList == (0 to 1000).toList)
    assert (elements.toList == (1000 to 2000).toList)
  }
  
  it should "order random data" in {
    val r = new scala.util.Random(1337)
    val l = List.fill(1000000)(r.nextInt)
    
    var pq = new PriorityQueue[Int, Int](Ordering.Int)
    for (x <- l) { pq = pq + (x,x) }
    
    val sorted = new ListBuffer[Int]
    while(!pq.isEmpty) {
      sorted += pq.nextElement
      pq = pq.nextQueue
    }
    
    for (pair <- sorted.toList.sliding(2))
    {
      assert(pair(1) >= pair(0))
    }
  }
  
}