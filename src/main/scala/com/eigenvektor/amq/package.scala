/*
 *  Scala ops for implicit conversions for AMQs.
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

package com.eigenvektor

import scala.language.implicitConversions

/** Implicit conversions for appoximate memory query interface. */
package object amq {

  /** Implicit conversion from ApproxMemQuery to AmqOps */
  implicit def amqToAmqOps[T](amq:ApproxMemQuery[T]) = new AmqOps[T](amq);
}