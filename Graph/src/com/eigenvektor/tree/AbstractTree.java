package com.eigenvektor.tree;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;

/**
 * An abstract implemention of Tree that provides a semantic equals() and hashCode().
 *
 * @param <T>
 */
public abstract class AbstractTree<T> implements Tree<T> {

	@Override
	/**
	 * Provides semantic equality based on equality of the subtrees and weights.  If subtree order
	 * matters to the implementation, this is reflected in the runtime type of the return value
	 * of getSubTrees(), and reflected here as well.
	 */
	public boolean equals(Object o)
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
		Collection<Tree<T>> subTrees = getSubTrees();
		if (!subTrees.equals(t.getSubTrees()))
		{
			return false;
		}
		
		for (Tree<T> curTree : subTrees)
		{
			if (this.getSubTreeWeight(curTree) != t.getSubTreeWeight(curTree)) { return false; }
		}
		
		return true;
	}

	/**
	 * A hash code that incorporates all of the tree elements.
	 */
	@Override
	public int hashCode()
	{
		int hash = getRoot().hashCode();
		
		// Fold in the subtrees.
		Collection<Tree<T>> subTrees = getSubTrees();
		hash ^= subTrees.hashCode();
		
		// Fold in the weights.
		for (final Tree<T> t : subTrees)
		{
			Double w = getSubTreeWeight(t);
			hash ^= w.hashCode();
		}
		
		return hash;
	}
	
	@Override
	public Iterator<T> getDepthFirstIterator() {
		return new DepthFirstIterator<T>(this);
	}

	@Override
	public Iterator<T> getBreadthFirstIterator() {
		Queue<Tree<T>> q = new ArrayDeque<Tree<T>>();
		return new BreadthFirstIterator<T>(this, q);
	}

}
