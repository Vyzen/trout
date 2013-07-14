package com.eigenvektor.tree;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

class DepthFirstIterator<T> implements Iterator<T> 
{

	private final Tree<T> tree;
	List<Tree<T>> s = new LinkedList<Tree<T>>();
	
	DepthFirstIterator(final Tree<T> tree)
	{
		this.tree = tree;
		s.add(0, this.tree);
	}
	
	@Override
	public boolean hasNext() {
		return !s.isEmpty();
	}

	@Override
	public T next() {
		if (s.isEmpty()) { throw new NoSuchElementException("Out of elements"); }
		
		Tree<T> next = this.s.get(0);
		int idx = 0;
		for (Tree<T> subTree : next.getSubTrees())
		{
			this.s.add(idx, subTree);
			idx++;  // this is to ensure subtrees go into the stack in order, if the tree
					// provides ordered subtrees.
		}
		
		return next.getRoot();
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException("remove() not supported for this iterator.");
	}

}
