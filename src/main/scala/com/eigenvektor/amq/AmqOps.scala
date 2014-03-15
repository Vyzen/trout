/*
 *  Scala wrapper for approx mem queries.
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

package com.eigenvektor.amq

/** A wrapper for scala-fication of ApproxMemQuery instances. */
final class AmqOps[T](private val amq:ApproxMemQuery[T]) {

  /** Determine if ean element is (probably) contained */
  def apply(x:T) = amq.contains(x)
  
  /** Add an object to this amq. */
  def +=(x:T) = { amq.add(x) ; amq }
  
  /** Add a set of elements to this amq. */
  def ++=(x:Traversable[T]) = { x.foreach(amq.add(_)) ; amq}
}