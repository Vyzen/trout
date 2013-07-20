package com.eigenvektor.tree;

import java.util.Iterator;
import java.util.Queue;

class BreadthFirstIterator<T> implements Iterator<T> {
	
	private final Tree<T> tree;
	private final Queue<T> q;
	
	/**
	 * Creates a new Breadth first tree iterator.
	 * 
	 * @param tree The tree to traverse.
	 * @param q A Queue to put subtrees in as you traverse. In general, you should pass an empty queue.
	 */
	public BreadthFirstIterator(final Tree<T> tree, final Queue<T> q)
	{
		this.tree = tree;
		this.q = q;
		this.q.add(tree.getRoot());
	}

	@Override
	public boolean hasNext() {
		return !q.isEmpty();
	}

	@Override
	public T next() {
		T nextNode = q.remove();
		
		// Before returning the tree root, add its children to the queue.
		for (T x : this.tree.getChildren(nextNode))
		{
			this.q.add(x);
		}
		
		return nextNode;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException("remove() not supported for this iterator.");
	}

}
