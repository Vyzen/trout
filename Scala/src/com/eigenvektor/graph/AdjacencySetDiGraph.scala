/*
 *  Adjacency set backed graph implementation
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

/**
 * An implementation of DiGraph that uses adjacency sets.
 * 
 * Main constructor constructs with a map from nodes to a set of neighbours, and the
 * inverse of that map.
 */
final class AdjacencySetDiGraph[E, EdgeType <: DiGraphEdge[E]] private (
    private val adj:Map[E, Set[EdgeType]],
    private val invAdj:Map[E, Set[EdgeType]]) extends DiGraph[E, EdgeType] {
  
  /** The nodes of this graph */
  def nodes = adj.keySet
  
  /** All of the nodes are roots */
  def roots = nodes
  
  /** Gets the neighbours of a node */
  def getNeighbours(x:E) = adj(x)
  
  /** Reverses this DiGraph */
  lazy val reverse = new AdjacencySetDiGraph[E, EdgeType](invAdj, adj)

  
  /** Adds a node to this */
  def +(x:E) = new AdjacencySetDiGraph[E, EdgeType](this.adj + (x -> Set[EdgeType]()), 
      this.invAdj + (x -> Set[EdgeType]()))

  
  /** Adds a directed edge to this */
  def +(e:EdgeType) = {
    val newNeighbours = adj(e.from) + e
    val newPredecessors = invAdj(e.to) + e.reverse.asInstanceOf[EdgeType]
    new AdjacencySetDiGraph[E, EdgeType](this.adj + (e.from -> newNeighbours), this.invAdj + (e.to -> newPredecessors))
  }
  
}

object AdjacencySetDiGraph {
  
  /** Construct empty */
  def apply[E, EdgeType <: DiGraphEdge[E]]() = new AdjacencySetDiGraph[E, EdgeType](Map[E, Set[EdgeType]](), Map[E, Set[EdgeType]]())
}