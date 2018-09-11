package genetic.genotype

import genetic.Population

trait Design[G] {
  def create: G

  def samples: Stream[G] = create #:: samples

  def make(n: Int): Population[G] = samples.take(n).toList
}

object Design {
  def make[G](n: Int)(implicit source: Design[G]): Population[G] = source.make(n)

  def pure[G](f: () => G): Design[G] = new Design[G] {
    def create: G = f()
  }

  def fromOne[G](a: G): Design[G] = new Design[G] {
    def create: G = a
  }
}

