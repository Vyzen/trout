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

class TestRAList extends FlatSpec {
  
  "RandomAccessList" should "initialize to empty" in {
    val l = RandomAccessList[Int]
    assert(l.size == 0)
  }
  
  it should "cons correctly" in {
    var l:RandomAccessList[Int] = RandomAccessList.Nil
    for (i <- 1 to 1000) {
      l = i :: l
      assert(l.size == i)
      assert(l.head == i)
    }
  }
  
  it should "tail correctly" in {
    var l:RandomAccessList[Int] = RandomAccessList.Nil
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
    var l:RandomAccessList[Int] = RandomAccessList.Nil
    builder += l
    for (i <- 1 to 1000) {
      l = i :: l
      builder += l
    }
    
    assert(l.size == 1000)
    assert(builder.toList.map(_.size) == (0 to 1000).toList)
  }
  
  it should "be persistent during tails" in {
    var l:RandomAccessList[Int] = RandomAccessList.Nil
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
  
  it should "reconstruct the original list" in {
    var l:RandomAccessList[Int] = RandomAccessList.Nil
    for (i <- 1 to 1000) {
      l = i :: l
    }
    
    var recover:List[Int] = Nil
    for (i <- 0 to 999) {
      recover = l(i) :: recover
    }
    
    assert(recover == (1 to 1000))
  }
  
  it should "update the list" in {
    var l:RandomAccessList[Int] = RandomAccessList.Nil
    for (i <- 1 to 1000) {
      l = i :: l
    }
    
    for (i <- 0 to 999) {
      l = l.updated(i, i + 10000)
    }
    
    var recover:List[Int] = Nil
    for (i <- 0 to 999) {
      recover = l(i) :: recover
    }
    
    assert(recover.reverse == (10000 to 10999))
  }
  
  it should "do equals() correctly" in {
    var l1 = RandomAccessList[Int]
    for (i <- 1 to 1000) {
      l1 = i :: l1
    }
    
    var l2 = RandomAccessList[Int]
    for (i <- 1 to 1000) {
      l2 = i :: l2
    }
    
    var l3 = RandomAccessList[Int]
    for (i <- 1 to 1000) {
      l3 = (i*5) :: l3
    }
    
    assert(l1 == l2)
    assert(l1 != l3)
    assert(l1 != "A large African Elephant")
  }
  
  it should "initialize with a list" in {
    val l = RandomAccessList(1,2,3,4,5,6,7,8,9)
    assert(l.head == 1)
    assert(l.tail.head == 2)
    assert(l.size == 9)
  }
  
  it should "do foreach() correctly" in {
    var l:RandomAccessList[Int] = RandomAccessList.Nil
    for (i <- 1 to 100) {
      l = i :: l
    }
    
    var sum:Int = 0
    l.iterator.foreach{ sum += _}
    assert(sum == 5050)
  }
  
  it should "do foreach() correctly on an empty list" in {
    var l:RandomAccessList[Int] = RandomAccessList.Nil

    var sum:Int = 0
    l.iterator.foreach{ sum += _}
    assert(sum == 0)
  }
  
  it should "do filter() correctly" in {
    var l:RandomAccessList[Int] = RandomAccessList.Nil
    for (i <- 1 to 100) {
      l = i :: l
    }
    
    l = l.filter(_ % 2 == 0)
    assert(l.size == 50)
    assert(l.head == 100)
  }
  
  it should "do map() correctly" in {
    var l:RandomAccessList[Int] = RandomAccessList.Nil
    for (i <- 1 to 100) {
      l = i :: l
    }
    
    val l2:RandomAccessList[Int] = l.map(x => x*x)
    assert(l2.size == 100)
    assert(l2.head == 100 * 100)
  }
  
  it should "do silly for loops correctly" in {
    var l:RandomAccessList[Int] = RandomAccessList.Nil
    for (i <- 1 to 100) {
      l = i :: l
    }
    
    val l2:RandomAccessList[Int] = for (e <- l) yield e*e 
    assert(l2.size == 100)
    assert(l2.head == 100 * 100)
    
    val l3:RandomAccessList[Int] = for (e <- l if e <= 50) yield e + 100
    assert(l3.size == 50)
    assert(l3.head == 150)
  }

}