package com.eigenvektor.graph;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * An abstract implementation of Graph which provides working toString(), hashCode(), equals(), and edgeIterator()
 * defined in terms of the other methods.
 * 
 * @param <T> The type of vertex.
 */
public abstract class AbstractGraph<T> implements Graph<T> {


	@Override
	/**
	 * Gets a hash code for the graph.
	 */
	public int hashCode()
	{
		int hash = 0;
		for (Iterator<T> it = vertexIterator() ; it.hasNext() ; )
		{
			hash ^= it.next().hashCode();
		}
		for (Iterator<Graph.Edge<T>> it = edgeIterator() ; it.hasNext() ; )
		{
			hash ^= it.next().hashCode();
		}
		return hash;
	}
	
	@Override
	/**
	 * Semantic equality for Graphs.
	 */
	public boolean equals(Object o)
	{
		if (o == this)
		{
			return true;
		}
		else if (!(o instanceof Graph))
		{
			return false;
		}
		else
		{
			@SuppressWarnings("unchecked")
			Graph<T> g = (Graph<T>) o;
			
			// First check for all the same vertices.
			Set<T> v = this.getVertices();
			if (!g.getVertices().equals(v)) { return false; }
			
			// For each vertex, make sure it has the same neighbours.
			for (T x : v)
			{
				if (!g.getNeighbours(x).equals(this.getNeighbours(x))) { return false; }
			}
			
			return true;
		}
	}
	
	@Override
	/**
	 * String representation of this.
	 */
	public String toString()
	{
		StringBuffer buf = new StringBuffer();
		buf.append("Vertices: ");
		buf.append(getVertices());
		buf.append(" Edges: ");
		buf.append(getEdges());
		return buf.toString();
	}

	@Override
	/**
	 * Gets the edges of the graph.  Note that this implementation assumes that
	 * T has hashCode() and equals() implemented semantically.  Do not use for
	 * T that does not have that property. 
	 */
	public Set<Edge<T>> getEdges()
	{
		Set<Edge<T>> edges = new HashSet<Edge<T>>();
		for (Iterator<Edge<T>> it = this.edgeIterator() ; it.hasNext() ; )
		{
			edges.add(it.next());
		}
		return Collections.unmodifiableSet(edges);
	}
	
	@Override
	public Iterator<Edge<T>> edgeIterator()
	{
		return new GraphEdgeIterator<T>(this);
	}
}
