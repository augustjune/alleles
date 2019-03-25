package genetic.genotype.standard.seq

import genetic.genotype.Variation
import genetic.genotype.syntax._
import genetic.toolset.RRandom

import scala.collection.SeqLike
import scala.collection.generic.CanBuildFrom

object Variations {

  def swap[CC[X] <: SeqLike[X, CC[X]], A](implicit cbf: CanBuildFrom[CC[_], A, CC[A]]): Variation[CC[A]] =
    (ind: CC[A]) => {
      // ToDo - handle same indices and individuals of size < 2
      val i1, i2 = RRandom.nextInt(ind.length)
      val temp = ind(i1)
      ind.updated(i1, ind(i2)).updated(i2, temp)
    }

  def elementVariation[CC[X] <: SeqLike[X, CC[X]], A]
  (implicit cbf: CanBuildFrom[CC[_], A, CC[A]], v: Variation[A]): Variation[CC[A]] =
    (ind: CC[A]) => {
      val i = RRandom.nextInt(ind.length)
      ind.updated(i, ind(i).modified)
    }
}
