package com.eigenvektor.priorityqueue

/** A common supertrait for all heaps 
 *
 *  @type E the element type.  
 */
trait Heap[E] {
  
  /** Tells if the heap is empty */
  val isEmpty:Boolean
  
  /** The size of the heap */
  val size:Int
  
  /** Adds an element to this heap
   *  
   *  @param x The element to add.
   *  @return A copy of this heap with x added.
   */
  def +(x:E):Heap[E]
  
  /** Adds a series of elements to this.
   *  
   *  @param t The elements to add.
   */
  def ++(t:Traversable[E]) = t.foldLeft(this)((x,y) => x + y)
  
  /** Gets the minimum value in the heap */
  val min:E
  
  /** Removes the minimum element of the heap
   *  
   *  @return a pair of the removed element and the heap without the element.
   */
  val removeMin:Pair[E, Heap[E]]
}