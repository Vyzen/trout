package com.eigenvektor.graph;

import java.util.Iterator;
import java.util.Set;

/**
 * Specification for an immutable directed, weighted Graph object.  T is the node type.
 *
 */
public interface Graph<T> {

	/**
	 * Tells if a given vertex is in the vertex set of the graph.
	 * 
	 * @param x The element to test.
	 * @return <i>true</i> iff <i>x</i> is in the graph.
	 */
	public boolean isVertex(T x);
	
	/**
	 * Tells if there is a directed edge between two vertices in the graph.
	 * 
	 * @param a The first vertex.
	 * @param b The second vertex.
	 * @return <i>true</i> iff <i>(a, b)</i> is a directed edge of the graph.
	 */
	public boolean isEdge(T a, T b);
	
	/**
	 * Gets the weight of a directed edge of the graph.
	 * 
	 * @param a The first vertex.
	 * @param b The second vertex.
	 * @return The weight of the edge <i>(a, b)</i>.  Returns infinite if <i>(a, b)</i> 
	 * is not an edge of the graph.
	 */
	public double getWeight(T a, T b);
	
	/**
	 * Gets the neighbours of a given vertex.
	 * 
	 * @param x The vertex to consider.
	 * @return The set of neighbours of x.  The returned set is not guaranteed to be mutable.
	 */
	Set<T> getNeighbours(T x);
	
	/**
	 * Gets an iterator for the vertices of the graph.
	 * 
	 * @return an iterator for the verices of the graph.
	 */
	Iterator<T> vertexIterator();
}
