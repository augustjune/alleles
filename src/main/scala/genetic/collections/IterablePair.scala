package genetic.collections

import scala.collection.immutable.Iterable

case class IterablePair[A](l: A, r: A) extends Iterable[A] {

  def iterator: Iterator[A] = new Iterator[A] {
    private var current = 0

    def hasNext: Boolean = current < 2

    def next(): A = current match {
      case 0 => current += 1; l
      case 1 => current += 1; r
    }
  }
}
