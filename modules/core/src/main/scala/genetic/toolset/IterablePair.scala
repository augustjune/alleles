package genetic.toolset

import scala.collection.immutable.Iterable

/**
  * Iterable pair of values of the same type
  */
class IterablePair[A](l: A, r: A) extends Iterable[A] {

  def iterator: Iterator[A] = Iterator(l, r)
}
