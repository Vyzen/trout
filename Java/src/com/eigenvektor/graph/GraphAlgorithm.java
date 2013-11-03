/*
 *  A collection of graph algorithms.
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

package com.eigenvektor.graph;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

import com.eigenvektor.tree.DefaultTree;
import com.eigenvektor.tree.Tree;

public final class GraphAlgorithm<T> {

	/**
	 * Private constructor to prevent instantiation.
	 */
	private GraphAlgorithm() {
	};

	/**
	 * A comparator by edges that sorts by weight.
	 * 
	 * @param <T>
	 *            The node type of the edges.
	 */
	private static final class EdgeWeightComparator<T> implements
			Comparator<Graph.Edge<T>> {
		/**
		 * Private constructor to prevent instantiation.
		 */
		public EdgeWeightComparator() {
		}

		@Override
		public int compare(Graph.Edge<T> a, Graph.Edge<T> b) {
			if (a.weight < b.weight) {
				return -1;
			} else if (a.weight > b.weight) {
				return +1;
			} else {
				return 0;
			}
		}

	}

	/**
	 * Gets the minimal spanning tree of a graph rooted at a given node.
	 * 
	 * @param g
	 *            The graph.
	 * @param node
	 *            The root node.
	 * @return A minimal spanning tree of <code>g</code> rooted at
	 *         <code>node</code>.
	 */
	public static <T> Tree<T> minimalSpanningTree(final Graph<T> g, final T node) {
		if (!g.isVertex(node)) {
			throw new IllegalArgumentException("Node is not in g.");
		}

		DefaultTree<T> ret = new DefaultTree<T>(node);

		// Get a priority queue on edges that sorts by weight.
		Comparator<Graph.Edge<T>> comp = new EdgeWeightComparator<T>();
		PriorityQueue<Graph.Edge<T>> q = new PriorityQueue<Graph.Edge<T>>(10,
				comp);

		// Seed with all of the edges from the initial node.
		q.addAll(g.getEdgesFrom(node));

		while (!q.isEmpty()) {
			// Get the next edge.
			Graph.Edge<T> nextEdge = q.remove();

			if (!ret.isNode(nextEdge.to)) {
				// If the tree has not yet reached the far end of this edge,
				// add it to the tree.
				ret.addNode(nextEdge.to, nextEdge.from, nextEdge.weight);

				// Then queue up the edges from that node.
				q.addAll(g.getEdgesFrom(nextEdge.to));
			}
		}

		return ret;
	}

	/**
	 * Gets a topological sort of a directed acyclic graph.
	 * 
	 * @param g The graph.
	 * @return A list containing a topological sort of the nodes in g.
	 * @throws GraphAlgorithmException iff g is not acyclic.
	 */
	public static <T> List<T> topologicalSort(final Graph<T> g)	throws GraphAlgorithmException {
		Set<T> nodes = findTopologicalLeast(g);
		if (nodes.isEmpty()) 
		{
			throw new GraphAlgorithmException("Could not find start node for topological sort.");
		}
		
		List<T> ret = new ArrayList<T>();
		
		// These sets are so I can be non-destructive to the original graph.
		// All edges in this set are deemed to have been removed.
		Set<Graph.Edge<T>> removedEdges = new HashSet<Graph.Edge<T>>();
		// All nodes in this set are deemed to have been removed.
		Set<T> removedNodes = new HashSet<T>();
		while (!nodes.isEmpty())
		{
			// Get a node from the node set.
			T n = nodes.iterator().next();
			
			// Remove that node.
			removedEdges.addAll(g.getEdgesFrom(n));
			nodes.remove(n);
			removedNodes.add(n);
			ret.add(n);
			
			// Cycle through the neighbours.  If all if their edges have been removed
			// add them to the candidate node set.
			for (T m : g.getNeighbours(n))
			{		
				// If we've created a new "node without predecessors", add it to the candidate set.
				if (removedEdges.containsAll(g.getEdgesTo(m)))
				{
					nodes.add(m);
				}
			}
		}
				
		if (removedNodes.size() != g.getNumVertices())
		{
			throw new GraphAlgorithmException("Graph is not a DAG.");
		}
		
		return ret;
	}

	/**
	 * Finds the nodes of a graph that have no predecessors.
	 * 
	 * @param g The graph.
	 * @return The nodes of <code>g</code> which have no predecessors.
	 */
	private static <T> Set<T> findTopologicalLeast(final Graph<T> g) {
		Set<T> ret = new HashSet<T>();
		for (Iterator<T> it = g.vertexIterator() ; it.hasNext() ; )
		{
			T x = it.next();
			if (g.getPreNeighbours(x).isEmpty())
			{
				ret.add(x);
			}
		}
		return ret;
	}
}
