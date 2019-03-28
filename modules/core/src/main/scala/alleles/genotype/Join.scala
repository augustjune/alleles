package alleles.genotype

import alleles.toolset.IterablePair

/**
  * Construction, which allows to cross mix information about pair of values into new instances stored in IterablePair
  */
trait Join[A] { self =>
  def cross(a: A, b: A): IterablePair[A]

  def recover(fix: A => A): Join[A] = alter(identity, fix)

  def alter[B](into: B => A, back: A => B): Join[B] =
    (a: B, b: B) => self.cross(into(a), into(b)).map(back)
}

object Join {
  /**
    * Symmetric version of Join, which allows to avoid overhead while computing both combinations of input pair
    */
  def symmetric[A](combine: (A, A) => A): Join[A] = (a: A, b: A) => {
    val res = combine(a, b)
    new IterablePair(res, res)
  }

  def pair[A](cross: (A, A) => (A, A)): Join[A] = (a: A, b: A) => cross(a, b) match {
    case (as, bs) => new IterablePair(as, bs)
  }
}
