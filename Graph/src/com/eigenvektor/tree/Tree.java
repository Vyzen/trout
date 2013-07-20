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
	 * Tells if an object is a node of this tree.
	 * 
	 * @param x The object to consider.
	 * @return true iff <i>x</i> is in the tree.
	 */
	public boolean isNode(final T x);
	
	/**
	 * Gets the weight of a node.
	 * 
	 * @param x The node to consider.
	 * @return The weight of <i>x</i>.
	 */
	public double getWeight(final T x);
	
	/**
	 * Gets the parent of a node.
	 * 
	 * @param x The node to consider.
	 * @return The parent of <i>x</i>, or <i>null</i> if <i>x</i> is the root.
	 */
	public T getParent(final T x);
	
	/**
	 * Gets the children of a node.
	 * 
	 * @param x
	 * @return The children of <i>x</i>.  If the children's order is important to the 
	 * implementation, returns an ordered collection.  Return value is not necessarily
	 * mutable.
	 */
	Collection<T> getChildren(final T x);

	
	/**
	 * Gets an iterator that traverses the tree depth first.  If the children of nodes are ordered, their
	 * subtrees are traversed in order.
	 * 
	 * @return An iterator that does that iteration in depth first order.
	 */
	Iterator<T> getDepthFirstIterator();
	
	/**
	 * Gets an iterator that traverses the tree breadth first.  If the children of nodes are ordered, they
	 * are traversed in order.
	 * 
	 * @return An iterator that does that iteration in breadth first order.
	 */
	Iterator<T> getBreadthFirstIterator();
}
