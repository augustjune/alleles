package genetic.genotype

import genetic.Population

trait Design[A] {
  def create: A

  def samples: Stream[A] = create #:: samples

  def make(n: Int): Population[A] = samples.take(n).toList
}

object Design {
  def make[A](n: Int)(implicit source: Design[A]): Population[A] = source.make(n)

  def pure[A](f: () => A): Design[A] = new Design[A] {
    def create: A = f()
  }

  def fromOne[A](a: A): Design[A] = new Design[A] {
    def create: A = a
  }
}

