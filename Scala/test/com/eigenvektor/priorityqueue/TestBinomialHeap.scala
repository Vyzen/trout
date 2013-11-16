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

final class TestPriorityQueue extends FlatSpec {
  
  "BinomialHeap" should "initailize to empty" in {
    val heap = new BinomialHeap[Int](Ordering.Int)
    assert(heap.size == 0)
  }
  
  it should "initailize to singleton" in {
    val heap = new BinomialHeap[Int](5, Ordering.Int)
    assert(heap.size == 1)
  }
  
  it should "merge empties to get empty" in  {
    val heap1 = new BinomialHeap[Int](Ordering.Int)
    val heap2 = new BinomialHeap[Int](Ordering.Int)
    val heap3 = heap1 merge heap2
    assert(heap3.size == 0)
  }
  
  it should "merge an empty with a singleton to get a singleton" in  {
    val heap1 = new BinomialHeap[Int](Ordering.Int)
    val heap2 = new BinomialHeap[Int](5, Ordering.Int)
    
    val heap3 = heap1 merge heap2
    val heap4 = heap2 merge heap1
    
    assert (heap3.size == 1)
    assert (heap4.size == 1)
  }
  
  it should "merge two singletons into a heap of size two" in   {
    val heap1 = new BinomialHeap[Int](3, Ordering.Int)
    val heap2 = new BinomialHeap[Int](5, Ordering.Int)
    
    val heap3 = heap1 merge heap2
    val heap4 = heap2 merge heap1
    
    assert(heap3.size == 2)
    assert(heap4.size == 2)
  }

}