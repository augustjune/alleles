package genetic.genotype

import genetic.Population

/**
  * Construction, which allows to create instances of type `A`
  */
trait Scheme[+A] {
  def create(): A

  /**
    * Infinite stream of instances of type `A` created on demand
    */
  def samples: Stream[A] = create #:: samples

  /**
    * Returns n-size sequence of instances `A`
    */
  def make(n: Int): Population[A] = samples.take(n).toVector
}

object Scheme {
  /**
    * Return scheme which creates elements from cycled iterator
    */
  def fromIterator[A](iterF: () => Iterator[A]): Scheme[A] = new Scheme[A] {
    private val iterator: Iterator[A] = Iterator.continually {
      val i = iterF()
      if (i.isEmpty) throw new RuntimeException("Passed iterator is empty")
      else i
    }.flatten

    def create(): A = iterator.next()
  }

  /**
    * Return scheme which creates elements from cycled iterable
    */
  def fromIterable[A](iterable: Iterable[A]): Scheme[A] = fromIterator(() => iterable.iterator)

  def make[A](n: Int)(implicit source: Scheme[A]): Population[A] = source.make(n)

  /**
    * Instance of Scheme[A] which repeatedly creates same value `a`
    */
  def fromOne[A](a: A): Scheme[A] = () => a
}

