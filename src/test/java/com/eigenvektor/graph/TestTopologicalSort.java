/*
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

import static org.junit.Assert.*;

import java.util.Iterator;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestTopologicalSort
{
	
	private MutableGraph<String> g1 = new AdjacencyListGraph<String>();
	private MutableGraph<String> g2 = new AdjacencyListGraph<String>();
	private MutableGraph<String> g3 = new AdjacencyListGraph<String>();
	private MutableGraph<String> g4 = new AdjacencyListGraph<String>();

	private final String v1 = "one";
	private final String v2 = "two";
	private final String v3 = "three";
	private final String v4 = "four";
	private final String v5 = "five";
	private final String v6 = "six";
	private final String v7 = "seven";

	@Before
	public void setUp() throws Exception
	{
		// Create g1 as a straight line.
		g1.addEdge(v1, v2, 1);
		g1.addEdge(v2, v3, 1);
		g1.addEdge(v3, v4, 1);
		g1.addEdge(v4, v5, 1);
		g1.addEdge(v5, v6, 1);
		g1.addEdge(v6, v7, 1);
		
		// Set up g2 as a backwards tree.
		g2.addEdge(v1, v5, 1);
		g2.addEdge(v2, v5, 1);
		g2.addEdge(v3, v6, 1);
		g2.addEdge(v4, v6, 1);
		g2.addEdge(v5, v6, 1);
		g2.addEdge(v5, v7, 1);
		g2.addEdge(v6, v7, 1);
		
		// Set up g3 so it has some untaken edges.
		g3.addEdge(v1, v2, 1);
		g3.addEdge(v1, v3, 1);
		g3.addEdge(v2, v4, 1);
		g3.addEdge(v3, v5, 1);
		g3.addEdge(v4, v6, 1);
		g3.addEdge(v5, v7, 1);
		g3.addEdge(v6, v7, 1);
		g3.addEdge(v3, v4, 1);
		
		// Set up g4 to be a failure.
		g4.addEdge(v1, v2, 1);
		g4.addEdge(v2, v3, 1);
		g4.addEdge(v3, v4, 1);
		g4.addEdge(v4, v5, 1);
		g4.addEdge(v5, v6, 1);
		g4.addEdge(v6, v7, 1);
		g4.addEdge(v6, v2, 1); // Back edge makes a cycle.
	}

	@After
	public void tearDown() throws Exception
	{
	}

	@Test
	public void test()
	{
		try
		{
			List<String> ts1 = GraphAlgorithm.topologicalSort(g1);
			
			// This should just be the nodes in order.
			assertTrue(isTopoSortOf(g1, ts1));
		} 
		catch (GraphAlgorithmException e)
		{
			fail("Unexpected Exception.");
		}
		
		try
		{
			List<String> ts2 = GraphAlgorithm.topologicalSort(g2);

			assertTrue(isTopoSortOf(g2, ts2));
		}
		catch (GraphAlgorithmException e)
		{
			fail("Unexpected Exception.");
		}
		
		try
		{
			List<String> ts3 = GraphAlgorithm.topologicalSort(g3);

			assertTrue(isTopoSortOf(g3, ts3));
		}
		catch (GraphAlgorithmException e)
		{
			fail("Unexpected Exception.");
		}
		
		try
		{
			GraphAlgorithm.topologicalSort(g4);

			fail("Unexpected Success.");
		}
		catch (GraphAlgorithmException e)
		{
			assertTrue(true);
		}
	}
	
	/**
	 * Tells if a list is a topological sort of a graph.
	 * 
	 * @param g The graph.
	 * @param l The list
	 * @return <code>true</code> iff l is a topological sort of g.
	 */
	private static boolean isTopoSortOf(Graph<String> g, List<String> l)
	{
		for (Iterator<Graph.Edge<String>> it = g.edgeIterator() ; it.hasNext(); )
		{
			Graph.Edge<String> e = it.next();
			
			// The list must contain both ends of the edge.
			if (!l.contains(e.from)) { return false; }
			if (!l.contains(e.to)) { return false; }
			
			// The from element must come before the to element.
			if (l.indexOf(e.to) < l.indexOf(e.from)) { return false; }
		}
		
		return true;
	}

}
