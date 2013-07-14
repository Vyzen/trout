package com.eigenvektor.tree;

import java.util.Collection;
import java.util.Iterator;

/**
 * Specification for a tree.  The number of children of each node is unspecified.  The edges of the tree
 * are implicitly weighted.
 *
 * @param <T> The node type of the tree.  This in general should have equals() and hashCode() implemented
 * correctly.
 */
public interface Tree<T> {

	/**
	 * Gets the root of the tree.
	 * 
	 * @return The root node of the tree.
	 */
	T getRoot();
	
	/**
	 * Gets the number of subtrees.
	 * 
	 * @return the number of subtrees.
	 */
	int getNumSubTrees();
	
	/**
	 * Gets the subtrees of the tree.
	 * 
	 * @return A collection of subtrees of the tree.  If subtree order is semantically important to this
	 * tree, the collection returned should be ordered, otherwise it need not be.  The returned collection
	 * is not guaranteed to be mutable.
	 */
	Collection<Tree<T>> getSubTrees();
	
	/**
	 * Gets the weight of the edge from the root to the subtree given.
	 * 
	 * @return The weight of said edge.
	 */
	double getSubTreeWeight(final Tree<T> t);
	
	/**
	 * Gets an iterator that traverses the tree depth first.  If the children of nodes are ordered, their
	 * subtrees are traversed in order.
	 * 
	 * @return An iterator that does that iteration.
	 */
	Iterator<T> getDepthFirstIterator();
	
	/**
	 * Gets an iterator that traverses the tree breadth first.  If the children of nodes are ordered, they
	 * are traversed in order.
	 * 
	 * @return An iterator that does that iteration.
	 */
	Iterator<T> getBreadthFirstIterator();
}
