/*
 *  Random Access list.
 *  Copyright (C) 2014 Michael Thorsley
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

package com.eigenvektor.collections.immutable

import com.eigenvektor.collections.RandomAccessList.CompleteBinaryTree

/** An implementation of the Random Access List that is guaranteed to be immutable. */
final class RandomAccessList[+A] private[collections] (trees:List[CompleteBinaryTree[A]])
  extends com.eigenvektor.collections.RandomAccessList[A](trees) {

  def newInstance[B](trees:List[CompleteBinaryTree[B]]) = new RandomAccessList[B](trees)
  
}