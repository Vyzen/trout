package com.eigenvektor.tree;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestDefaultTree {

	String v1 = "one";
	String v2 = "two";
	String v3 = "three";
	String v4 = "four";
	String v5 = "five";
	String v6 = "six";
	String v7 = "seven";
	String v8 = "eight";

	DefaultTree<String> t;

	@Before
	public void setUp() throws Exception {
		t = new DefaultTree<String>(v1);

		DefaultTree<String> sub1 = new DefaultTree<String>(v5);
		sub1.addSubTree(new DefaultTree<String>(v6), 66);
		sub1.addSubTree(new DefaultTree<String>(v7), 77);

		DefaultTree<String> sub2 = new DefaultTree<String>(v2);
		DefaultTree<String> sub3 = new DefaultTree<String>(v3);
		sub3.addSubTree(new DefaultTree<String>(v4), 444);
		sub2.addSubTree(sub3, 33);

		t.addSubTree(sub1, 5);
		t.addSubTree(sub2, 2);
		t.addSubTree(new DefaultTree<String>(v8), 8);
	}

	@After
	public void tearDown() throws Exception {

	}

	@Test
	public void testConstruction() {
		assertTrue(t.getRoot().equals(v1));
		assertTrue(t.getSubTrees().size() == 3);
	}

	@Test
	public void testIteration() {
		// Depth first iteration.
		List<String> depthFirst = new ArrayList<String>();
		for (Iterator<String> it = t.getDepthFirstIterator(); it.hasNext();) {
			depthFirst.add(it.next());
		}
		assertTrue(depthFirst.size() == 8);
		assertTrue(depthFirst.get(0).equals(v1));

		// Depth first iteration.
		List<String> breadthFirst = new ArrayList<String>();
		for (Iterator<String> it = t.getBreadthFirstIterator(); it.hasNext();) {
			breadthFirst.add(it.next());
		}
		assertTrue(breadthFirst.size() == 8);
		assertTrue(breadthFirst.get(0).equals(v1));
		assertTrue(breadthFirst.get(7).equals(v4)); // The single element on level 3 must be last.
	}

}
