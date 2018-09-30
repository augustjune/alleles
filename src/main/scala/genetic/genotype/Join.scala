package genetic.genotype

import cats.kernel.Semigroup
import genetic.collections.IterablePair

trait Join[G] extends Semigroup[G] {
  def cross(a: G, b: G): IterablePair[G] = IterablePair(combine(a,b), combine(b, a))
}

object Join {
  def commutative[G](f: (G, G) => G): Join[G] = new Join[G] {
    def combine(x: G, y: G): G = f(x, y)

    override def cross(a: G, b: G): IterablePair[G] = {
      val res = combine(a, b)
      IterablePair(res, res)
    }
  }
}
