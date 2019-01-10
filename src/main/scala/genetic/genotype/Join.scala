package genetic.genotype

import cats.kernel.Semigroup
import genetic.toolset.IterablePair

/**
  * Construction, which allows to cross mix information about pair of values into new instances stored in IterablePair
  */
trait Join[G] {
  def cross(a: G, b: G): IterablePair[G]
}

object Join {
  /**
    * Join which uses identical function for combining both elements of output pair
    */
  def symmetric[G](combine: (G, G) => G): Join[G] = (a: G, b: G) => IterablePair(combine(a, b), combine(b, a))

  /**
    * Commutative version of Join, which allows to avoid overhead while computing both combinations of input pair
    */
  def commutative[G](combine: (G, G) => G): Join[G] = (a: G, b: G) => {
    val res = combine(a, b)
    IterablePair(res, res)
  }

  def pair[G](cross: (G, G) => (G, G)): Join[G] = (a: G, b: G) => cross(a, b) match {
    case (as, bs) => IterablePair(as, bs)
  }

  def singlePoint[G](split: G => (G, G))(combineParts: (G, G) => G): Join[G] = (x: G, y: G) => {
    val (x1, x2) = split(x)
    val (y1, y2) = split(y)
    IterablePair(combineParts(x1, y2), combineParts(y1, x2))
  }

  def samePoint[G, P](p: (G, G) => P, splitWith: G => P => (G, G))(combineParts: (G, G) => G): Join[G] = (x: G, y: G) => {
    val point = p(x, y)
    val (x1, x2) = splitWith(x)(point)
    val (y1, y2) = splitWith(y)(point)
    IterablePair(combineParts(x1, y2), combineParts(y1, x2))
  }
}
