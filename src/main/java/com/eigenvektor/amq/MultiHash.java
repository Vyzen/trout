/*
 *  Interface for extracting multiple hashes from an object.
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
 * Specification for a class that takes multiple hash codes of an object.  Ideally,
 * these would be independent hash codes of the object.
 * 
 * @param <T> The type of object to hash.
 */
public interface MultiHash<T>
{
	/**
	 * Gets a number of hash codes of the object.
	 * 
	 * @param obj The object to get hash codes of.
	 * @param num The number of hash codes to get.
	 * @return An array of <code>num</code> hash codes for <code>obj</code>
	 */
	public int[] getHashes(T obj, int num);
	
}
