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

package com.eigenvektor.graph

import org.scalatest.FlatSpec
import scala.collection.mutable.ListBuffer

final class TestTree extends FlatSpec {
  
  val testTree = Tree(1, Tree(2, Tree(3) :: Tree(4) :: Tree(5) :: Nil ) :: Tree(6) :: Nil)
  
  "Tree" should "initialize to a leaf" in {
    val t = Tree(5)
    
    assert(t.isLeaf)
  }
  
  it should "pre-order traverse" in {
    val builder:ListBuffer[Int] = new ListBuffer
    
    testTree.preorderForeach(builder.append(_))
    
    val l = builder.toList
    assert(l == 1 :: 2 :: 3 :: 4 :: 5 :: 6 :: Nil)
  }
  
  it should "post-order traverse" in {
    val builder:ListBuffer[Int] = new ListBuffer
    
    testTree.postorderForeach(builder.append(_))
    
    val l = builder.toList
    assert(l == 3 :: 4 :: 5 :: 2 :: 6 :: 1 :: Nil)
  }
  
  it should "find children of 2" in {
    val kids = testTree.getNeighbours(2)
    assert (kids == List(3,4,5))
  }
  
  it should "pattern match" in 
  {
    val passed = testTree match {
      case Tree(5, _) => false
      case Tree(1, Tree(2, _) :: Tree(25, _) :: _) => false
      case Tree(1, Tree(2, _) :: _) => true
      case _ => false
    }
    assert(passed)
  }

}