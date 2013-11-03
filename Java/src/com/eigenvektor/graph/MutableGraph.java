/*
 *  Interface for a mutable directed graph.
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
