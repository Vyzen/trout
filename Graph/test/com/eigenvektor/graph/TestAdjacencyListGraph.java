package com.eigenvektor.graph;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestAdjacencyListGraph {
	
	private MutableGraph<String> g;
	
	String v1;
	String v2;
	String v3;
	String v4;
	String v5;

	@Before
	public void setUp() throws Exception {
		g = new AdjacencyListGraph<String>();
		
		v1 = "one";
		v2 = "two";
		v3 = "three";
		v4 = "four";
		v5 = "five";
		
		g.addVertex(v1);
		g.addVertex(v2);
		
		g.addEdge(v4, v5, 6.0);
		g.addEdge(v1, v4, 7.0);
		g.addEdge(v1, v5, 1.0);
		g.addEdge(v1, v5, 3.0);
		g.addEdge(v4, v1, 25.0);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testCreation() 
	{		
		// Check that it gets the last update of weight.
		assertTrue(g.getWeight(v1, v5) ==  3.0);
		
		// Test some edges ane vertices.
		assertTrue(g.isEdge(v4, v5));
		assertFalse(g.isEdge(v5, v4));
		assertFalse(g.isVertex(v3));
		assertTrue(g.isVertex(v4));
		
		// Check that the neighbours sets come out OK.
		assertTrue(g.getNeighbours(v1).contains(v4));
		assertTrue(g.getNeighbours(v1).contains(v5));
		assertTrue(g.getNeighbours(v1).size() == 2);
		assertTrue(g.getNeighbours(v2).isEmpty());
	}
	
	@Test
	public void testIteration()
	{
		// Iterate through the vertices and make sure they all come out.
		Set<String> vertices = new HashSet<String>();
		for (Iterator<String> it = g.vertexIterator() ; it.hasNext() ; )
		{
			vertices.add(it.next());
		}
		assertTrue(vertices.size() == 4);
		assertTrue(vertices.contains(v1));
		assertTrue(vertices.contains(v2));
		assertFalse(vertices.contains(v3));
		assertTrue(vertices.contains(v4));
		assertTrue(vertices.contains(v5));
		
		// Iterate through the edges and make sure they all come out.
		Set<Graph.Edge<String>> edges = new HashSet<Graph.Edge<String>>();
		for (Iterator<Graph.Edge<String>> it = g.edgeIterator() ; it.hasNext() ; )
		{
			edges.add(it.next());
		}
		assertTrue(edges.size() == 4);
	}

}
