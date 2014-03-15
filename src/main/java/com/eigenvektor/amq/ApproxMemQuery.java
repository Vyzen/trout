/*
 *  Interface for approximate membership queries.
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

package com.eigenvektor.amq;

/**
 * Specification for an approximate membership query.
 * 
 * @param <T> The type of element.
 */
public interface ApproxMemQuery<T>
{

	/**
	 * Adds an element to the AMQ.
	 * 
	 * @param x The element to add.
	 */
	public void add(T x);

	/**
	 * Tells whether an object has been added.  There is a chance of a false positive
	 * result, but not a false negative result.
	 * 
	 * @param x The element to query.
	 * @return <code>true</code> if <code>x</code> is in this set.  Very likely <code>false</code> otherwise.
	 */
	public boolean contains(T x);

}