package com.eigenvektor.priorityqueue

/** Implementation of a binomial tree with values
 *  
 *  Simple implementation with just an add method.
 */
final class BinomialTree[T](val value:T, val subtrees:List[BinomialTree[T]]) {
  
  /** Secondary constrctor for a single-node binomial tree */
  def this(value:T) = this(value, Nil)

  /** The size of the binomial tree
   *  
   *  This is always 2^(number of subtrees)
   */
  def size = 1 << subtrees.size
  
  /** Merges another tree with this one.
   *  
   *  @param other the tree to merge.
   *  @throws IllegalArgumentException if other is not the same size as this.
   */
  def merge(other:BinomialTree[T]) = {
    require(other.size == this.size, "other must have the same size as this.")
    new BinomialTree(value, other :: subtrees)
    }
  
}