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

package com.eigenvektor.collections

import com.eigenvektor.collections.RandomAccessList.CompleteBinaryTree
import scala.collection.SeqLike

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
abstract class RandomAccessList[+A] protected[this] (private val trees:List[CompleteBinaryTree[A]]) 
  extends Seq[A] with SeqLike[A, RandomAccessList[A]] {
  
  import com.eigenvektor.collections.RandomAccessList.Leaf
  import com.eigenvektor.collections.RandomAccessList.Node
  import com.eigenvektor.collections.RandomAccessList.treeLookup
  import com.eigenvektor.collections.RandomAccessList.treeUpdate
  
  import scala.collection.mutable.ListBuffer	
  
  import scala.annotation.tailrec
  
  /** Creates a new instance of this type of random access list
   *  
   *  Subclasses should implement to return an instance of their specific type.
   */
  protected[this] def newInstance[B](trees:List[CompleteBinaryTree[B]]):RandomAccessList[B]
  
  /** Gets the head of the list. */
  override def head = trees.head.value
  
  /** Tells if the list is empty. */
  override def isEmpty = trees.isEmpty
  
  /** The size of the list */
  override lazy val size = trees.map(_.size).sum
  
  /** The size of the list. */
  override lazy val length = size;
  
  /** Prepends a value to the list. */
  def cons[B >: A](value:B) = {
    trees match {
      // If we're empty, make a singleton.
      case Nil => newInstance[B](Leaf(value) :: Nil)
      
      // If we're a singleton, make a double.
      case tree :: Nil => newInstance[B](Leaf(value) :: trees)
      
      case t1 :: t2 :: tail => 
        // If the first two trees aren't the same size, we don't need to do anything special.
        if (t1.size != t2.size) newInstance[B](Leaf(value) :: trees)
        
        // If they are, we combine them with the new value.
        else newInstance[B](Node(value, t1, t2) :: tail)
    }
  }
  
  /** Prepends a value to the list. */
  def ::[B >: A](value:B) = cons(value)
  
  /** Gets the tail of the list. */
  override def tail = {
    trees match {
      // If we're empty, there is no tail.
      case Nil => throw new IllegalStateException("Empty list has no tail.")
      
      // If the first tree is a singleton, the tail is just the rest.
      case Leaf(value) :: rest => newInstance(rest)
      
      // If the first tree is composite, prepend the subtrees to the rest.
      case Node(value, left, right) :: rest => newInstance(left :: right :: rest)
    }
  }
  
  /** Gets value of the list at a given index. */
  def apply(idx:Int):A = {
    
    /** Performs a lookup of an index in a list of trees. */
    @tailrec def lookup[B >: A](remainingTrees:List[CompleteBinaryTree[B]], remainingIdx:Int):B = {
      if (remainingTrees == Nil) throw new IndexOutOfBoundsException("Index out of bounds")
      else if (remainingIdx < remainingTrees.head.size) treeLookup(remainingTrees.head, remainingIdx)
      else lookup(remainingTrees.tail, remainingIdx - remainingTrees.head.size)
    }
    
    lookup(trees, idx)
  }
  
  /** Updates the value at a given index.  Returns a new list with the updated element. */
  def updated[B >: A](idx:Int, value:B):RandomAccessList[B] = {
    
    /** Gets the updated list of trees for an update. */
    @tailrec def getUpdatedTrees[B >: A](
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
    
    newInstance[B](getUpdatedTrees(new ListBuffer[CompleteBinaryTree[B]], trees, idx, value))
  }
  
  /** Reverses the list in O(N) time */
  override def reverse = {
    def reverseInto(a:RandomAccessList[A], b:RandomAccessList[A]):RandomAccessList[A] = {
      if (a.isEmpty) b
      else reverseInto(a.tail, a.head :: b)
    }
    
    reverseInto(this, newInstance(Nil))
  }
  
  def iterator() = {
    if (isEmpty) Iterator.empty
    else {
      /** A simple iterator for this object that assumes that this is non-empty. */
      class RALIterator extends Iterator[A] {
      
        val listIter = trees.iterator
        var treeIter = listIter.next().iterator
      
        def hasNext = treeIter.hasNext || listIter.hasNext
      
        def next() = {
          if (!treeIter.hasNext) {
            treeIter = listIter.next.iterator
          }
    	  treeIter.next()
        }
      
      }
    
      new RALIterator
    }
  }
  
  override def reverseIterator() = {
    if (isEmpty) Iterator.empty
    else {
      /** A simple reverse iterator. */
      class RALReverseIterator extends Iterator[A] {
       
        val listIter = trees.reverse.iterator
        var treeIter = listIter.next().reverseIterator
      
        def hasNext = treeIter.hasNext || listIter.hasNext
      
        def next() = {
          if (!treeIter.hasNext) {
            treeIter = listIter.next.reverseIterator
          }
    	  treeIter.next()
        }
      }
      
      new RALReverseIterator
    }
  }
  
  /** Override of the seq method from Iterable. */
  override def seq = this
  
  /** Creates a builder for this class. */
  override protected[this] def newBuilder = RandomAccessList.newBuilder[A]
  
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
  
  import scala.collection.immutable.{Nil => NilList} // Because I'm making my own RandomAccessList Nil here.
  import scala.collection.mutable.Builder
  import scala.collection.generic.CanBuildFrom
  import scala.annotation.tailrec
  
  /** An implementation of a complete binary tree, to be used inside the
   *  general random access list.
   */
  sealed private[collections] abstract class CompleteBinaryTree[+T] (val value:T) {
    def size:Int
    def iterator:Iterator[T] = new CBTIterator(this)
    def reverseIterator:Iterator[T] = new ReverseCBTIterator(this)
  }
  private[collections] case class Leaf[+T] (v:T) extends CompleteBinaryTree[T](v) {
    val size = 1;
  }
  private[collections] case class Node[+T] (v:T, 
    val left:CompleteBinaryTree[T], 
    val right:CompleteBinaryTree[T]) extends CompleteBinaryTree[T](v) {
    require(left.size == right.size)
    val size = left.size * 2 + 1
  }
  
  /** Finds the element of a complete binary tree at a given index in log(n) time */
  @tailrec private[collections] def treeLookup[T](cbt:CompleteBinaryTree[T], idx:Int):T = {
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
  private[collections] def treeUpdate[T, U >: T](cbt:CompleteBinaryTree[T], idx:Int, value:U):CompleteBinaryTree[U] = {
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
  private[collections] final class CBTIterator[T] (cbt:CompleteBinaryTree[T]) extends Iterator[T] {
    
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
  
  /** A simple iterator for a complete binary tree that iterates in reverse post-order */
  private[collections] final class ReverseCBTIterator[T] (cbt:CompleteBinaryTree[T]) extends Iterator[T] {
    
    // A list of trees combined with a flag to tells us whether we have already visited
    // their left and right subtrees.
    private var trees:List[Tuple3[CompleteBinaryTree[T], Boolean, Boolean]] = (cbt, false, false) :: NilList
    
    // Advance the tree list.
    trees = advanceTrees(trees)
    
    /** Tells if there are more values. */
    def hasNext = !trees.isEmpty
    
    def next() = {
      val ret = trees.head._1.value
      trees = advanceTrees(trees.tail)
      ret
    }
    
    /** Advance the trees to the next position.  I.e. the position where the next node is at the head
     *  head of the list, and can be popped after use.
     */
    @tailrec private def advanceTrees(trees:List[Tuple3[CompleteBinaryTree[T], Boolean, Boolean]])
      :List[Tuple3[CompleteBinaryTree[T], Boolean, Boolean]] = {
      
      // If we have an empty tree list, continue to have one.
      if (trees.isEmpty) trees
      else {
        val head = trees.head
        head match {
          // If the head is a leaf or already visited node, we're already there.
          case (Leaf(_), _ , _) => trees
          case (Node(_, _, _), true, true) => trees
        
          // If it is a completely unvisited node, advance right.
          case (Node(_, _, _), false, false) => {
              val n = head._1.asInstanceOf[Node[T]]
              advanceTrees((n.right, false, false) :: (n, false, true) :: trees.tail)
            }
        
          // If it is visited on the right, but not the left, advance left.
          case (Node(_, _, _), false, true)  => {
              val n = head._1.asInstanceOf[Node[T]]
              advanceTrees((n.left, false, false) :: (n, true, true) :: trees.tail)
            }
        
          // This should not happen.
          case _ => throw new IllegalStateException()
        }
      }
    }
  }

  /** Creates a new, empty instance. */
  def apply[T]():RandomAccessList[T] = new immutable.RandomAccessList[T](NilList)
  
  /** Creates a new instance with elements passed in. */
  def apply[T](elems:T*):RandomAccessList[T] = 
    elems.reverse.foldLeft(new immutable.RandomAccessList[T](NilList):RandomAccessList[T])(_.cons(_))
  
  // A special Nil for this kind of list.
  val Nil = new immutable.RandomAccessList[Nothing](NilList)
  
  /** Creates a builder for this class. */
  def newBuilder[T]: Builder[T, RandomAccessList[T]] = {
    // A very simple builder that just collects its elements into a standard list
    // and creates the RandomAccessList at the last moment.
    class RALBuilder extends Builder[T, RandomAccessList[T]] {
      private var elems:List[T] = NilList
      
      def +=(elem:T) = { 
        elems = (elem :: elems) 
        this
        }
      
      def clear() = elems = NilList
      
      def result():RandomAccessList[T] = 
        elems.foldLeft(new immutable.RandomAccessList[T](NilList):RandomAccessList[T])(_.cons(_))
    }
    
    new RALBuilder
  }
  
  implicit def canBuildFrom[T, U >: T] : CanBuildFrom[RandomAccessList[T], U, RandomAccessList[U]] = 
    new CanBuildFrom[RandomAccessList[T], U, RandomAccessList[U]] {
      def apply():Builder[U, RandomAccessList[U]] = newBuilder[U]
      def apply(from:RandomAccessList[T]) = newBuilder[U]
    }
 
}