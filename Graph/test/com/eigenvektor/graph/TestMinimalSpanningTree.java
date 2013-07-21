package com.eigenvektor.graph;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.eigenvektor.tree.Tree;

public class TestMinimalSpanningTree {

	private MutableGraph<String> g1 = new AdjacencyListGraph<String>();
	private MutableGraph<String> g2 = new AdjacencyListGraph<String>();
	private MutableGraph<String> g3 = new AdjacencyListGraph<String>();
	private MutableGraph<String> g4 = new AdjacencyListGraph<String>();

	final String v1 = "one";
	final String v2 = "two";
	final String v3 = "three";
	final String v4 = "four";
	final String v5 = "five";
	final String v6 = "six";

	@Before
	public void setUp() throws Exception {
		// Set up g1 as an undirected loop.
		g1.addEdge(v1, v2, 1);
		g1.addEdge(v2, v3, 1);
		g1.addEdge(v3, v4, 1);
		g1.addEdge(v4, v5, 1);
		g1.addEdge(v5, v6, 1);
		g1.addEdge(v6, v1, 1);
		g1.addEdge(v2, v1, 1);
		g1.addEdge(v3, v2, 1);
		g1.addEdge(v4, v3, 1);
		g1.addEdge(v5, v4, 1);
		g1.addEdge(v6, v5, 1);
		g1.addEdge(v1, v6, 1);

		// Set up g1 as an undirected loop.
		g2.addEdge(v1, v2, 1);
		g2.addEdge(v2, v3, 1);
		g2.addEdge(v3, v4, 1);
		g2.addEdge(v4, v5, 1);
		g2.addEdge(v5, v6, 1);
		g2.addEdge(v6, v1, 100);
		g2.addEdge(v2, v1, 1);
		g2.addEdge(v3, v2, 1);
		g2.addEdge(v4, v3, 1);
		g2.addEdge(v5, v4, 1);
		g2.addEdge(v6, v5, 1);
		g2.addEdge(v1, v6, 100);

		// Set up g3 as a directed loop, with a short cut.
		g3.addEdge(v1, v2, 1);
		g3.addEdge(v2, v3, 1);
		g3.addEdge(v3, v4, 1);
		g3.addEdge(v4, v5, 1);
		g3.addEdge(v5, v6, 1);
		g3.addEdge(v6, v1, 1);
		g3.addEdge(v2, v5, 1);

		// Set up g3 as a directed loop, with a long cut.
		g4.addEdge(v1, v2, 1);
		g4.addEdge(v2, v3, 1);
		g4.addEdge(v3, v4, 1);
		g4.addEdge(v4, v5, 1);
		g4.addEdge(v5, v6, 1);
		g4.addEdge(v6, v1, 1);
		g4.addEdge(v2, v5, 100);

	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Gets the total weight of a tree above a node.
	 * 
	 * @param t
	 *            The tree.
	 * @param node
	 *            The node.
	 * @return The sum of the weights of the nodes along the path from the root
	 *         to the node inclusive.
	 */
	private static double totalWeight(Tree<String> t, String node) {
		if (!t.isNode(node)) {
			return 0.0;
		}

		double sum = 0.0;
		for (String curNode = node; curNode != null; curNode = t
				.getParent(curNode)) {
			sum += t.getWeight(curNode);
		}

		return sum;
	}

	@Test
	public void test() {

		Tree<String> mst1 = GraphAlgorithm.minimalSpanningTree(g1, v1);
		Tree<String> mst2 = GraphAlgorithm.minimalSpanningTree(g2, v1);
		Tree<String> mst3 = GraphAlgorithm.minimalSpanningTree(g3, v1);
		Tree<String> mst4 = GraphAlgorithm.minimalSpanningTree(g4, v1);

		// These two should be equal.
		assertTrue(mst2.equals(mst4));

		// This should be a single path.
		assertTrue(totalWeight(mst2, v6) == 5.0);

		// These are two different paths.
		assertTrue(totalWeight(mst1, v5) == 2.0);
		assertTrue(totalWeight(mst1, v3) == 2.0);

		// The short cut should be taken.
		assertTrue(totalWeight(mst3, v6) == 3.0);
		assertTrue(totalWeight(mst3, v2) == 1.0);
		assertTrue(totalWeight(mst3, v4) == 3.0);
	}

}
