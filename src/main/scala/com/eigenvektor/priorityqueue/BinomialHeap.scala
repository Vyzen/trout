/*
 *  An implementation of a binomial heap.
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

/**
 * Implementation of a binomial heap.
 */
final class BinomialHeap[T] private (private val trees:List[BinomialHeap.BinomialTree[T]], private val order:Ordering[T])
	extends Heap[T] {
  
  /** Gets the size of this heap */
  lazy val size = trees.foldLeft(0)((x,y) => x + y.size)
  
  /** Tells if the heap is empty */
  val isEmpty = trees == Nil
  
  /** Merge this heap with another.
   * 
   * @param other the other heap to merge.
   * @return a Binomial heap that combines <code>this</code> with <code>other</code>
   */
  def merge(other:BinomialHeap[T]) = {
    require(other.order == this.order, "Orderings must match in merged trees")
    
    /** Merges two binomial trees based on the ordering specified for this heap. */
    def mergeTrees(x:BinomialHeap.BinomialTree[T], y:BinomialHeap.BinomialTree[T]) = {
      // Merge the one with a larger head into the one with a smaller head.
      if (order.lt(x.value, y.value)) { x.merge(y) } else { y.merge(x) }
    }
    
    /** Merges lists together with a carry. */
    def mergeLists(
        left:List[BinomialHeap.BinomialTree[T]],
        right:List[BinomialHeap.BinomialTree[T]],
        carry:Option[BinomialHeap.BinomialTree[T]]) : List[BinomialHeap.BinomialTree[T]] =
    {
      // Match on all the cases that might be passed in.
      (left, right, carry) match {
        // If nothing is passed in, return an empty list.
        case (Nil, Nil, None) => Nil
        
        // If only one of the items is passed in, return it.
        case (l, Nil, None) => l
        case (Nil, r, None) => r 
        case (Nil, Nil, Some(c)) => List(c)
        
        // If two lists are specified, merge them.
        case (l, r, None) => {
          if (l.head.size < r.head.size) l.head :: mergeLists(l.tail, r, None)
          else if (r.head.size < l.head.size) r.head :: mergeLists(l, r.tail, None)
          else mergeLists(l.tail, r.tail, Some(mergeTrees(l.head, r.head)))
        }
        
        // If one list and a carry are specified, merge them.
        case (l, Nil, Some(c)) => {
          if (l.head.size < c.size) l.head :: mergeLists(l.tail, Nil, carry) 
          else if (c.size < l.head.size) c :: mergeLists(l, Nil, None)
          else mergeLists(l.tail, Nil, Some(mergeTrees(l.head, c)))
        }
        case (Nil, r, Some(c)) => mergeLists(r, Nil, carry) // Reduce to the other case.
        
        // If all three are specified, do complicated things.
        case (l, r, Some(c)) => {
          // If the carry is smaller than both the others, merge it here.
          if (c.size < left.head.size && c.size < right.head.size) c :: mergeLists(l, r, None)
          // l is equal in size to c.
          else if (c.size < r.head.size) mergeLists(l.tail, r, Some(mergeTrees(l.head, c)))
          // r is equal in size to c
          else if (c.size < l.head.size) mergeLists(l, r.tail, Some(mergeTrees(r.head, c)))
          // all three are equal. Keep the left one, and merge the right into the carry.
          else l.head :: mergeLists(l.tail, r.tail, Some(mergeTrees(r.head, c)))
        }
      }
    }
    
    // Create the new.
    new BinomialHeap[T](mergeLists(trees, other.trees, None), order)
  }
  
  /** Gets the minimum element of the heap */
  lazy val min = {
    require(!isEmpty, "Empty heap has no min.")
    trees.map(_.value).min(order)
  }
  
  /** Adds an element to the heap.
   * 
   * @param x the element to add.
   */
  def +(x:T) = merge(BinomialHeap[T](x, order))
  
  /** Removes the min element from the heap.
   *  
   *  Returns a pair with the removed min as its first element, and the
   *  heap without that min as its second.
   */
  lazy val removeMin = {
    require(!isEmpty, "Can't remove from empty heap")
    
    // Make a heap from the list of trees without the tree that has the min for its root.
    val (before, after) = trees.span(_.value != min)
    val bhWithout = new BinomialHeap[T](before ::: after.drop(1), order)
    
    // Create a heap from the kids of the tree we dropped.
    // (reverse because the tree stores from largest to smallest instead 
    // of smallest to largest like we do.
    val bhKids = new BinomialHeap[T](after.head.subtrees.reverse, order)
    
    (min, bhWithout merge bhKids)
  }
  
  /** Simple toString that just shows the orders of the trees */
  override def toString() = trees.map(_.size).mkString("<", ", ", ">")
  
}

/** Companion object for BinomialHeap class */
object BinomialHeap {


  /** Implementation of a binomial tree with values
   *  
   *  Simple implementation with just an add method.
   */
  private final class BinomialTree[+T](val value:T, val subtrees:List[BinomialTree[T]]) {

    /** Secondary constructor for a single-node binomial tree */
	def this(value:T) = this(value, Nil)

	/** The size of the binomial tree
	 *  
	 *  This is always 2^(number of subtrees)
	 */
	val size = 1 << subtrees.size

	/** Merges another tree with this one.
	 *  
	 *  @param other the tree to merge.
	 *  @throws IllegalArgumentException if other is not the same size as this.
	 */
	def merge[B >: T](other:BinomialTree[B]) = {
	  require(other.size == this.size, "other must have the same size as this.")
	  new BinomialTree[B](value, other :: subtrees)
	}

  }

  /** Create an empty instance 
   *
   *  @param order The ordering to use for this heap  
   */
  def apply[T](order:Ordering[T]) = new BinomialHeap[T](Nil, order) 
  
  /** Create a singleton instance 
   *  
   *  @param x the single entry for the heap.
   *  @param order the ordering for this heap.
   */
  def apply[T](x:T, order:Ordering[T]) = new BinomialHeap[T](List(new BinomialTree(x)), order)
}

