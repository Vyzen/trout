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

class TestDiGraph extends FlatSpec {
  
  "DiGraph" should "initalize to empty" in {
    val g = DiGraph[String]()()
    assert (g.numNodes == 0)
    assert (g.numEdges == 0)
  }
  
  it should "contain only nodes" in {
    val g = DiGraph[String]("One", "Two", "Three")()
    assert (g.numNodes == 3)
    assert (g.numEdges == 0)
  }
  
  it should "contain two edges" in {
    val g = DiGraph[String]("One", "Two", "Three")("One"->"Two", "One"->"Three")
    assert (g.numNodes == 3)
    assert (g.numEdges == 2)
    
    assert (g.getNeighbours("One").map(_.to) == Set("Two", "Three"))
    assert (g.getNeighbours("Two").map(_.to) == Set())
    assert (g.getNeighbours("Three").map(_.to) == Set())
  }
  
  it should "reverse correctly" in {
    val g = DiGraph[String]("One", "Two", "Three")("One"->"Two", "One"->"Three")
    val gr = g.reverse
    assert (gr.numNodes == 3)
    assert (gr.numEdges == 2)
    
    assert (gr.getNeighbours("One").map(_.to) == Set())
    assert (gr.getNeighbours("Two").map(_.to) == Set("One"))
    assert (gr.getNeighbours("Three").map(_.to) == Set("One"))
  }
  
  it should "fail to create" in {
    intercept[NoSuchElementException] {
    	val g = DiGraph[String]("One", "Two", "Three")("One"->"Two", "One"->"Three", "Five"->"Three")
    }
  }
  
  it should "add nodes correctly" in {
    val g = DiGraph[String]("One", "Two", "Three")("One"->"Two", "One"->"Three")
    assert (g.numNodes == 3)
    assert (g.numEdges == 2)
    
    val h = g + "Four"
    assert (h.numNodes == 4)
    assert (h.numEdges == 2)
    
    assert (h.getNeighbours("Four").map(_.to) == Set())
    assert (h.getNeighbours("One").map(_.to) == Set("Two", "Three"))
    assert (h.getNeighbours("Two").map(_.to) == Set())
    assert (h.getNeighbours("Three").map(_.to) == Set())
  }
  
  it should "add edges correctly" in {
    val g = DiGraph[String]("One", "Two", "Three")("One"->"Two", "One"->"Three")
    assert (g.numNodes == 3)
    assert (g.numEdges == 2)
    
    val h = g + "Four" + new DiGraph.DiGraphEdge("Four", "One")
    assert (h.numNodes == 4)
    assert (h.numEdges == 3)
    
    assert (h.getNeighbours("Four").map(_.to) == Set("One"))
    assert (h.getNeighbours("One").map(_.to) == Set("Two", "Three"))
    assert (h.getNeighbours("Two").map(_.to) == Set())
    assert (h.getNeighbours("Three").map(_.to) == Set())
  }
  
  "WeightedDiGraph" should "construct correctly" in {
    val g = DiGraph.getWeighted[String]("One", "Two", "Three")(("One", "Two", 5), ("One", "Three", 10))
    assert (g.numNodes == 3)
    assert (g.numEdges == 2)
  }
    
  it should "add nodes correctly" in {
    val g = DiGraph.getWeighted[String]("One", "Two", "Three")(("One", "Two", 5), ("One", "Three", 10))
    assert (g.numNodes == 3)
    assert (g.numEdges == 2)
    
    val h = g + "Four"
    assert (h.numNodes == 4)
    assert (h.numEdges == 2)
    
    assert (h.getNeighbours("Four").map(_.to) == Set())
    assert (h.getNeighbours("One").map(_.to) == Set("Two", "Three"))
    assert (h.getNeighbours("Two").map(_.to) == Set())
    assert (h.getNeighbours("Three").map(_.to) == Set())
    
    
    assert (h.getNeighbours("One").map(_.weight) == Set(5, 10))
  }
  
  it should "add edges correctly" in {
    val g = DiGraph.getWeighted[String]("One", "Two", "Three")(("One", "Two", 5), ("One", "Three", 10))
    assert (g.numNodes == 3)
    assert (g.numEdges == 2)
    
    val h = g + "Four" + new DiGraph.WeightedDiGraphEdge("Four", "One", 8)
    assert (h.numNodes == 4)
    assert (h.numEdges == 3)
    
    assert (h.getNeighbours("Four").map(_.to) == Set("One"))
    assert (h.getNeighbours("One").map(_.to) == Set("Two", "Three"))
    assert (h.getNeighbours("Two").map(_.to) == Set())
    assert (h.getNeighbours("Three").map(_.to) == Set())
    
    assert (h.getNeighbours("One").map(_.weight) == Set(5, 10))
    assert (h.getNeighbours("Four").map(_.weight) == Set(8))
  }

}