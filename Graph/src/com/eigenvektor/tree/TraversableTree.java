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

	
}
