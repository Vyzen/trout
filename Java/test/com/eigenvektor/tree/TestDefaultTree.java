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
		t.addNode(v5, v1, 5);
		t.addNode(v6, v5, 66);
		t.addNode(v7, v5, 77);

		t.addNode(v2, v1, 2);
		t.addNode(v3, v2, 33);
		t.addNode(v4, v3, 333);

		t.addNode(v8, v1, 8);
	}

	@After
	public void tearDown() throws Exception {

	}

	@Test
	public void testConstruction() {
		assertTrue(t.getRoot().equals(v1));
		assertTrue(t.getChildren(t.getRoot()).size() == 3);
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
