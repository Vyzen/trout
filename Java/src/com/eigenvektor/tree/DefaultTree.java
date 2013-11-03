/*
 *  Default implementation of a tree.
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A default implementation of a tree.
 *
 * @param <T> The node type of the tree.
 */
public final class DefaultTree<T> extends AbstractTree<T> {
	
	/**
	 * Representation of a tree node.  This contains the element's parent, its weight
	 * and its children.
	 * 
	 * @param <T> The node type.
	 */
	private static class TreeNode<T>
	{
		private final T parent;
		private final double weight;
		private List<T> children = new ArrayList<T>();
		
		public TreeNode(final T parent, final double weight)
		{
			this.parent = parent;
			this.weight = weight;
		}

		/**
		 * The parent of this node.
		 * 
		 * @return the parent.
		 */
		public T getParent() {
			return parent;
		}
		
		/**
		 * Gets the weight of this node.
		 * 
		 * @return the weight.
		 */
		public double getWeight() {
			return weight;
		}

		/**
		 * Gets all the children.
		 * 
		 * @return the children.
		 */
		public List<T> getChildren() {
			return children;
		}


		/**
		 * Adds a child to this node.
		 * 
		 * @param child the child
		 */
		public void addChild(final T child)
		{
			this.children.add(child);
		}
		
	}
	
	// The elements.  A map from the element itself to the node record.
	private final Map<T, TreeNode<T>> elements = new HashMap<T, TreeNode<T>>();
	private final T root;
	
	/**
	 * Creates a tree with a known root.
	 * 
	 * @param root The root of the tree.
	 */
	public DefaultTree(final T root)
	{
		this.root = root;
		elements.put(root, new TreeNode<T>(null, 0.0));
	}
	
	public void addNode(final T node, final T parent, final double weight)
	{
		if (!isNode(parent))
		{
			throw new IllegalArgumentException("Parent is not in the tree.");
		}
		
		if (node == null)
		{
			throw new NullPointerException("Null nodes not supported.");
		}
		
		if (isNode(node))
		{
			throw new IllegalArgumentException("Node is already in the tree.");
		}
		
		// Add a TreeNode for the new node.
		elements.put(node, new TreeNode<T>(parent, weight));
		
		// Record it for the parent as well.
		elements.get(parent).addChild(node);
	}
	
	
	@Override
	public T getRoot() {
		return this.root;
	}

	@Override
	public boolean isNode(T x) {
		return elements.containsKey(x);
	}

	@Override
	public double getWeight(T x) {
		return elements.get(x).getWeight();
	}

	@Override
	public T getParent(T x) {
		return elements.get(x).getParent();
	}

	@Override
	public Collection<T> getChildren(T x) {
		return elements.get(x).getChildren();
	}
	
	/**
	 * A simpler hash code, also compatable with equals, but cheaper to compute,
	 * and just as good unless you've got a lot of different trees with the same root.
	 */
	public int hashCode() { return root.hashCode(); }

}
