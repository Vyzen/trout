/*
 *  An implementation of a priority queue based on the binomial heap.
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

package com.eigenvektor.priorityqueue

/** A fully-persistent min-priority queue implemented using a binomial heap.
 *  
 *  @type E The type of element in the queue
 *  @type P The type of priority in the queue
 */
final class PriorityQueue[E, P] private (private val heap:SkewBinomialHeap[Pair[E, P]]) {

  /** Creates a new, empty, priority queue */
  def this(order:Ordering[P]) = this(new SkewBinomialHeap[Pair[E,P]](Ordering.by((x:Pair[E,P]) => x._2)(order)))
  
  /** Inserts an element into this
   *
   *  @param element The element to add.
   *  @param priority The priority of the new element.
   *  @return The queue with <code>element</code> inserted at priority <code>priority</code>
   */
  def insert(element:E, priority:P) = new PriorityQueue[E,P](this.heap + (element, priority))
  
  /** Inserts an element-priority pair */
  def +(element:Pair[E, P]) = insert(element._1, element._2)
  
  /** Gets the next element of the queue without removing it. */
  lazy val nextElement = heap.min._1
  
  /** Gets the next priority of the queue */
  lazy val nextPriority = heap.min._2
  
  /** Convenience method that gets the queue without returning the element as well */
  lazy val nextQueue = new PriorityQueue(heap.removeMin._2)
  
  /** Removes and returns the next element of the queue
   *  
   *  @return a triple consisting of the next element, its priority, and the queue after
   *  that element is removed.
   */
  lazy val pop = {
    val ((element, priority), newHeap) = heap.removeMin
    (element, priority, new PriorityQueue(newHeap))
  }
  
  /** The size of the queue */
  lazy val size = heap.size
  
  /** Whether the queue is empty */
  lazy val isEmpty = heap.isEmpty
  
}