/*
 *  Trait for a reversable flow.
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

/** A flow with the additional property that it can be "reversed", and therefore
 *  the predecessor nodes of a given node can be discovered by taking the successor
 *  nodes on the reversed flow.
 *  
 *  @type E The Node type.
 */
trait ReversibleFlow[E, EdgeType <: Flow.Edge[E]] extends Flow[E, EdgeType] {

  /** Gets the flow that is the reverse of this one. */
  def reverse:ReversibleFlow[E, EdgeType]
  
}