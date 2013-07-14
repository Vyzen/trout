package com.eigenvektor.graph;

/**
 * Specification of a Mutable directed graph.
 * 
 * @param <T> The node type of the graph.
 */
public interface MutableGraph<T> extends Graph<T> {

	/**
	 * Adds a vertex to the graph connected to nothing.
	 * 
	 * @param x The vertex to add.
	 */
	public void addVertex(T x);
	
	/**
	 * Removes a vertex from the graph, and all edges that contain that vertex.
	 * 
	 * @param x The vertex to remove.
	 */
	public void removeVertex(T x);
	
	/**
	 * Adds a directed edge to the graph.
	 * 
	 * @param a The start of the edge.
	 * @param b The end of the edge.
	 * @param w The weight of the edge.
	 */
	public void addEdge(T a, T b, double w);
	
	/**
	 * Removes an edge form the graph.  Leaves the vertices in the graph, even if
	 * they no longer have any edges.
	 * 
	 * @param a The start of the edge.
	 * @param b The end of the edge.
	 */
	public void removeEdge(T a, T b);
}
