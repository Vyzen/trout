/*
 *  Trait for a tree.
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

import Tree.TreeEdge

/**
 * A trait for all trees.
 */
trait Tree[E] extends Flow[E, TreeEdge[E]] {
  
  /** The root of the tree */
  def root:E

  /** The roots of the tree.  There is only one. */
  def roots = Set(root)
  
  /** The children of the tree. */
  def children:List[Tree[E]]
  
  /** Tells if this is a leaf */
  def isLeaf:Boolean = children.isEmpty
  
  /** Applies a function to each node in preorder
   *  
   *  @param f the function to apply.
   */
  def preorderForeach(f: E => Unit):Unit = {
    f(root)
    children.foreach(_.preorderForeach(f))
  }
  
  /** Applies a function to each node in preorder
   *  
   *  @param f the function to apply.
   */
  def postorderForeach(f: E => Unit):Unit = {
    children.foreach(_.postorderForeach(f))
    f(root)
  }
  
  /** Adds another tree to this as the first subtree */
  def +(t:Tree[E]):Tree[E]
  
  override def toString() = {
    if (isLeaf) root.toString()
    else {
      val ret = new StringBuilder()
      ret append root
      ret append ":"
      ret append children.mkString("[", ",", "]")
      ret.toString
  	}
  }
}

object Tree {
  
  /** An edge for this type of class */
  class TreeEdge[E](val from:E, val to:E) extends Equals with Flow.Edge[E] {
    
    lazy val reverse = new TreeEdge[E](to, from)
    
    def canEqual(that: Any) = that.isInstanceOf[TreeEdge[E]]
    
    override def equals(that:Any) = {
      if (!(that.isInstanceOf[TreeEdge[E]])) false
      else {
        val e = that.asInstanceOf[TreeEdge[E]]
        e.canEqual(this) && e.from == this.from && e.to == this.to
      }
    }
    
    override def toString = {
      val buf = new StringBuilder()
      buf append "("
      buf append from.toString()
      buf append ", "
      buf append to.toString()
      buf append ")"
      buf.toString
    }
  }
  
  /** Creates a tree with kids */
  def apply[E](root:E, kids:Tree[E]*) = new DefaultTree(root, kids.toList)
  
  /** Unapply for patter matching */
  def unapply[E](t:Tree[E]):Option[(E, List[Tree[E]])] = Some(t.root, t.children)
}