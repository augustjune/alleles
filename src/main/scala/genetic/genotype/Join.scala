package genetic.genotype

import genetic.collections.IterablePair

trait Join[G] {
  def group(a: G, b: G): IterablePair[G]
}

object Join {
  def pure[G](f: (G, G) => G): Join[G] = (a: G, b: G) => IterablePair(f(a, b), f(b, a))

  def commutative[G](f: (G, G) => G): Join[G] = (a: G, b: G) => {
    val res = f(a, b)
    IterablePair(res, res)
  }
}
