/*
 *  Abstract implementation of Tree interface.
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

package com.eigenvektor.tree;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;

/**
 * An abstract implementation of Tree that provides a semantic equals() and hashCode().  Also iterators and toString().
 *
 * @param <T> The type of tree node.
 */
public abstract class AbstractTree<T> implements Tree<T> {

	@Override
	/**
	 * Provides semantic equality based on equality of the subtrees and weights.  If subtree order
	 * matters to the implementation, this is reflected in the runtime type of the return value
	 * of getSubTrees(), and reflected here as well.
	 */
	public boolean equals(final Object o)
	{
		if (o == this)
		{
			return true;
		}
		
		if (!(o instanceof Tree))
		{
			return false;
		}
		
		@SuppressWarnings("unchecked")
		Tree<T> t = (Tree<T>) o;
		return t.getRoot().equals(this.getRoot()) && equals(t, t.getRoot());
	}
	
	/**
	 * Tells if another tree is equal to <code>this</code> in its subtree of a given node.
	 * 
	 * @param other the second tree.
	 * @param node  The node to compare under.
	 * @return true iff the subtrees of node in other and this are the same.
	 */
	private boolean equals(final Tree<T> other, final T node)
	{
		if (!this.isNode(node)) 
		{
			return false;
		}
		
		// Check that the weight of the node is correct.
		if (this.getWeight(node) != other.getWeight(node))
		{
			return false;
		}
		
		// Check that the children of the node are the same.
		Collection<T> kids = this.getChildren(node);
		if (!this.getChildren(node).equals(other.getChildren(node)))
		{
			return false;
		}
		
		// Finally check that all of the child trees are the same.
		for (T x : kids)
		{
			if (!equals(other, x)) { return false; }
		}
		
		return true;
	}

	/**
	 * A hash code that incorporates all of the tree elements.
	 */
	@Override
	public int hashCode()
	{
		return hashCode(getRoot());
	}
	
	/**
	 * Gets the hash code for the subtree below a given node.
	 * 
	 * @param node The node to get the hash code below.
	 * @return The hash code for the subtree rooted at node.
	 */
	private int hashCode(final T node)
	{
		// Incorporate the current node.
		int hash = node.hashCode();
		Double w = getWeight(node);
		hash ^= w.hashCode();
		
		// Incorporate the subtrees.
		for (T x : getChildren(node))
		{
			hash ^= hashCode(x);
		}
		
		return hash;
	}
	
	@Override
	public Iterator<T> getDepthFirstIterator() {
		return new DepthFirstIterator<T>(this);
	}

	@Override
	public Iterator<T> getBreadthFirstIterator() {
		Queue<T> q = new ArrayDeque<T>();
		return new BreadthFirstIterator<T>(this, q);
	}
	
	@Override
	public String toString()
	{
		StringBuffer buf = new StringBuffer();
		toString(getRoot(), buf);
		return buf.toString();
	}
	
	/**
	 * Writes out a string for  this tree below a subtree.
	 * 
	 * @param node The node to consider.
	 * @param buf A buffer to write into.
	 */
	private void toString(final T node, final StringBuffer buf)
	{
		buf.append(node);
		buf.append(":<");
		for (T x : getChildren(node))
		{
			double w = getWeight(x);
			buf.append("[");
			buf.append(w);
			buf.append("]");
			toString(x, buf);
			buf.append(" ");
		}
		buf.append(">");
	}

}
