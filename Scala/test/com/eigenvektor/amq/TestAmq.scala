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

package com.eigenvektor.amq

import org.scalatest.FlatSpec

class TestQuotientFilter extends FlatSpec
{
   val qf = new QuotientFilter[Int](10)
  
  "Quotient Filter" should "initialize to empty" in {
    assert(qf.getNumOccupied == 0)
  }
  
  it should "recover input data" in 
  { 
    qf += 5
    qf += 27
    qf ++= 200 to 250
    
    assert(qf(5))
    assert(qf(27))
    for (x <- 200 to 250) { assert(qf(x)) }
    
    assert (!qf(100))
  }

}