/*
 *  An iterator for graph edges.
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

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A read-only iterator for graph edges.
 */
final class GraphEdgeIterator<T> implements Iterator<Graph.Edge<T>>
{
	private final Graph<T> g;
	private final Iterator<T> vertexIterator;
	private Iterator<T> neighbourIterator = null;
	
	private T currentEdgeStart = null;
	private T currentEdgeEnd = null;
	
	public GraphEdgeIterator(Graph<T> g)
	{
		this.g = g;
		vertexIterator = g.getVertices().iterator();
		advance();
	}

	@Override
	public boolean hasNext() {
		// advance() populates the edge start and end, and nulls them when out of elements, 
		// so we just need to check those for null.
		return currentEdgeStart != null;
	}

	@Override
	public Graph.Edge<T> next() {
		// If we don't have a current edge, throw.
		if (currentEdgeStart == null) { throw new NoSuchElementException("Out of edges.");}

		double weight = g.getWeight(currentEdgeStart, currentEdgeEnd);
		Graph.Edge<T> ret = new Graph.Edge<T>(currentEdgeStart, currentEdgeEnd, weight);
		advance();
		return ret;
	}
	
	private void advance()
	{
		boolean primedForNextElement = false;
		boolean outOfElements = false;
		do
		{
			// If we have a neighbour iterator with more elements, we can continue easily.
			if (neighbourIterator != null && neighbourIterator.hasNext())
			{
				currentEdgeEnd = neighbourIterator.next();
				primedForNextElement = true;
				outOfElements = false;
			}
			else if (vertexIterator.hasNext())
			{
				// We need to advance to the next vertex.
				currentEdgeStart = vertexIterator.next();
				neighbourIterator = g.getNeighbours(currentEdgeStart).iterator();
				
				// If the neighbour iterator now has elements (i.e. the new source vertex has neighbours)
				// we can prime this iterator.  Otherwise we can't and should loop again.
				if (neighbourIterator.hasNext())
				{
					currentEdgeEnd = neighbourIterator.next();
					outOfElements = false;
					primedForNextElement = true;
				}
				else
				{
					outOfElements = false;
					primedForNextElement = false;
				}
			}
			else
			{
				// Neither iterator has more, put this iterator in a consistent state.
				currentEdgeStart = null;
				currentEdgeEnd = null;
				outOfElements = true;
				primedForNextElement = false;
			}
		} while (!primedForNextElement && !outOfElements);
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException("remove() not supported for this iterator.");
	}

}
