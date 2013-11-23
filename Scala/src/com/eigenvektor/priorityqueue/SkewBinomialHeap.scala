/*
 *  An implementation of a skew heap.
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

/** Implementation of s skew binomial tree with values. */
final class SkewBinomialTree[T] private (val value:T, val subtrees:List[SkewBinomialTree[T]], val rank:Int) {
  
  /** Convenience constructor for singleton instance. */
  def this(value:T) = this(value, Nil, 0);
  
  /** Non-skew link.  This tree is taken to be new root. */
  def link(other:SkewBinomialTree[T]) = {
    require(other.rank == this.rank, "other must have the same rank as this. other.rank = " + other.rank + " this.rank = " + this.rank)
    new SkewBinomialTree(value, other :: subtrees, rank + 1)
  }
  
  /** Type A skew link.  The singleton is the new root. */
  def skewLinkA(other:SkewBinomialTree[T], single:T)  = {
    require(other.rank == this.rank, "other must have the same rank as this.")
    new SkewBinomialTree(single, this :: other :: Nil, rank + 1)
  }
  
  /** Type B skew link.  This tree is the new root. */
  def skewLinkB(other:SkewBinomialTree[T], single:T)  = {
    require(other.rank == this.rank, "other must have the same rank as this.")
    new SkewBinomialTree(value, new SkewBinomialTree[T](single) :: other :: subtrees, rank + 1)
  }
  
  /** The size of the tree. */
  lazy val size:Int = 1 + subtrees.foldLeft(0)((x,y) => x + y.size)
  
  /** Simple toString.  Useful for debugging */
  override def toString() = "[Rank: " + rank + "]"
}

/** Implementation of a skew binomial heap */
final class SkewBinomialHeap[T] private (private val trees:List[SkewBinomialTree[T]], private val order:Ordering[T]){

  /** Constructor for an empty heap */
  def this(order:Ordering[T]) = this(Nil, order)
  
  /** The size of the heap */
  lazy val size = trees.foldLeft(0)((x,y) => x + y.size)
  
  /** Tells if this is empty */
  val isEmpty = trees == Nil;
  
  /** Merges another heap with this. */
  def merge(other:SkewBinomialHeap[T]) = {
    require(other.order == this.order, "Orderings must match in merged trees")
    
    /** Inserts a single tree into a list of trees. */
    def ins(tree:SkewBinomialTree[T], list:List[SkewBinomialTree[T]]):List[SkewBinomialTree[T]] = {
      list match {
        case Nil => List(tree)
        case head::tail => if (tree.rank < head.rank) tree :: list else {
          if (order.lt(head.value, tree.value)) ins(head.link(tree), tail)
          else ins(tree.link(head), tail)
        }
      }
    }
    
    /** Makes the lowest rank unique */
    def uniqify(list:List[SkewBinomialTree[T]]) = if (list == Nil) Nil else ins(list.head, list.tail)
    
    /** Merge lists of tress that are known to have unique lowest rank */
    def mergeUnique(left:List[SkewBinomialTree[T]], right:List[SkewBinomialTree[T]]):List[SkewBinomialTree[T]] = {
      (left, right) match {
        case (_, Nil) => left
        case (Nil, _) => right
        case (lhead::ltail, rhead::rtail) => {
          if (lhead.rank < rhead.rank) lhead :: mergeUnique(ltail, right)
          else if (rhead.rank < lhead.rank) rhead :: mergeUnique(left, rtail)
          else {
            if (order.lt(lhead.value, rhead.value)) ins(lhead.link(rhead), mergeUnique(ltail, rtail))
            else ins(rhead.link(lhead), mergeUnique(ltail, rtail))
          }
        }
      }
    }
    
    new SkewBinomialHeap(mergeUnique(uniqify(trees), uniqify(other.trees)), order)
  }
  
  /** Inserts an element into this.
   *  
   *  @param x the element to add.
   */
  def +(x:T) = {
	  
    /** Performs the correct skew link of two trees and a singleton based on the specified order
     *  If the singleton is minimal,  */
    def skewLink(t1:SkewBinomialTree[T], t2:SkewBinomialTree[T], x:T) = {
      if (order.lt(x, t1.value) && order.lt(x, t2.value)) {
        t1.skewLinkA(t2, x);
      }
      else if (order.lt(t1.value, t2.value)) {
        t1.skewLinkB(t2, x)
      }
      else {
        t2.skewLinkB(t1, x)
      }
    }
    
    trees match {
      case h1 :: h2 :: tail if (h1.rank == h2.rank) => 
        new SkewBinomialHeap(skewLink(h1, h2, x) :: tail, order) 
      case _ =>
        new SkewBinomialHeap(new SkewBinomialTree(x) :: trees, order)
    }
        
  }
  
  /** Adds a sequence of elements to the heap.
   *  
   *  @param t the elements to add.
   */
  def ++(t:Traversable[T]) = {
    var ret = this;
    for (x <- t) { ret = ret + x }
    ret
  }
  
  /** Gets the minimum element of the heap */
  lazy val min = {
    require(!isEmpty, "Empty heap has no min.")
    trees.map(_.value).min(order)
  }
  
  /** Removes the minimum value from the heap. */
  lazy val removeMin = {
	require(!isEmpty, "Can't remove from empty heap")
    
    // Make a heap from the list of trees without the tree that has the min for its root.
    val (before, after) = trees.span(_.value != min)
    val bhWithout = new SkewBinomialHeap[T](before ::: after.tail, order)
    
    // Partition the kids of the new element into the rank 0 and rank non-zero ones.
    val removedTree = after.head;
	val (zeros, nonZeros) = removedTree.subtrees.partition(_.rank == 0)
	
	// The non zero kids for a valid help themselves, so just merge it.
	val partMerged = bhWithout.merge(new SkewBinomialHeap(nonZeros.reverse, order))
	
	// Merge in the zero kids one at a time.
	val merged = zeros.foldLeft(partMerged)((heap, tree) => heap + tree.value)
	
	(removedTree.value, merged)
  }
  
  /** Simple toString that just shows the orders of the trees */
  override def toString() = trees.map(_.rank).mkString("<", ", ", ">")
}