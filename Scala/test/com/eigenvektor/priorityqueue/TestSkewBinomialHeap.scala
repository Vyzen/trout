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

final class TestSkewBinomialHeap extends FlatSpec {

  "SkewBinomialHeap" should "initailize to empty" in {
    val heap = new SkewBinomialHeap[Int](Ordering.Int)
    assert(heap.size == 0)
    assert(heap.isEmpty)
  }
  
  it should "merge empties to get empty" in  {
    val heap1 = new SkewBinomialHeap[Int](Ordering.Int)
    val heap2 = new SkewBinomialHeap[Int](Ordering.Int)
    val heap3 = heap1 merge heap2
    assert(heap3.size == 0)
    assert(heap3.isEmpty)
  }
  
  it should "accept a bunch of numbers without failing" in {
    var heap = new SkewBinomialHeap[Int](Ordering.Int)
    for (j <- 0 to 1000) { heap = heap + j }
    assert (heap.size == 1001)
    assert (heap.min == 0)
  }
  
  it should "be persistent during adds" in {
    val builder = new ListBuffer[SkewBinomialHeap[Int]]
    var heap = new SkewBinomialHeap[Int](Ordering.Int)
    for (j <- 0 to 1000) {
      heap = heap + j;
      builder += heap;
    }
    
    // The sizes should be 0 to 1000
    val sizes = builder.toList.map(_.size)
    for (x <- sizes.zipWithIndex) {
      assert(x._1 == x._2+1)
    }
  }
  
  it should "remove from a singleton to get an empty" in {
    val heap1 = new SkewBinomialHeap(Ordering.Int) + 5
    val (num, heap2) = heap1.removeMin;
    
    assert(num == 5)
    assert(heap2.size == 0)
    assert(heap2.isEmpty)
  }
  
}