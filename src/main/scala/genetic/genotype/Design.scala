package genetic.genotype

import genetic.Population

trait Design[A] {
  def create: A

  def samples: Stream[A] = create #:: samples
}

object Design {
  def make[A](n: Int)(implicit source: Design[A]): Population[A] = source.samples.take(n).toList

  def pure[A](f: () => A): Design[A] = new Design[A] {
    def create: A = f()
  }

  def fromOne[A](a: A): Design[A] = new Design[A] {
    def create: A = a
  }
}

