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
  
  it should "remove a bunch of numbers without failing" in {
    var heap = new SkewBinomialHeap[Int](Ordering.Int)
    for (j <- 0 to 1000) { heap = heap + j }
    
    val builder = new ListBuffer[Int]
    for (j <- 0 to 1000) {
    	val (x, h) = heap.removeMin
    	heap = h;
    	builder += x
    }
    
    assert(heap.size == 0)
    assert(heap.isEmpty)
    assert (builder.toList == (0 to 1000).toList)
  }
  
  it should "be persistent during removal" in {
    var heap = new SkewBinomialHeap[Int](Ordering.Int)
    for (j <- 0 to 1000) { heap = heap + j }
    
    val builder = new ListBuffer[SkewBinomialHeap[Int]]
    for (j <- 0 to 1000) {
    	val (x, h) = heap.removeMin
    	builder += h
    	heap = h
    }
    
    assert (heap.size == 0)
    assert (heap.isEmpty)
    assert (builder.toList.map(_.size) == (0 to 1000).toList.reverse)
  }
  
  it should "order random data" in {
    val r = new scala.util.Random(1337)
    val l = List.fill(1000)(r.nextInt)
    
    var heap = new SkewBinomialHeap[Int](Ordering.Int)
    heap = heap ++ l
    
    val sorted = new ListBuffer[Int]
    while(!heap.isEmpty) {
      val (x, h) = heap.removeMin
      sorted += x
      heap = h
    }
    
    for (pair <- sorted.toList.sliding(2))
    {
      assert(pair(1) >= pair(0))
    }
  }
  
  it should "handle duplicates" in {
    var heap = new SkewBinomialHeap[Int](Ordering.Int)
    heap = heap + 6
    heap = heap + 6
    heap = heap + 6
    heap = heap + 6
    heap = heap + 6
    heap = heap + 6
    
    assert(heap.min == 6)
    assert(heap.size == 6)
    
    {
    val (x, h) = heap.removeMin
    assert(h.min == 6)
    assert(h.size == 5)
    heap = h;
    }
    
    {
    val (x, h) = heap.removeMin
    assert(h.min == 6)
    assert(h.size == 4)
    heap = h;
    }
    
    {
    val (x, h) = heap.removeMin
    assert(h.min == 6)
    assert(h.size == 3)
    heap = h;
    }
    
    {
    val (x, h) = heap.removeMin
    assert(h.min == 6)
    assert(h.size == 2)
    heap = h;
    }
    
    {
    val (x, h) = heap.removeMin
    assert(h.min == 6)
    assert(h.size == 1)
    heap = h;
    }
    
    {
    val (x, h) = heap.removeMin
    assert(h.size == 0)
    heap = h;
    }
  }
  
}