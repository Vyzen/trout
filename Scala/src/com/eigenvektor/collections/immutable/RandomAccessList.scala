/*
 *  Random Access list.
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

package com.eigenvektor.collections.immutable

import com.eigenvektor.collections.immutable.RandomAccessList.CompleteBinaryTree

/** Implementation of a random access list
 *  
 *  This class is a list that supports constant-time head, tail and cons 
 *  operations like a traditional linked list, but also supports access to
 *  arbitrary elements in the array in O(log(n)) time instead of the usual
 *  O(n) time for normal linked lists.
 *  
 *  This is principally taken from Chris Okasaki's paper, "Purely Functional 
 *  Random-Access Lists".
 */
final class RandomAccessList[+A] private (private val trees:List[CompleteBinaryTree[A]]) {
  
  import com.eigenvektor.collections.immutable.RandomAccessList.Leaf
  import com.eigenvektor.collections.immutable.RandomAccessList.Node
  import com.eigenvektor.collections.immutable.RandomAccessList.treeLookup
  import com.eigenvektor.collections.immutable.RandomAccessList.treeUpdate
  
  import scala.collection.mutable.ListBuffer	
  
  /** Gets the head of the list. */
  def head = trees.head.value
  
  /** Tells if the list is empty. */
  def isEmpty = trees.isEmpty
  
  /** The size of the list */
  lazy val size = trees.map(_.size).sum
  
  /** Prepends a value to the list. */
  def cons[B >: A](value:B) = {
    trees match {
      // If we're empty, make a singleton.
      case Nil => new RandomAccessList[B](Leaf(value) :: Nil)
      
      // If we're a singleton, make a double.
      case tree :: Nil => new RandomAccessList[B](Leaf(value) :: trees)
      
      case t1 :: t2 :: tail => 
        // If the first two trees aren't the same size, we don't need to do anything special.
        if (t1.size != t2.size) new RandomAccessList[B](Leaf(value) :: trees)
        
        // If they are, we combine them with the new value.
        else new RandomAccessList[B](Node(value, t1, t2) :: tail)
    }
  }
  
  /** Prepends a value to the list. */
  def ::[B >: A](value:B) = cons(value)
  
  /** Gets the tail of the list. */
  def tail = {
    trees match {
      // If we're empty, there is no tail.
      case Nil => throw new IllegalStateException("Empty list has no tail.")
      
      // If the first tree is a singleton, the tail is just the rest.
      case Leaf(value) :: rest => new RandomAccessList(rest)
      
      // If the first tree is composite, prepend the subtrees to the rest.
      case Node(value, left, right) :: rest => new RandomAccessList(left :: right :: rest)
    }
  }
  
  /** Gets value of the list at a given index. */
  def apply(idx:Int):A = lookup(trees, idx)
  
  /** Performs a lookup of an index in a list of trees. */
  private def lookup[B >: A](remainingTrees:List[CompleteBinaryTree[B]], remainingIdx:Int):B = {
    if (remainingTrees == Nil) throw new IndexOutOfBoundsException("Index out of bounds")
    else if (remainingIdx < remainingTrees.head.size) treeLookup(remainingTrees.head, remainingIdx)
    else lookup(remainingTrees.tail, remainingIdx - remainingTrees.head.size)
  }
  
  /** Updates the value at a given index.  Returns a new list with the updated element. */
  def updated[B >: A](idx:Int, value:B):RandomAccessList[B] = new RandomAccessList[B](
    getUpdatedTrees(new ListBuffer[CompleteBinaryTree[B]], trees, idx, value))

  /** Gets the updated list of trees for an update. */
  private def getUpdatedTrees[B >: A](
      prevTrees:ListBuffer[CompleteBinaryTree[B]],  // The trees before the current position.
      nextTrees:List[CompleteBinaryTree[B]],        // The trees at or after the current position.
      remainingIdx:Int,                             // The index remaining after being shifted to the current position.
      value:B                                       // The value to update.
      ):List[CompleteBinaryTree[B]] = {
    if (nextTrees == Nil) throw new IndexOutOfBoundsException("Index out of bounds")
    else if (remainingIdx < nextTrees.head.size) {
      val newTree = treeUpdate(nextTrees.head, remainingIdx, value)
      prevTrees.toList ::: newTree :: nextTrees.tail
    }
    else {
      prevTrees += nextTrees.head
      getUpdatedTrees(prevTrees, nextTrees.tail, remainingIdx - nextTrees.head.size, value)
    }
  }
  
  override def equals(o:Any) = {
    if (!o.isInstanceOf[RandomAccessList[A]]) false
    else {
      val ral = o.asInstanceOf[RandomAccessList[A]]
      ral.trees.equals(this.trees)
    }
  }
  
  /** Simple toString for debugging. */
  override def toString = trees.map(_.size).mkString("RAList(", ",", ")")
  
}

object RandomAccessList {
  
  // Because I'm making my own RandomAccessList Nil here.
  import scala.collection.immutable.{Nil => NilList}
  
  /** An implementation of a complete binary tree, to be used inside the
   *  general random access list.
   */
  sealed private abstract class CompleteBinaryTree[+T] (val value:T) {
    def size:Int
  }
  private case class Leaf[+T] (v:T) extends CompleteBinaryTree[T](v) {
    val size = 1;
  }
  private case class Node[+T] (v:T, 
    val left:CompleteBinaryTree[T], 
    val right:CompleteBinaryTree[T]) extends CompleteBinaryTree[T](v) {
    require(left.size == right.size)
    val size = left.size * 2 + 1
  }
  
  /** Finds the element of a complete binary tree at a given index in log(n) time */
  private def treeLookup[T](cbt:CompleteBinaryTree[T], idx:Int):T = {
    cbt match {
      case l: Leaf[T] => if (idx == 0) l.value else throw new IndexOutOfBoundsException("index out of bounds")
      case n: Node[T] => if (idx == 0) n.value else {
        val sz = n.left.size
        if (idx <= sz) treeLookup(n.left, idx-1)
        else treeLookup(n.right, idx - 1 - sz)
      }
    }
  }
  
  /** Creates a binary tree that is just like an existing one, but updated at a given index with a given value
   *  in log(n) time, reusing as much of the structure as possible.
   */
  private def treeUpdate[T, U >: T](cbt:CompleteBinaryTree[T], idx:Int, value:U):CompleteBinaryTree[U] = {
    cbt match {
      case l: Leaf[T] => if (idx == 0) new Leaf(value) else throw new IndexOutOfBoundsException("index out of bounds")
      case n: Node[T] => if (idx == 0) new Node(value, n.left, n.right) else
      {
        val sz = n.left.size
        if (idx <= sz) new Node(n.value, treeUpdate(n.left, idx-1, value), n.right)
        else new Node(n.value, n.left, treeUpdate(n.right, idx - 1 - sz, value))
      }
    }
  }
  
  /** A simple iterator for a complete binary tree that iterates in pre-order */
  private final class CBTIterator[T] (cbt:CompleteBinaryTree[T]) extends Iterator[T] {
    
    // The list of subtrees on the current path
    private var trees:List[CompleteBinaryTree[T]] = cbt :: NilList
    
    /** Tells if there are more values */
    def hasNext = !trees.isEmpty
    
    /** Gets the next value */
    def next() = {
      val ret = trees.head.value
      
      // Advance the list to the next value.
      trees = trees.head match {
        case n: Node[T] => {
          n.left :: n.right :: trees.tail
        }
        case Leaf(value) => trees.tail
      }
      ret
    }
  }

  /** Creates a new, empty instance. */
  def apply[T]() = new RandomAccessList[T](NilList)
  
  /** Creates a new instance with elements passed in. */
  def apply[T](elems:T*) = elems.reverse.foldLeft(new RandomAccessList[T](NilList))(_.cons(_))
  
  val Nil = new RandomAccessList[Nothing](NilList)
}