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
}