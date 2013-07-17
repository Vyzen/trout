package com.eigenvektor.tree;

import java.util.Collection;

/**
 * Specification for a tree that's a little more "node-based" than Tree.  In particular,
 * all elements can be accessed at random rather than just the root.
 * 
 * @param <T> The node type of the tree.  This in general should have equals() and hashCode() implemented
 * correctly.
 */
public interface TraversableTree<T> extends Tree<T> {

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
}
