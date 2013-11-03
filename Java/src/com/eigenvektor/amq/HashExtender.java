/*
 *  Hashing strategy that creates multiple hashes from a Java hash code.
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
 * A MultiHash implementation that creates multiple hash codes by mangling the bits of
 * an object's basic hashCode() implementation.  These are not independent hash codes,
 * but can still be useful for applications where only a portion of the codes is being
 * used.
 * 
 * @param <T>  The type of object to hash.
 */
public final class HashExtender<T> implements MultiHash<T>
{

	@Override
	public int[] getHashes(T obj, int num)
	{
		if (num < 0) { throw new IllegalArgumentException("num must be non-negative."); }
		
		if (num == 0) { return new int[0]; }
		
		int[] ret = new int[num];
		int buf = obj.hashCode();
		for (int idx = 0 ; idx < num ; ++idx)
		{
			ret[idx] = buf;
			buf = Integer.rotateRight(buf, 19);
			buf ^= 0x8088AAFF;
		}
		return ret;
	}

}
