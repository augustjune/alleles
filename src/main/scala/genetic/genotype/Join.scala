package genetic.genotype

import cats.kernel.Semigroup
import genetic.collections.IterablePair

/**
  * Construction, which allows to cross mix information about pair of values into new instances stored in IterablePair
  */
trait Join[G] extends Semigroup[G] {
  def cross(a: G, b: G): IterablePair[G] = IterablePair(combine(a,b), combine(b, a))
}

object Join {
  /**
    * Commutative version of Join, which allows to avoid overhead while computing both combinations of input pair
    */
  def commutative[G](f: (G, G) => G): Join[G] = new Join[G] {
    def combine(x: G, y: G): G = f(x, y)

    override def cross(a: G, b: G): IterablePair[G] = {
      val res = combine(a, b)
      IterablePair(res, res)
    }
  }
}
