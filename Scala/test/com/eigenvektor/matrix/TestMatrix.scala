package com.eigenvektor.matrix

/**
 * A test for the scala-fied matrix stuff.
 */
object TestMatrix {
 
  def main(args:Array[String]) = {
    val f1 = new FullMatrix(3,3)
    val f2 = new FullMatrix(3,3)
    
    for (j <- 0 to 2 ; k <- 0 to 2) 
    { 
      f1.set(j,k,j*k)
      f2.set(j,k,j+k) 
    }
    println(f1);
    println(f2);
    println(f1 + f2)
    println(f1 - f1)
    
    f1 += f2;
    println(f1)
  }
}