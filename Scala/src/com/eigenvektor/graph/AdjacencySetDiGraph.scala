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
final class AdjacencySetDiGraph[E] extends DiGraph[E] {
  
  type EdgeType = DiGraphEdge[E]
  
  // These are vars only to allow construction of derived graphs quickly
  // They are not externally visible.
  private var adj = Map[E, Set[EdgeType]]()
  private var invAdj = Map[E, Set[EdgeType]]()
  
  /** The nodes of this graph */
  def nodes = adj.keySet
  
  /** All of the nodes are roots */
  def roots = nodes
  
  /** Gets the neighbours of a node */
  def getNeighbours(x:E) = adj(x)
  
  /** Reverses this DiGraph */
  lazy val reverse = {
    val ret = new AdjacencySetDiGraph[E]()
    ret.adj = this.invAdj
    ret.invAdj = this.adj
    ret
  }
  
  /** Adds a node to this */
  def +(x:E) = {
    val ret = new AdjacencySetDiGraph[E]
    ret.adj = this.adj + (x -> Set[EdgeType]())
    ret.invAdj = this.invAdj + (x -> Set[EdgeType]())
    ret
  }
  
  /** Adds a directed edge to this */
  def +(e:EdgeType) = {
    val newNeighbours = adj(e.from) + e
    val newPredecessors = invAdj(e.to) + e.reverse
    val ret = new AdjacencySetDiGraph[E]
    ret.adj = this.adj + (e.from -> newNeighbours)
    ret.invAdj = this.invAdj + (e.to -> newPredecessors)
    ret
  }
  
  /** Adds a directed edge to this */
  def +(e:Pair[E,E]) = this + new EdgeType(e._1, e._2)

}