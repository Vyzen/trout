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

/**
 * An implementation of DiGraph that uses adjacency sets.
 * 
 * Main constructor constructs with a map from nodes to a set of neighbours, and the
 * inverse of that map.
 */
final class AdjacencyListDiGraph[E] private (
    private val adj:Map[E, Set[E]], 
    private val invAdj:Map[E, Set[E]]) 
    extends DiGraph[E] {
  
  /** Construct an empty instance */
  def this() = this(Map[E, Set[E]](), Map[E, Set[E]]())
  
  /** The nodes of this graph */
  val nodes = adj.keySet
  
  /** All of the nodes are roots */
  val roots = nodes
  
  /** Gets the neighbours of a node */
  def getNeighbours(x:E) = adj(x)
  
  /** Reverses this DiGraph */
  lazy val reverse = new AdjacencyListDiGraph(invAdj, adj)
  
  /** Adds a node to this */
  def +(x:E) = new AdjacencyListDiGraph(adj + (x -> Set[E]()), invAdj + (x -> Set[E]()))
  
  /** Adds a directed edge to this */
  def +(e:Pair[E,E]) = {
    val newNeighbours = adj(e._1) + e._2
    val newPredecessors = invAdj(e._2) + e._1
    new AdjacencyListDiGraph(adj + (e._1 -> newNeighbours), invAdj + (e._2 -> newPredecessors))
  }

}