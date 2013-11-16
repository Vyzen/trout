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

import scala.collection.mutable.ListBuffer

/**
 * Implementation of a binomial heap.
 */
final class BinomialHeap[T] private (private val trees:List[BinomialTree[T]], private val order:Ordering[T]) {

  /** Secondary constructor to make an empty heap
   *  
   *   @param order The ordering to use for this heap
    */
  def this(order:Ordering[T]) = this(Nil, order)
  
  /** Secondary constructor to make a single-entry heap
   *
   *  @param x the single entry for the heap.
   *  @param order the ordering for this heap.
   */
  def this(x:T, order:Ordering[T]) = this(List(new BinomialTree(x)), order)
  
  /** Gets the size of this queue */
  val size = trees.foldLeft(0)((x,y) => x + y.size)
  
  /** Merge this heap with another.
   * 
   * @param other the other heap to merge.
   * @return a Binomial heap that combines <code>this</code> with <code>other</code>
   */
  def merge(other:BinomialHeap[T]) = {
    require(other.order == this.order, "Orderings must match in merged trees")
    
    var left = trees;
    var right = other.trees;
    var carry:Option[BinomialTree[T]] = None;
    
    val builder = new ListBuffer[BinomialTree[T]]
    while (left != Nil && right != Nil) {
      
      // Branch based on whether we have a carry.
      carry match {
        case None => {
          // fold in the smaller tree
          if (left.head.size < right.head.size) {
            builder += left.head
            left = left.tail
          }
          else if (right.head.size < left.head.size) {
            builder += right.head
            right = right.tail
          }
          else {
            carry = Some(mergeTrees(left.head, right.head))
            left = left.tail
            right = right.tail
          }
        }
        case Some(c) => {
          if (c.size < left.head.size && c.size < right.head.size) {
            // If the carry is the smallest, append it.
            builder += c
            carry = None
          }
          else if (c.size < right.head.size) {
            // The carry now merges the left and its current.
            carry = Some(mergeTrees(c, left.head))
            left = left.tail;
          }
          else if (c.size < left.head.size) {
            // The carry now merges the right and its current.
            carry = Some(mergeTrees(c, right.head))
            right = right.tail;
          }
          else
          {
            // All three trees are the same size.  Retain one (arbitrarily the left)
            // and merge the other into the carry.
            builder += left.head
            carry = Some(mergeTrees(c, right.head))
            left = left.tail;
            right = right.tail;
          }
        }
      }
    }
    
    // Handle any remaining trees.
    var remainder = if (left == Nil) { right } else { left }
    while (remainder != Nil)
    {
      carry match
      {
        case None => { 
          builder += remainder.head
          remainder = remainder.tail
          }
        case Some(c) if (c.size == remainder.head.size) => {
          carry = Some(mergeTrees(c, remainder.head));
          remainder = remainder.tail;
        }
        case Some(c) if (c.size > remainder.head.size) => {
          builder += remainder.head
          remainder = remainder.tail;
        }
        case Some(c) if (c.size < remainder.head.size) => {
          builder += c
          carry = None;
        }
      }
    }
    
    // Finally, if we have a leftover carry, append it.
    builder ++= carry;
    
    // Create the new.
    new BinomialHeap[T](builder.toList, order)
  }
  
  private def mergeTrees(x:BinomialTree[T], y:BinomialTree[T]) = {
    // Merge the one with a larger head into the one with a smaller head.
    if (order.lt(x.value, y.value)) { x.merge(y) } else { y.merge(x) }
  }
  
  /** Gets the minimum element of the heap */
  def min = trees.map(_.value).min(order)
  
  /** Adds an element to the heap.
   * 
   * @param x the element to add.
   */
  def +(x:T) = merge(new BinomialHeap[T](x, order))
  
  /** Removes the min element from the heap.
   *  
   *  Returns a pair with the removed min as its first element, and the
   *  heap without that min as its second.
   */
  def removeMin = {
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