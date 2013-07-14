package com.eigenvektor.tree;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * A default implementation of a tree.
 *
 * @param <T> The node type of the tree.
 */
public final class DefaultTree<T> extends AbstractTree<T> {
	
	final T root;
	final Map<Tree<T>, Double> subTrees = new HashMap<Tree<T>, Double>();
	
	/**
	 * Creates a tree with a known root.
	 * 
	 * @param root The root of the tree.
	 */
	public DefaultTree(final T root)
	{
		this.root = root;
	}
	
	/**
	 * Adds a subtree with a given weight to the tree.
	 * 
	 * @param tree The subtree to add.
	 * @param weight The weight of the subtree.
	 */
	public void addSubTree(final Tree<T> tree, double weight)
	{
		subTrees.put(tree, weight);
	}

	@Override
	public T getRoot() {
		return this.root;
	}

	@Override
	public int getNumSubTrees() {
		return subTrees.size();
	}

	@Override
	public Collection<Tree<T>> getSubTrees() {
		return Collections.unmodifiableSet(subTrees.keySet());
	}

	@Override
	public double getSubTreeWeight(Tree<T> t) {
		return subTrees.get(t);
	}
	
	/**
	 * A simpler hasCode, also compatable with equals, but cheaper to compute,
	 * and just as good unless you've got a lot of different trees with the same root.
	 */
	public int hashCode() { return root.hashCode(); }

}
