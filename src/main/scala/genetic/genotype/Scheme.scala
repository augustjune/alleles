package genetic.genotype

import genetic.Population

/**
  * Construction, which allows to create instances of type `G`
  */
trait Scheme[+G] {
  def create(): G

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
  /**
    * Return scheme which creates elements from cycled iterator
    */
  def fromIterator[G](iterF: () => Iterator[G]): Scheme[G] = new Scheme[G] {
    private val iterator: Iterator[G] = Iterator.continually {
      val i = iterF()
      if (i.isEmpty) throw new RuntimeException("Passed iterator is empty")
      else i
    }.flatten

    def create(): G = iterator.next()
  }

  /**
    * Return scheme which creates elements from cycled iterable
    */
  def fromIterable[G](iterable: Iterable[G]): Scheme[G] = fromIterator(() => iterable.iterator)

  def make[G](n: Int)(implicit source: Scheme[G]): Population[G] = source.make(n)

  /**
    * Instance of Scheme[G] which repeatedly creates same value `a`
    */
  def fromOne[G](a: G): Scheme[G] = () => a
}

