package genetic.genotype

import genetic.Population

/**
  * Construction, which allows to create instances of type `G`
  */
trait Scheme[+G] {
  def create: G

  /**
    * Infinite stream of instances of type `G` created on demand
    */
  def samples: Stream[G] = create #:: samples

  /**
    * Returns n-size sequence of instances `G`
    */
  def make(n: Int): Population[G] = samples.take(n).toVector
}

object Scheme {
  def pure[G](f: () => G): Scheme[G] = new Scheme[G] {
    def create: G = f()
  }

  def make[G](n: Int)(implicit source: Scheme[G]): Population[G] = source.make(n)

  /**
    * Instance of Scheme[G] which repeatedly creates same value `a`
    */
  def fromOne[G](a: G): Scheme[G] = new Scheme[G] {
    def create: G = a
  }
}

