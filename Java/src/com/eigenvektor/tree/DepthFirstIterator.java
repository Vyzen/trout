package com.eigenvektor.tree;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * An iterator that iterates through a tree depth first.
 * 
 * @param <T> The type of the tree.
 */
class DepthFirstIterator<T> implements Iterator<T> 
{

	private final Tree<T> tree;
	private final List<T> s = new LinkedList<T>();
	
	public DepthFirstIterator(final Tree<T> tree)
	{
		this.tree = tree;
		s.add(0, tree.getRoot());
	}
	
	@Override
	public boolean hasNext() {
		return !s.isEmpty();
	}

	@Override
	public T next() {
		if (s.isEmpty()) { throw new NoSuchElementException("Out of elements"); }
		
		T next = this.s.get(0);
		this.s.remove(0);
		int idx = 0;
		for (T x : tree.getChildren(next))
		{
			this.s.add(idx, x);
			idx++;  // this is to ensure subtrees go into the stack in order, if the tree
					// provides ordered subtrees.
		}
		
		return next;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException("remove() not supported for this iterator.");
	}

}
