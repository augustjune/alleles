package genetic.genotype.standard.seq

import genetic.genotype.Join
import genetic.toolset.{IterablePair, RRandom}

import scala.collection.SeqLike
import scala.collection.generic.CanBuildFrom

object Joins {

  def singlePoint[CC[X] <: SeqLike[X, CC[X]], A]
  (implicit cbf: CanBuildFrom[CC[_], A, CC[A]]): Join[CC[A]] =
    (a: CC[A], b: CC[A]) => {
      val pivot = RRandom.nextInt(a.length)
      val (a1, a2) = a.splitAt(pivot)
      val (b1, b2) = b.splitAt(pivot)
      new IterablePair[CC[A]](a1 ++ b2, b1 ++ a2)
    }
}
