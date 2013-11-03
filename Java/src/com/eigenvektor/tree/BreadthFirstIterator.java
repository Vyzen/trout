/*
 *  Iterator that traverses a tree in Breadth-first order.
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
