package genetic.genotype

import genetic.Population

trait Scheme[+G] {
  def create: G

  def samples: Stream[G] = create #:: samples

  def make(n: Int): Population[G] = samples.take(n).toList
}

object Scheme {
  def make[G](n: Int)(implicit source: Scheme[G]): Population[G] = source.make(n)

  def pure[G](f: () => G): Scheme[G] = new Scheme[G] {
    def create: G = f()
  }

  def fromOne[G](a: G): Scheme[G] = new Scheme[G] {
    def create: G = a
  }
}

