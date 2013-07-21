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
	public boolean isVertex(final T x);
	
	/**
	 * Tells if there is a directed edge between two vertices in the graph.
	 * 
	 * @param a The first vertex.
	 * @param b The second vertex.
	 * @return <i>true</i> iff <i>(a, b)</i> is a directed edge of the graph.
	 */
	public boolean isEdge(final T a, final T b);
	
	/**
	 * Gets the weight of a directed edge of the graph.
	 * 
	 * @param a The first vertex.
	 * @param b The second vertex.
	 * @return The weight of the edge <i>(a, b)</i>.  Returns infinite if <i>(a, b)</i> 
	 * is not an edge of the graph.
	 */
	public double getWeight(final T a, final T b);

	/**
	 * Gets the neighbours of a given vertex.
	 * 
	 * @param x The vertex to consider.
	 * @return The set of neighbours of x.  The returned set is not guaranteed to be mutable.
	 */
	Set<T> getNeighbours(final T x);
	
	/**
	 * Gets the edges from a given vertex.
	 * 
	 * @param x The vertex to consider.
	 * @return The set of edges from x.  The returned set is not guaranteed to be mutable.
	 */
	Set<Edge<T>> getEdgesFrom(final T x);
	
	/**
	 * Gets the edges to a given vertex.
	 * 
	 * @param x The vertex to consider.
	 * @return The set of edges to x.  The returned set is not guaranteed to be mutable.
	 */
	Set<Edge<T>> getEdgesTo(final T x);
	
	/**
	 * Gets the set of points for which x is a neighbour.
	 * 
	 * @param x The vertex to consider.
	 * @return The points for which x is a neighbour.  The returned set is not guaranteed to be mutable.
	 */
	Set<T> getPreNeighbours(final T x);
	
	/**
	 * Gets an iterator for the vertices of the graph.
	 * 
	 * @return an iterator for the vertices of the graph.
	 */
	Iterator<T> vertexIterator();
	
	/**
	 * Small class to represent an edge.
	 *
	 * @param <T> The vertex type.
	 */
	public static final class Edge<T>
	{
		public Edge(T from, T to, double weight)
		{
			this.from = from;
			this.to = to;
			this.weight = weight;
		}
		
		public final T from;
		public final T to;
		public final double weight;
		
		public boolean equals(Object o)
		{
			if (!(o instanceof Edge))
			{
				return false;
			}
			else
			{
				Edge<?> oe = ((Edge<?>) o);
				return oe.from.equals(this.from) && oe.to.equals(this.to);
			}
		}
		
		public int hashCode()
		{
			return from.hashCode() ^ to.hashCode() ^ 31;
		}
		
		@Override
		public String toString()
		{
			StringBuffer buf = new StringBuffer();
			buf.append("(");
			buf.append(from);
			buf.append(", ");
			buf.append(to);
			buf.append(")[");
			buf.append(weight);
			buf.append("]");
			return buf.toString();
		}
	}
	
	/**
	 * Gets an iterator over all of the edges of the graph.
	 * 
	 * @return an iterator over all of the edges of the graph.
	 */
	public Iterator<Edge<T>> edgeIterator();
	
	
	/**
	 * Gets all the vertices of the graph.
	 * 
	 * @return  The set of all vertices.  The returned set is not guaranteed to be mutable.
	 */
	public Set<T> getVertices();
	
	/**
	 * Gets the number of vertices in the graph.
	 * 
	 * @return The number of vertices in the graph.
	 */
	public int getNumVertices();
	
	/**
	 * Gets all the edges of the graph.
	 * 
	 * @return  The set of all edges.  The returned set is not guaranteed to be mutable.
	 */
	public Set<Edge<T>> getEdges();
	
	/**
	 * Gets the number of edges in the graph.
	 * 
	 * @return The number of edges in the graph.
	 */
	public int getNumEdges();
}
