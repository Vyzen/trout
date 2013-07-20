package com.eigenvektor.graph;

import java.util.Comparator;
import java.util.PriorityQueue;

import com.eigenvektor.tree.DefaultTree;
import com.eigenvektor.tree.Tree;

public final class GraphAlgorithm<T> {

	/**
	 * Private constructor to prevent instantiation.
	 */
	private GraphAlgorithm() {};
	

	private static class EdgeWeightComparator<T> implements Comparator<Graph.Edge<T>>
	{
		/**
		 * Private constructor to prevent instantiation.
		 */
		public EdgeWeightComparator() {}

		@Override
		public int compare(Graph.Edge<T> a, Graph.Edge<T> b) {
			if (a.weight < b.weight)
			{
				return -1;
			}
			else if (a.weight > b.weight)
			{
				return +1;
			}
			else
			{
				return 0;
			}
		}


		
	}
	
	/**
	 * Gets the minimal spanning tree of a graph rooted at a given node.
	 * 
	 * @param g The graph.
	 * @param node The root node.
	 * @return A minimal spanning tree of <code>g</code> rooted at <code>node</code>.
	 */
	public static <T> Tree<T> minimalSpanningTree(final Graph<T> g, final T node)
	{
		if (!g.isVertex(node))
		{
			throw new IllegalArgumentException("Node is not in g.");
		}
		
		DefaultTree<T> ret = new DefaultTree<T>(node);
		
		// Get a priority queue on edges that sorts by weight.
		Comparator<Graph.Edge<T>> comp = new EdgeWeightComparator<T>();
		PriorityQueue<Graph.Edge<T>> q = new PriorityQueue<Graph.Edge<T>>(10, comp);
		
		// Seed with all of the edges from the initial node.
		q.addAll(g.getEdgesFrom(node));
		
		while (!q.isEmpty())
		{
			// Get the next edge.
			Graph.Edge<T> nextEdge = q.remove();
			
			if (!ret.isNode(nextEdge.to))
			{
				// If the tree has not yet reached the far end of this edge,
				// add it to the tree.
				ret.addNode(nextEdge.to, nextEdge.from, nextEdge.weight);
				
				// Then queue up the edges from that node.
				q.addAll(g.getEdgesFrom(nextEdge.to));
			}
		}
		
		return ret;
	}
	
}
