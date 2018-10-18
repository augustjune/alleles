package examples

import genetic.RRandom

import scala.language.implicitConversions

package object geneticProgramming {

  class RandOr[T](val possibilities: List[T]) extends AnyVal {
    def or(other: T): RandOr[T] = new RandOr(other :: possibilities)
  }

  implicit def elToOr[T](t: T): RandOr[T] = new RandOr(List(t))

  implicit def orToEl[T](randOr: RandOr[T]): T = RRandom.chooseOne(randOr.possibilities)
}
