/*
 *  Copyright (C) 2014 Michael Thorsley
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

package com.eigenvektor.collections

import org.scalatest.FlatSpec
import scala.collection.mutable.ListBuffer

class TestRandomAccessQueue extends FlatSpec{

  "RandomAccessQueue" should "initialize to empty" in {
    val q = RandomAccessQueue.empty
    assert(q.size == 0)
  }
  
  it should "add correctly" in {
    var q:RandomAccessQueue[Int] = RandomAccessQueue.empty
    for (x <- 1 to 10) {
      q = q + x
    }
    assert(q.size == 10)
  }
  
  it should "remove in order" in {
    var q:RandomAccessQueue[Int] = RandomAccessQueue.empty
    for (x <- 1 to 10) {
      q = q + x
    }
    
    val lb = new ListBuffer[Int]
    while (!q.isEmpty) {
      val (elem, q2) = q.removeFirst
      lb += elem
      q = q2
    }
    assert(lb.toList == (1 to 10))
  }
  
  it should "remove in order in segments" in {
    var q:RandomAccessQueue[Int] = RandomAccessQueue.empty
    for (x <- 1 to 10) {
      q = q + x
    }
    
    val lb = new ListBuffer[Int]
    while (q.size > 5) {
      val (elem, q2) = q.removeFirst
      lb += elem
      q = q2
    }
    
    for (x <- 11 to 20) {
      q = q + x
    }
    
    while (!q.isEmpty) {
      val (elem, q2) = q.removeFirst
      lb += elem
      q = q2
    }
    assert(lb.toList == (1 to 20))
  }
  
  it should "access elements correctly" in {
    var q:RandomAccessQueue[Int] = RandomAccessQueue.empty
    for (x <- 1 to 10) {
      q = q + x
    }
    
    for (idx <- 0 to 9) {
      assert(q(idx) == idx + 1)
    }
  }
  
  it should "update elements correctly" in {
    var q:RandomAccessQueue[Int] = RandomAccessQueue.empty
    for (x <- 1 to 10) {
      q = q + x
    }
    
    for (idx <- 0 to 9) {
      q = q.updated(idx, 100 + idx)
    }
    
    for (idx <- 0 to 9) {
      assert(q(idx) == 100 + idx)
    }
  }
  
  it should "do foreach() correctly" in {
    var q:RandomAccessQueue[Int] = RandomAccessQueue.empty
    for (i <- 1 to 100) {
      q = q + i
    }
    
    var sum:Int = 0
    q.foreach{ sum += _}
    assert(sum == 5050)
  }
  
  it should "do foreach() correctly on an empty queue" in {
    var q:RandomAccessQueue[Int] = RandomAccessQueue.empty

    var sum:Int = 0
    q.foreach{ sum += _}
    assert(sum == 0)
  }
  
  it should "do filter() correctly" in {
    var q:RandomAccessQueue[Int] = RandomAccessQueue.empty
    for (i <- 1 to 100) {
      q = q + i
    }
    
    q = q.filter(_ % 2 == 0)
    assert(q.size == 50)
    assert(q.head == 2)
  }
  
  it should "do map() correctly" in {
    var q:RandomAccessQueue[Int] = RandomAccessQueue.empty
    for (i <- 1 to 100) {
      q = q + i
    }
    
    val q2:RandomAccessQueue[Int] = q.map(x => x*x)
    assert(q2.size == 100)
    for (i <- 1 to 100) {
      assert(q2(i-1) == i*i)
    }
  }
  
  it should "do silly for loops correctly" in {
    var q:RandomAccessQueue[Int] = RandomAccessQueue.empty
    for (i <- 1 to 100) {
      q = q + i
    }
    
    val q2:RandomAccessQueue[Int] = for (e <- q) yield e*e 
    assert(q2.size == 100)
    for (i <- 1 to 100) {
      assert(q2(i-1) == i*i)
    }
    
    val q3:RandomAccessQueue[Int] = for (e <- q if e <= 50) yield e + 100
    assert(q3.size == 50)
    assert(q3(49) == 150)
  }
}