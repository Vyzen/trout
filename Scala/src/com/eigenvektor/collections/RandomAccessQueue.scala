/*
 *  Random Access queue.
 *  Copyright (C) 2014 Michael Thorsley
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

package com.eigenvektor.collections

/** A FIFO queue based on the random access list.
 *  
 *  In addition to O(1) insert, and amortized O(1) removal, this also supports log(n) random access.
 */
final class RandomAccessQueue[+A] private (private val insert:RandomAccessList[A],
    private val remove:RandomAccessList[A]) {
 
  /** Tells if the queue is empty. */
  def isEmpty = insert.isEmpty && remove.isEmpty
  
  /** Gets the size of the queue. */
  def size = insert.size + remove.size
  
  /** Same thing, different name. */
  def length = size;
  
  /** Adds an element to the queue. */
  def +[B >: A](x:B) = new RandomAccessQueue[B](x :: insert, remove)
  
  /** Removes the head element from the queue. */
  def removeFirst = {
    require(!isEmpty, "Can't remove from empty queue")
    
    if (!remove.isEmpty) (remove.head, new RandomAccessQueue[A](insert, remove.tail)) 
    else {
      val newRemove = insert.reverse
      (newRemove.head, new RandomAccessQueue[A](RandomAccessList.Nil, newRemove.tail))
    }
  }
  
  /** Gets an element from the queue. */
  def apply(idx:Int) = {
    require(idx < this.size)
    
    if (idx < remove.size) remove(idx)
    else if (idx - remove.size < insert.size) insert(insert.size - idx - 1 + remove.size)
  }
  
  /** Updates the value at a given index.  Returns a new queue with the updated element. */
  def updated[B >: A](idx:Int, value:B) = {
    if (idx < remove.size) new RandomAccessQueue[B](insert, remove.updated(idx, value))
    else if (idx - remove.size < insert.size) {
      new RandomAccessQueue[B](insert.updated(insert.size - idx - 1 + remove.size, value), remove)
    } else throw new IndexOutOfBoundsException("Index out of bounds")
  }

}

object RandomAccessQueue {
  
  // An empty queue.
  val empty = new RandomAccessQueue(RandomAccessList.Nil, RandomAccessList.Nil)
}