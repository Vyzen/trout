/*
 *  Trait for a general flow
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

import com.eigenvektor.graph.Flow.Edge

/** Specification for a class that is like a directed graph, but does not necessarily know
 *  all of its nodes in advance of "discovering" them by following edges, in the same
 *  way that tree might only know its root initially and discover the remainder of its
 *  nodes through traversal.
 *  
 *  The nodes initially known are returned by the [[roots]] property.
 *  
 *  @type E The node type of the flow.
 */
trait Flow[E, EdgeType <: Edge[E]] {

  /** Gets the roots of the flow.   */
  def roots:Set[E]
  
  /** Gets the neighbours of a given node
   *  
   *  @param x the node to get the neighbours of.
   *  @return the neighbours of [[x]]
   */
  def getNeighbours(x:E):Iterable[EdgeType]
  
}

object Flow {
  
  /** A trait for Edge implementations to implement */
  trait Edge[E] {
    
    /** The node the edge is from. */
    def from:E
    
    /** The node the edge is to. */
    def to:E
    
    /** Gets the reversed version of this edge. */
    def reverse:Edge[E]
  }
}