package com.eigenvektor.graph;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * An adjacency list representation of a graph.
 *
 * @param <T> The node type of the graph.  Must be immutable, and 
 * implement equals() and hashCode() correctly.
 */
public final class AdjacencyListGraph<T> extends AbstractGraph<T> implements MutableGraph<T> {
	
	// The edge set.  First key is the "from" edge, second is the "to" edge, 
	// value is the edge weight.
	private Map<T, Map<T, Double>> adjacencyLists = new HashMap<>();
	private Map<T, Map<T, Double>> preAdjacenyLists = new HashMap<>();
	
	/**
	 * Creates a new AdjacenyListGraph with no vertices or edges.
	 */
	public AdjacencyListGraph()
	{
	}

	@Override
	public boolean isVertex(final T x) {
		return this.adjacencyLists.containsKey(x);
	}

	@Override
	public boolean isEdge(final T a, final T b) {
		// If a is not a vertex, clearly (a, b) is not an edge.
		if (!this.adjacencyLists.containsKey(a))
		{	
			return false;
		}
		else
		{
			return this.adjacencyLists.get(a).containsKey(b);
		}
	}

	@Override
	public double getWeight(final T a, final T b) {
		if (!this.adjacencyLists.containsKey(a))
		{
			return Double.POSITIVE_INFINITY;
		}
		else
		{
			Map<T, Double> neighbours = this.adjacencyLists.get(a);
			if (!neighbours.containsKey(b))
			{
				return Double.POSITIVE_INFINITY;
			}
			else
			{
				return neighbours.get(b);
			}
		}
	}
	
	@Override
	public Set<T> getVertices()
	{
		return Collections.unmodifiableSet(this.adjacencyLists.keySet());
	}
	
	@Override
	public int getNumVertices()
	{
		return this.adjacencyLists.size();
	}

	@Override
	public Set<T> getNeighbours(final T x) {
		if (!this.adjacencyLists.containsKey(x))
		{
			return Collections.emptySet();
		}
		else
		{
			return Collections.unmodifiableSet(this.adjacencyLists.get(x).keySet()); 
		}
	}
	
	@Override
	public Set<T> getPreNeighbours(final T x) {
		if (!this.preAdjacenyLists.containsKey(x))
		{
			return Collections.emptySet();
		}
		else
		{
			return Collections.unmodifiableSet(this.preAdjacenyLists.get(x).keySet());
		}
	}

	@Override
	public Iterator<T> vertexIterator() {
		return this.adjacencyLists.keySet().iterator();
	}

	@Override
	public void addVertex(T x) {
		// If x is already a vertex, just return.
		if (this.adjacencyLists.containsKey(x)) { return; }
		
		Map<T, Double> adj = new HashMap<>();
		this.adjacencyLists.put(x, adj);
		
		Map<T, Double> preAdj = new HashMap<>();
		this.preAdjacenyLists.put(x, preAdj);
	}

	@Override
	public void removeVertex(T x) {
		this.adjacencyLists.remove(x);
		this.preAdjacenyLists.remove(x);
	}

	@Override
	public void addEdge(T a, T b, double w) {
		// Make sure the vertices are both there.
		addVertex(a);
		addVertex(b);
		
		this.adjacencyLists.get(a).put(b, w);
		this.preAdjacenyLists.get(b).put(a, w);
	}

	@Override
	public void removeEdge(T a, T b) {
		if (!this.adjacencyLists.containsKey(a)) { return; }
		if (!this.adjacencyLists.containsKey(b)) { return; }
		
		this.adjacencyLists.get(a).remove(b);
		this.preAdjacenyLists.get(b).remove(a);
	}

}
