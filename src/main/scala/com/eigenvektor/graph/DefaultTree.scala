/*
 *  Default implementation of a tree.
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

import com.eigenvektor.graph.Tree.TreeEdge

//import com.eigenvektor.graph.DefaultTree.DefaultTreeEdge

/** Default implementation of a tree */
final class DefaultTree[E](val root:E, val children:List[Tree[E]]) extends Tree[E] {
  type EdgeType = TreeEdge[E]
  
  /** Convenience constructor for a singleton list */
  def this(root:E) = this(root, List())
  
  /** Gets the neighbours of a given node.  This can involve a traverse of the entire tree
   *  so might be slow.
   *  
   *  @param x the node to get the neighbours of.
   *  @return the neighbours of [[x]]
   */
  def getNeighbours(x:E):List[EdgeType] = {
    /** Tells if a given tree matches the root */
    def matchesRoot(t:Tree[E]):Option[Tree[E]] = {
      if (t.root == x) Some(t)
      else None
    }
    
    /** Finds the subtree with a given root. */
    def findSubtreeWithRoot(t:Tree[E]):Option[Tree[E]] = {
     if (matchesRoot(t) == None)
     {
       val withRoots = t.children.map(findSubtreeWithRoot(_))
       if (withRoots.isEmpty) None
       else withRoots.head
     }
     else {
       Some(t)
     }
    }
    
    val opt = findSubtreeWithRoot(this)
    opt match {
      case Some(t:Tree[E]) => t.children.map(y => new EdgeType(x, y.root));
      case None => throw new NoSuchElementException()
    }
  }

  /** Adds another tree to this as a subtree */
  def +(t:Tree[E]) = {new DefaultTree[E](root, t :: children)}
}


