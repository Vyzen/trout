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

package com.eigenvektor.collections.immutable

import org.scalatest.FlatSpec
import scala.collection.mutable.ListBuffer

class TestRandomAccessList extends FlatSpec {
  
  "RandomAccessList" should "initialize to empty" in {
    val l = RandomAccessList[Int]()
    assert(l.size == 0)
  }
  
  it should "cons correctly" in {
    var l = RandomAccessList[Int]()
    for (i <- 1 to 1000) {
      l = i :: l
      assert(l.size == i)
      assert(l.head == i)
    }
  }
  
  it should "tail correctly" in {
    var l = RandomAccessList[Int]()
    for (i <- 1 to 1000) {
      l = i :: l
    }
    
    for (i <- 1 to 1000) {
      assert(l.size + i == 1001)
      assert(l.head + i == 1001)
      l = l.tail
    }
  }
  
  it should "be persistent during adds" in {
    val builder = new ListBuffer[RandomAccessList[Int]]
    var l = RandomAccessList[Int]
    builder += l
    for (i <- 1 to 1000) {
      l = i :: l
      builder += l
    }
    
    assert(l.size == 1000)
    assert(builder.toList.map(_.size) == (0 to 1000).toList)
  }
  
  it should "be persistent during tails" in {
    var l = RandomAccessList[Int]()
    for (i <- 1 to 1000) {
      l = i :: l
    }
    
    val builder = new ListBuffer[RandomAccessList[Int]]
    builder += l
    for (i <- 1 to 1000) {
      l = l.tail
      builder += l
    }
    
    assert (l.size == 0)
    assert(builder.toList.map(_.size) == (0 to 1000).toList.reverse)
  }

}