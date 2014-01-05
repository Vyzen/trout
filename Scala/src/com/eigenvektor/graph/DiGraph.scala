/*
 *  Trait for a directed graph.
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

import com.eigenvektor.graph.DiGraph.DiGraphEdge

/** Specification for a directed graph, which differs from a [[Flow]] in that all of
 *  the nodes are known in advance. 
 *  
 *  Extends PartialFunction as a function from its nodes to the neighbours of those nodes.
 * 
 */
trait DiGraph[E, EdgeType <: DiGraphEdge[E]] extends ReversibleFlow[E, EdgeType] {
  
  /** Gets the nodes of the graph */
  def nodes:Set[E]
  
  /** Gets the number of nodes in the graph */
  def numNodes = nodes.size

  /** Gets the number of edges in the graph */
  def numEdges = nodes.foldLeft(0)(_ + this.getNeighbours(_).size)
  
  /** Override getNeighbours so it returns a Set, rather than a generic Iterable */
  def getNeighbours(x:E):Set[EdgeType]
  
  /** Adds a node to this */
  def +(x:E):DiGraph[E, EdgeType]
  
  /** Adds a directed edge to this */
  def +(e:EdgeType):DiGraph[E, EdgeType]
}

/** Companion object for DiGraph */
object DiGraph {
  
  class DiGraphEdge[E](val from:E, val to:E) extends Equals with Flow.Edge[E] {
    
    lazy val reverse:DiGraphEdge[E] = new DiGraphEdge(to, from)
    
    def canEqual(that: Any) = that.isInstanceOf[DiGraphEdge[E]]
    
    override def equals(that:Any) = {
      if (!(that.isInstanceOf[DiGraphEdge[E]])) false
      else {
        val e = that.asInstanceOf[DiGraphEdge[E]]
        e.canEqual(this) && e.from == this.from && e.to == this.to
      }
    }
    
    override def toString = {
      val buf = new StringBuilder()
      buf append "("
      buf append from.toString()
      buf append ", "
      buf append to.toString()
      buf append ")"
      buf.toString
    }
  }
  
  /** Create a graph with given nodes and edges */
  def apply[E](nodes:E*)(edges:Pair[E,E]*) = {
    edges.foldLeft(nodes.foldLeft(AdjacencySetDiGraph[E, DiGraphEdge[E]]())(_+_))(
        (g, p) => g + new DiGraphEdge(p._1, p._2))
  }
  
}