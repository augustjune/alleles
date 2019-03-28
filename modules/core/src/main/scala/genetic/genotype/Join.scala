package genetic.genotype

import cats.kernel.Semigroup
import genetic.toolset.IterablePair

/**
  * Construction, which allows to cross mix information about pair of values into new instances stored in IterablePair
  */
trait Join[A] {
  def cross(a: A, b: A): IterablePair[A]
}

object Join {
  /**
    * Join which uses identical function for combining both elements of output pair
    */
  def symmetric[A](combine: (A, A) => A): Join[A] = (a: A, b: A) => new IterablePair(combine(a, b), combine(b, a))

  /**
    * Commutative version of Join, which allows to avoid overhead while computing both combinations of input pair
    */
  def commutative[A](combine: (A, A) => A): Join[A] = (a: A, b: A) => {
    val res = combine(a, b)
    new IterablePair(res, res)
  }

  def pair[A](cross: (A, A) => (A, A)): Join[A] = (a: A, b: A) => cross(a, b) match {
    case (as, bs) => new IterablePair(as, bs)
  }
}
